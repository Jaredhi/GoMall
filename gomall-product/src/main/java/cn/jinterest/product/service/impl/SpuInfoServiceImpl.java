package cn.jinterest.product.service.impl;

import cn.jinterest.common.to.SkuReductionTo;
import cn.jinterest.common.to.SpuBoundTo;
import cn.jinterest.common.utils.R;
import cn.jinterest.product.entity.*;
import cn.jinterest.product.feign.CouponFeignService;
import cn.jinterest.product.service.*;
import cn.jinterest.product.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.jinterest.common.utils.PageUtils;
import cn.jinterest.common.utils.Query;

import cn.jinterest.product.dao.SpuInfoDao;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Slf4j
@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {

    @Autowired
    SpuInfoDescService spuInfoDescService;

    @Autowired
    SpuImagesServiceImpl spuImagesService;

    @Autowired
    AttrService attrService;

    @Autowired
    ProductAttrValueService productAttrValueService;

    @Autowired
    CouponFeignService couponFeignService;

    @Autowired
    SkuInfoService skuInfoService;

    @Autowired
    SkuImagesService skuImagesService;

    @Autowired
    SkuSaleAttrValueService skuSaleAttrValueService;

    @Autowired
    BrandService brandService;

    @Autowired
    CategoryService categoryService;



    // 当前线程共享同样的数据 商品id
    ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void saveSpuInfo(SpuSaveVo spuSaveVo) {
        //1、保存spu基本信息 pms_spu_info
        SpuInfoEntity infoEntity = new SpuInfoEntity();
        BeanUtils.copyProperties(spuSaveVo,infoEntity);
        infoEntity.setCreateTime(new Date());
        infoEntity.setUpdateTime(new Date());
        this.saveBaseSpuInfo(infoEntity);

        //2、保存Spu的描述图片 pms_spu_info_desc
        this.saveSpuInfoDesc(spuSaveVo);

        //3、保存spu的图片集 pms_spu_images
        saveSpuImages(spuSaveVo);

        //4、保存spu的规格参数;pms_product_attr_value
        saveProductAttrValue(spuSaveVo);

        //5、保存spu的积分信息；gomall_sms->sms_spu_bounds
        Bounds bounds = spuSaveVo.getBounds();
        SpuBoundTo spuBoundTo = new SpuBoundTo();
        BeanUtils.copyProperties(bounds,spuBoundTo);
        spuBoundTo.setSpuId(threadLocal.get());

        R r = couponFeignService.saveSpuBounds(spuBoundTo);
        if (r.getCode() != 0) {
            log.debug("远程保存spu信息积分失败");
        }

        // TODO 优化代码
        //6、保存当前spu对应的所有sku信息
        //  6.1）、sku的基本信息；pms_sku_info
        List<Skus> skus = spuSaveVo.getSkus();

        if (skus != null && skus.size() > 0) {
            skus.forEach( item -> {
                String defaultImg = "";
                for (Images image : item.getImages()) {
                    if(image.getDefaultImg() == 1){
                        defaultImg = image.getImgUrl();
                    }
                }
                SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
                BeanUtils.copyProperties(item, skuInfoEntity);
                skuInfoEntity.setBrandId(infoEntity.getBrandId());

                skuInfoEntity.setCatalogId(infoEntity.getCatalogId());
                skuInfoEntity.setSaleCount(0L);
                skuInfoEntity.setSpuId(infoEntity.getId());
                skuInfoEntity.setSkuDefaultImg(defaultImg);
                skuInfoService.saveSkuInfo(skuInfoEntity);

                Long skuId = skuInfoEntity.getSkuId();

                //6.2）、sku的图片信息；pms_sku_image
                List<SkuImagesEntity> imagesEntityList = item.getImages().stream().map(img -> {
                    SkuImagesEntity skuImagesEntity = new SkuImagesEntity();

                    skuImagesEntity.setSkuId(skuId);
                    skuImagesEntity.setImgUrl(img.getImgUrl());
                    skuImagesEntity.setDefaultImg(img.getDefaultImg());

                    return skuImagesEntity;
                }).filter( imagesEntity -> {
                    //返回true就是需要，false就是剔除  过滤掉未勾选的图片
                    return !StringUtils.isEmpty(imagesEntity.getImgUrl());
                }).collect(Collectors.toList());

                skuImagesService.saveBatch(imagesEntityList);

                //6.3）、sku的销售属性信息：pms_sku_sale_attr_value
                List<Attr> attr = item.getAttr();
                List<SkuSaleAttrValueEntity> skuSaleAttrValueEntities = attr.stream().map(a -> {
                    SkuSaleAttrValueEntity attrValueEntity = new SkuSaleAttrValueEntity();
                    BeanUtils.copyProperties(a, attrValueEntity);
                    attrValueEntity.setSkuId(skuId);

                    return attrValueEntity;
                }).collect(Collectors.toList());
                skuSaleAttrValueService.saveBatch(skuSaleAttrValueEntities);

                //6.4）、sku的优惠、满减等信息；gomall_sms->sms_sku_ladder\sms_sku_full_reduction\sms_member_price
                SkuReductionTo skuReductionTo = new SkuReductionTo();
                BeanUtils.copyProperties(item,skuReductionTo);
                skuReductionTo.setSkuId(skuId);
                if(skuReductionTo.getFullCount() >0 || skuReductionTo.getFullPrice().compareTo(new BigDecimal("0")) == 1){
                    R r1 = couponFeignService.saveSkuReduction(skuReductionTo);
                    if(r1.getCode() != 0){
                        log.error("远程保存sku优惠信息失败");
                    }
                }
            });
        }

    }

    // TODO 代码优化
    /**
     * 1、保存商品spu基本信息  pms_spu_info
     * @param infoEntity
     */
    @Override
    public void saveBaseSpuInfo(SpuInfoEntity infoEntity) {
        this.baseMapper.insert(infoEntity);
        log.debug("本次保存商品的id：{}", infoEntity.getId());
        // 存储刚刚存入的商品id
        threadLocal.set(infoEntity.getId());


        log.debug("当前线程....{}---->{} ------- 保存商品spu基本信息",Thread.currentThread().getId(),Thread.currentThread().getName());
    }

    /**
     * 2、保存Spu的描述图片 pms_spu_info_desc
     * @param spuSaveVo
     */
    private void saveSpuInfoDesc(SpuSaveVo spuSaveVo) {
        List<String> decript = spuSaveVo.getDecript();
        SpuInfoDescEntity descEntity = new SpuInfoDescEntity();
        // 获取当前线程中存储的商品id
        descEntity.setSpuId(threadLocal.get());
        descEntity.setDecript(String.join(",",decript));

        spuInfoDescService.saveSpuInfoDesc(descEntity);

        log.debug("当前线程....{}---->{} ------- 保存商品Spu的描述图片信息",Thread.currentThread().getId(),Thread.currentThread().getName());

    }

    /**
     * 3、保存spu的图片集 pms_spu_images
     * @param spuSaveVo
     */
    private void saveSpuImages(SpuSaveVo spuSaveVo) {

        List<String> images = spuSaveVo.getImages();
        spuImagesService.saveImages(threadLocal.get(),images);

        log.debug("当前线程....{}---->{} ------- 保存商品spu的图片集信息",Thread.currentThread().getId(),Thread.currentThread().getName());

    }

    /**
     * 4、保存spu的规格参数;pms_product_attr_value
     * @param spuSaveVo
     */
    private void saveProductAttrValue(SpuSaveVo spuSaveVo) {
        List<BaseAttrs> baseAttrs = spuSaveVo.getBaseAttrs();

        List<ProductAttrValueEntity> collect = baseAttrs.stream().map(attr -> {
            ProductAttrValueEntity valueEntity = new ProductAttrValueEntity();
            valueEntity.setAttrId(attr.getAttrId());
            // 根据属性id 获取属性名
            AttrEntity id = attrService.getById(attr.getAttrId());

            valueEntity.setAttrName(id.getAttrName());
            valueEntity.setAttrValue(attr.getAttrValues());
            valueEntity.setQuickShow(attr.getShowDesc());
            valueEntity.setSpuId(threadLocal.get());

            return valueEntity;
        }).collect(Collectors.toList());
        // TODO 优化代码
        productAttrValueService.saveProductAttr(collect);

    }


}

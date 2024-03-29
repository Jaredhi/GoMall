package cn.jinterest.product.service.impl;

import cn.jinterest.common.utils.R;
import cn.jinterest.common.vo.SkuInfoVo;
import cn.jinterest.product.entity.SkuImagesEntity;
import cn.jinterest.product.entity.SpuInfoDescEntity;
import cn.jinterest.product.feign.SecKillFeignService;
import cn.jinterest.product.service.*;
import cn.jinterest.product.vo.SecKillInfoVo;
import cn.jinterest.product.vo.SkuItemSaleAttrVo;
import cn.jinterest.product.vo.SkuItemVo;
import cn.jinterest.product.vo.SpuItemAttrGroupVo;
import com.alibaba.fastjson.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.jinterest.common.utils.PageUtils;
import cn.jinterest.common.utils.Query;

import cn.jinterest.product.dao.SkuInfoDao;
import cn.jinterest.product.entity.SkuInfoEntity;
import org.springframework.util.StringUtils;


@Service("skuInfoService")
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {
    @Autowired
    private SkuImagesService skuImagesService;

    @Autowired
    private SpuInfoDescService spuInfoDescService;

    @Autowired
    private AttrGroupService attrGroupService;

    @Autowired
    private SkuSaleAttrValueService skuSaleAttrValueService;

    @Autowired
    private SecKillFeignService secKillFeignService;

    @Autowired
    private ThreadPoolExecutor executor;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                new QueryWrapper<SkuInfoEntity>()
        );

        return new PageUtils(page);
    }
    /**
     * sku的基本信息；pms_sku_info
     * @param skuInfoEntity
     */
    @Override
    public void saveSkuInfo(SkuInfoEntity skuInfoEntity) {
        this.baseMapper.insert(skuInfoEntity);
    }

    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {

        QueryWrapper<SkuInfoEntity> queryWrapper = new QueryWrapper<>();
        /**
         * key:
         * catelogId: 0
         * brandId: 0
         * min: 0
         * max: 0
         */
        String key = (String) params.get("key");
        if(!StringUtils.isEmpty(key)){
            queryWrapper.and((wrapper)->{
                wrapper.eq("sku_id",key).or().like("sku_name",key);
            });
        }

        String catelogId = (String) params.get("catelogId");
        if(!StringUtils.isEmpty(catelogId)&&!"0".equalsIgnoreCase(catelogId)){

            queryWrapper.eq("catalog_id",catelogId);
        }

        String brandId = (String) params.get("brandId");
        if(!StringUtils.isEmpty(brandId)&&!"0".equalsIgnoreCase(catelogId)){
            queryWrapper.eq("brand_id",brandId);
        }

        String min = (String) params.get("min");
        if(!StringUtils.isEmpty(min)){
            queryWrapper.ge("price",min);
        }

        String max = (String) params.get("max");

        if(!StringUtils.isEmpty(max)  ){
            try{
                BigDecimal bigDecimal = new BigDecimal(max);

                if(bigDecimal.compareTo(new BigDecimal("0"))==1){
                    queryWrapper.le("price",max);
                }
            }catch (Exception e){

            }

        }


        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    /**
     * 根据spuId查询所有sku信息
     * @param spuId
     * @return
     */
    @Override
    public List<SkuInfoEntity> getSkusBySpuId(Long spuId) {

        return this.list(new QueryWrapper<SkuInfoEntity>().eq("spu_id", spuId));
    }

    /**
     * 前台查询商品详情页信息 - 加入异步编排
     * @param skuId
     * @return
     */
    @Override
    public SkuItemVo item(Long skuId) throws ExecutionException, InterruptedException {

        SkuItemVo skuItemVo = new SkuItemVo();

        // supplyAsync 需要返回结果  因为 3 4 5 依赖1
        CompletableFuture<SkuInfoEntity> infoFuture = CompletableFuture.supplyAsync(() -> {
            // 1、获取sku基本信息 pms_sku_info
            SkuInfoEntity skuInfoEntity = getById(skuId);
            skuItemVo.setInfo(skuInfoEntity);
            return skuInfoEntity;
        }, executor);

        CompletableFuture<Void> saleFuture = infoFuture.thenAcceptAsync((res) -> {
            // 3、获取spu的销售属性组合
            if (res!=null){
                List<SkuItemSaleAttrVo> skuItemSaleAttrVos = skuSaleAttrValueService.getSaleAttrsBySpuId(res.getSpuId());
                skuItemVo.setSaleAttrs(skuItemSaleAttrVos);
            }
        }, executor);

        CompletableFuture<Void> descFuture = infoFuture.thenAcceptAsync((res) -> {
            // 4、获取spu的介绍 pms_spu_info_desc
            if (res!=null) {
                SpuInfoDescEntity spuInfoDescEntity = spuInfoDescService.getById(res.getSpuId());
                skuItemVo.setDesp(spuInfoDescEntity);
            }
        }, executor);

        CompletableFuture<Void> baseAttrFuture = infoFuture.thenAcceptAsync((res) -> {
            // 5、获取spu的规格参数信息
            if (res!=null) {
                List<SpuItemAttrGroupVo> attrGroupVos = attrGroupService.getAttrGroupWithAttrsBySpuId(res.getCatalogId(), res.getSpuId());
                skuItemVo.setAttrGroups(attrGroupVos);
            }
        }, executor);

        CompletableFuture<Void> imageFuture = CompletableFuture.runAsync(() -> {
            // 2、获取sku的图片信息 pms_spu_images
            List<SkuImagesEntity> skuImagesEntities = skuImagesService.getImageBySkuId(skuId);
            skuItemVo.setImages(skuImagesEntities);
        }, executor);

        // 6、查询当前sku是否参与秒杀优惠
        CompletableFuture<Void> secKillFuture = CompletableFuture.runAsync(() -> {
            R skuSecKillInfo = secKillFeignService.getSkuSecKillInfo(skuId);
            if (skuSecKillInfo.getCode() == 0) {
                SecKillInfoVo skuSecKillInfoData = skuSecKillInfo.getData(new TypeReference<SecKillInfoVo>() {
                });
                skuItemVo.setSecKillInfoVo(skuSecKillInfoData);
            }
        }, executor);


        // 等到所有任务都完成
        CompletableFuture.allOf(saleFuture, descFuture, baseAttrFuture, imageFuture, secKillFuture).get();

        return skuItemVo;
    }

    @Override
    public List<SkuInfoVo> getSkus() {

        return this.baseMapper.getSkus();
    }

}

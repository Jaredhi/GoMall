package cn.jinterest.product.service.impl;

import cn.jinterest.product.vo.SkuItemSaleAttrVo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.jinterest.common.utils.PageUtils;
import cn.jinterest.common.utils.Query;

import cn.jinterest.product.dao.SkuSaleAttrValueDao;
import cn.jinterest.product.entity.SkuSaleAttrValueEntity;
import cn.jinterest.product.service.SkuSaleAttrValueService;


@Service("skuSaleAttrValueService")
public class SkuSaleAttrValueServiceImpl extends ServiceImpl<SkuSaleAttrValueDao, SkuSaleAttrValueEntity> implements SkuSaleAttrValueService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuSaleAttrValueEntity> page = this.page(
                new Query<SkuSaleAttrValueEntity>().getPage(params),
                new QueryWrapper<SkuSaleAttrValueEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 获取spu的销售属性组合
     * @param spuId
     * @return
     */
    @Override
    public List<SkuItemSaleAttrVo> getSaleAttrsBySpuId(Long spuId) {

        List<SkuItemSaleAttrVo> skuItemSaleAttrVos = this.baseMapper.getSaleAttrsBySpuId(spuId);
        return skuItemSaleAttrVos;
    }

    /**
     * 根据skuId查询sku销售属性组合
     * @param skuId
     * @return
     */
    @Override
    public List<String> getSkuSaleAttrValuesAsStringList(Long skuId) {
        List<String> skuSaleAttrValuesAsStringList = this.baseMapper.getSkuSaleAttrValuesAsStringList(skuId);
        return skuSaleAttrValuesAsStringList;
    }

}

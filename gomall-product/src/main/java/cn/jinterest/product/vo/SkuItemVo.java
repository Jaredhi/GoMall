package cn.jinterest.product.vo;


import cn.jinterest.product.entity.SkuImagesEntity;
import cn.jinterest.product.entity.SkuInfoEntity;
import cn.jinterest.product.entity.SpuInfoDescEntity;
import lombok.Data;

import java.util.List;

@Data
public class SkuItemVo {

    SkuInfoEntity info; // sku基本信息

    boolean hasStock = true; // 是否有库存

    List<SkuImagesEntity> images; // sku图片信息

    List<SkuItemSaleAttrVo> saleAttrs;  // spu的销售属性组合信息

    SpuInfoDescEntity desp; // spu介绍信息

    List<SpuItemAttrGroupVo> attrGroups; // spu规格参数

    SecKillInfoVo secKillInfoVo; // 商品秒杀信息
}

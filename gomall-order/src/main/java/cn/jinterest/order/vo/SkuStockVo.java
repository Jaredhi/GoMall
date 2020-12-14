package cn.jinterest.order.vo;

import lombok.Data;

/**
 * 库存
 */
@Data
public class SkuStockVo {

    private Long skuId;
    private Boolean hasStock;
}

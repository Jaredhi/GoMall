package cn.jinterest.product.vo;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;


@Data
@ToString
public class SecKillInfoVo {

    /**
     * id
     */
    private Long id;
    /**
     * 活动id
     */
    private Long promotionId;
    /**
     * 活动场次id
     */
    private Long promotionSessionId;
    /**
     * 商品id
     */
    private Long skuId;

    /**
     * 商品秒杀的随机码
     */
    private String randomCode;

    /**
     * 秒杀价格
     */
    private BigDecimal seckillPrice;
    /**
     * 秒杀总量
     */
    private BigDecimal seckillCount;
    /**
     * 每人限购数量
     */
    private BigDecimal seckillLimit;
    /**
     * 排序
     */
    private Integer seckillSort;

    private Long startTime;

    private Long endTime;
}

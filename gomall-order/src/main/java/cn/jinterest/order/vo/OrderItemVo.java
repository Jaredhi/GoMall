package cn.jinterest.order.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Description 购买商品项
 **/
@Data
public class OrderItemVo {

    private Long skuId;

    private String title;

    private String image;

    private List<String> skuAttr;

    private BigDecimal price;

    private Integer count;

    private BigDecimal totalPrice;

}

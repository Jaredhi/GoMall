package cn.jinterest.order.vo;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * @Description 提交
 **/
@ToString
@Data
public class OrderSubmitVo {

    private Long addrId; // 地址id

    private Integer payType; // 付款类型

    private String orderToken; // 订单令牌

    private BigDecimal payPrice; // 验价

    // 用户相关信息在session中

    // 不需要提交购买的商品 去购物车在获取一遍 类似京东

    // TODO 优惠。。。
}

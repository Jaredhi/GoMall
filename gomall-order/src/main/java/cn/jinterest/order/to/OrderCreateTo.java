package cn.jinterest.order.to;


import cn.jinterest.order.entity.OrderEntity;
import cn.jinterest.order.entity.OrderItemEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;


@Data
public class OrderCreateTo {

    private OrderEntity orderEntity; //订单详细信息

    private List<OrderItemEntity> items; //每一个购物项详细信息

    private BigDecimal payPrice; // 订单计算的应付价格

    // TODO 暂未实现
    private BigDecimal fare; // 运费

}

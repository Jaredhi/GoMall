package cn.jinterest.order.vo;

import lombok.Data;
import lombok.ToString;

import java.util.List;


@Data
@ToString
public class WareSkuLockVo {

    private String orderSn;

    private List<OrderItemVo> locks; // 需要锁住的所有库存
}

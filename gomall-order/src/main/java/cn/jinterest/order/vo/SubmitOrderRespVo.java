package cn.jinterest.order.vo;


import cn.jinterest.order.entity.OrderEntity;
import lombok.Data;


@Data
public class SubmitOrderRespVo {

    private OrderEntity orderEntity;

    private Integer code; //状态码  0成功  1令牌验证失败

}

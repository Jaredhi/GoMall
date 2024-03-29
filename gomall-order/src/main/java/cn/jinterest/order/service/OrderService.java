package cn.jinterest.order.service;

import cn.jinterest.common.to.mq.SecKillOrderTo;
import cn.jinterest.order.vo.*;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.jinterest.common.utils.PageUtils;
import cn.jinterest.order.entity.OrderEntity;

import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * 订单
 *
 * @author JInterest
 * @email hwj2586@163.com
 * @date 2020-10-31 14:14:31
 */
public interface OrderService extends IService<OrderEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 获取订单确认页需要的数据
     * @return
     */
    OrderConfirmVo confirmOrder() throws ExecutionException, InterruptedException;

    /**
     * 下单
     * @param orderSubmitVo
     * @return
     */
    SubmitOrderRespVo submitOrder(OrderSubmitVo orderSubmitVo);

    /**
     * 根据订单号查询订单状信息
     * @param orderSn
     * @return
     */
    OrderEntity getOrderByOrderSn(String orderSn);

    /**
     * 关闭订单
     * @param entity
     */
    void closeOrder(OrderEntity entity);

    /**
     * 获取当前订单的支付信息
     * @param orderSn
     * @return
     */
    PayVo getOrderPayInfo(String orderSn);

    /**
     * 查询用户的订单信息
     * @param params
     * @return
     */
    PageUtils queryPageWhtiItem(Map<String, Object> params);


    /**
     * 支付成功修改订单信息
     * @param payAsyncVo 支付宝返回的信息
     * @return
     */
    String handlePayResult(PayAsyncVo payAsyncVo);


    /**
     * 创建秒杀商品订单
     * @param secKillOrderTo
     */
    void createSecKillOrder(SecKillOrderTo secKillOrderTo);
}


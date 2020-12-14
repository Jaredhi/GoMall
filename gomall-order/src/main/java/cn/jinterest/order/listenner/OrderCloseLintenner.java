package cn.jinterest.order.listenner;

import cn.jinterest.order.entity.OrderEntity;
import cn.jinterest.order.service.OrderService;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;


@RabbitListener(queues = {"order.release.order.queue"})
@Service
public class OrderCloseLintenner {

    @Autowired
    OrderService orderService;


    /**
     * 关闭订单
     * @param entity
     * @param channel
     * @param message
     * @throws IOException
     */
    @RabbitHandler
    public void listenner(OrderEntity entity, Channel channel, Message message) throws IOException {

        System.out.println("收到过期的订单信息，准备关闭订单：" + entity.getOrderSn());

        try {
            orderService.closeOrder(entity);
            //TODO 手动调用支付宝收单API,预防网络异常和服务器宕机
            // 查看支付宝文档

            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
        }

    }




}

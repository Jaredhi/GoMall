//package cn.jinterest.order.listenner;
//
//
//import cn.jinterest.order.service.OrderService;
//import com.rabbitmq.client.Channel;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.amqp.core.Message;
//import org.springframework.amqp.rabbit.annotation.RabbitHandler;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//
//
//@Slf4j
//@RabbitListener(queues = "order.seckill.order.queue")
//@Component
//public class OrderSecKillListenner {
//
//    @Autowired
//    OrderService orderService;
//
//    /**
//     * 监听队列，创建秒杀商品订单
//     * @param secKillOrderTo
//     * @param channel
//     * @param message
//     * @throws IOException
//     */
//    @RabbitHandler
//    public void listenner(SecKillOrderTo secKillOrderTo, Channel channel, Message message) throws IOException {
//
//
//        try {
//            log.info("收到秒杀的商品信息，订单号：{}",secKillOrderTo.getOrderSn());
//            orderService.createSecKillOrder(secKillOrderTo);
//
//            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
//        } catch (Exception e) {
//            channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
//        }
//
//    }
//
//}

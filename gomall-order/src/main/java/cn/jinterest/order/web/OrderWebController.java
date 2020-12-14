package cn.jinterest.order.web;

import cn.jinterest.order.service.OrderService;
import cn.jinterest.order.vo.OrderConfirmVo;
import cn.jinterest.order.vo.OrderSubmitVo;
import cn.jinterest.order.vo.SubmitOrderRespVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.concurrent.ExecutionException;


@Controller
public class OrderWebController {

    @Autowired
    OrderService orderService;

    /**
     * 跳转到结算页
     * @return
     */
    @GetMapping("/toTrade")
    public String toTrade(Model model, @ModelAttribute(name = "msg",binding = false)String msg) throws ExecutionException, InterruptedException {
        try {
            // 获取订单确认页需要的数据
            OrderConfirmVo orderConfirmVo = orderService.confirmOrder();
            model.addAttribute("msg",msg);
            model.addAttribute("orderConfirmData", orderConfirmVo);

            return "confirm";
        }catch (Exception e){
            e.printStackTrace();
            //购物车没有商品
            return "redirect:http://cart.gomall.com/cart.html";
        }

    }

    /**
     * 下单
     * @param orderSubmitVo
     * @return
     */
    @PostMapping("/submitOrder")
    public String submitOrder(OrderSubmitVo orderSubmitVo, Model model, RedirectAttributes redirectAttributes) {
        try {
            SubmitOrderRespVo submitOrderRespVo = orderService.submitOrder(orderSubmitVo);
            if (submitOrderRespVo.getCode() == 0) {
                model.addAttribute("submitOrderResp", submitOrderRespVo);
                return "pay";
            } else {
                String msg = "下单失败,";
                switch (submitOrderRespVo.getCode()) {
                    case 1:
                        msg += "订单信息过期，稍后再试";
                        break;
                    case 2:
                        msg += "订单商品价格发生变化";
                        break;
//                    case 3:
//                        msg += "商品库存不足";
//                        break;
                }
                redirectAttributes.addFlashAttribute("msg", msg);
                return "redirect:http://order.gomall.com/toTrade";
            }
        }catch (Exception e){
            e.printStackTrace();
            String msg = "下单失败,商品库存不足";
            redirectAttributes.addFlashAttribute("msg", msg);
            return "redirect:http://order.gomall.com/toTrade";
        }

    }
}


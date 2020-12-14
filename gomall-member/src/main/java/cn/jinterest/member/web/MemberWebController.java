package cn.jinterest.member.web;

import cn.jinterest.common.utils.R;
import cn.jinterest.member.feign.OrderFeignService;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;


@Controller
public class MemberWebController {

    @Autowired
    OrderFeignService orderFeignService;

    @GetMapping("/memberOrder.html")
    public String memberOrderPage(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum, Model model) {


        HashMap<String, Object> map = new HashMap<>();
        map.put("page", pageNum.toString());
        R r = orderFeignService.listWithItem(map);
        System.out.println(JSON.toJSONString(r));
        model.addAttribute("orders", r);

        return "orderList";
    }
}

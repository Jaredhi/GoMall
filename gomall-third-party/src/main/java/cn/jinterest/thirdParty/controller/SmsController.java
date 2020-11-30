package cn.jinterest.thirdParty.controller;


import cn.jinterest.common.utils.R;
import cn.jinterest.thirdParty.component.SmsComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description TODO
 * @Author 鲁班爱喝旺仔
 * @Date 2020/4/21 04:50
 * @Version 1.0
 **/
@RestController
@RequestMapping("/sms")
public class SmsController {

    @Autowired
    SmsComponent smsComponent;
    @Value("${aliyun.sms.templateCode}")
    String templateCode;

    /**
     * 阿里云发送手机架验证码 - 提供给别的服务调用
     * @param phone
     * @param code
     * @return
     */
    @GetMapping("/sendcode")
    R sendCode(@RequestParam("phone") String phone, @RequestParam String code){
        Map<String, Object> param = new HashMap<>();
        param.put("code", code);
        boolean b = smsComponent.sendCode(phone, templateCode, param);
        if (b){
            return R.ok();
        }
        return R.error(10004,"发送短信失败");
    }
}

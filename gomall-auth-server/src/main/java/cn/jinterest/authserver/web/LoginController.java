package cn.jinterest.authserver.web;

import cn.jinterest.authserver.feign.MemberFeignService;
import cn.jinterest.authserver.feign.ThirdPartyFeignService;
import cn.jinterest.authserver.vo.UserLoginVo;
import cn.jinterest.authserver.vo.UserRegistVo;
import cn.jinterest.common.constant.ums.AuthServerConstant;
import cn.jinterest.common.exception.BizCodeEnume;
import cn.jinterest.common.utils.R;
import cn.jinterest.common.utils.RandomUtil;
import cn.jinterest.common.vo.MemberRespVo;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Description 登录
 * @Author ajun
 * @Date 2020/11/29 16:00
 * @Version 1.0
 **/
@Slf4j
@Controller
public class LoginController {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    ThirdPartyFeignService thirdPartyFeignService;

    @Autowired
    MemberFeignService memberFeignService;



    /**
     * 清除redis中的session
     * @param
     * @return
     */
    @RequestMapping("/removesession")
    public String removeSession(HttpSession session) {
        session.invalidate();

        return "redirect:http://gomall.com/";
    }

    /**
     * 发送验证码 - 调用第三方服务向阿里云发送验证码
     *
     * @param phone
     * @return
     */
    @GetMapping("/sms/sendcode")
    @ResponseBody
    public R sendCode(@RequestParam("phone") String phone) {

        String redisCode = stringRedisTemplate.opsForValue().get(AuthServerConstant.SMS_CODE_CACHE_PREFIX + phone);
        if (!StringUtils.isEmpty(redisCode)) {
            // 获取redis这个key存储的时间
            String[] str = redisCode.split("_");
            long createCacheTime = Long.parseLong(str[1]);
            // 60秒内,提示短信发送频繁
            if (System.currentTimeMillis() - createCacheTime < 60000) { // 由于存储的时候的毫秒 需要*1000
                return R.error(BizCodeEnume.VAILD_SMS_CODE_EXCEPTION.getCode(), BizCodeEnume.VAILD_SMS_CODE_EXCEPTION.getMsg());
            }
        }
        //生成随机值，传递阿里云进行发送
        String code = RandomUtil.getFourBitRandom();

        R r = thirdPartyFeignService.sendCode(phone,code);
        if (r.getCode() == 0) {
            // 发送成功，手机号号和验证码存入redis  5分钟过期
            String redisStr = code + "_" + System.currentTimeMillis();
            stringRedisTemplate.opsForValue().set(AuthServerConstant.SMS_CODE_CACHE_PREFIX + phone, redisStr, 5, TimeUnit.MINUTES);
            return R.ok();
        } else {
            return R.error(10004, "发送短信失败");
        }

    }

    /**
     * 用户注册
     * RedirectAttributes 模拟重定向携带数据
     * 原理:利用session，将数据存放在session中，重要跳到下一个页面取出这个数据后，session里面的数据就会删掉
     * 1、需要解决分布式session问题
     *
     * @param registVo
     * @param result
     * @param redirectAttributes
     * @return
     */
    @PostMapping("/regist")
    public String regist(@Valid UserRegistVo registVo, BindingResult result,
                         RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) { // 校验错误 收集错误信息
            System.out.println();
            Map<String, String> errors = result.getFieldErrors().stream().collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
            redirectAttributes.addFlashAttribute("errors", errors);
            return "redirect:http://auth.gomall.com/reg.html";
        }
        // 校验验证码
        String code = registVo.getCode();
        String str = stringRedisTemplate.opsForValue().get(AuthServerConstant.SMS_CODE_CACHE_PREFIX + registVo.getPhone());
        if (!StringUtils.isEmpty(str)) { // 判断验证是否过期
            String redisCode = str.split("_")[0];
            if (code.equalsIgnoreCase(redisCode)) { // 验证码正确
                // 删掉redis中存储的验证码；令牌机制
                stringRedisTemplate.delete(AuthServerConstant.SMS_CODE_CACHE_PREFIX + registVo.getPhone());
                // 调用远程服务注册
                R r = memberFeignService.regist(registVo);
                if (r.getCode() == 0) { // 成功
                    return "redirect:http://auth.gomall.com/login.html";

                } else { // 注册失败
                    Map<String, String> errors = new HashMap<>();
                    errors.put("msg", r.getData("msg", new TypeReference<String>() {
                    }));
                    redirectAttributes.addFlashAttribute("errors", errors);
                    return "redirect:http://auth.gomall.com/reg.html";
                }
            } else {
                Map<String, String> errors = new HashMap<>();
                errors.put("code", "验证码错误");
                redirectAttributes.addFlashAttribute("errors", errors);
                return "redirect:http://auth.gomall.com/reg.html";
            }
        } else {
            Map<String, String> errors = new HashMap<>();
            errors.put("code", "验证码已过期");
            redirectAttributes.addFlashAttribute("errors", errors);
            return "redirect:http://auth.gomall.com/reg.html";
        }

    }


    /**
     * 会员登录
     *
     * @param vo
     * @return
     */
    @PostMapping("/login")
    public String login(UserLoginVo vo, RedirectAttributes redirectAttributes,
                        HttpSession session) {

        R r = memberFeignService.login(vo);
        if (r.getCode() == 0) {
            MemberRespVo memberRespVo = r.getData("data", new TypeReference<MemberRespVo>() {
            });
            log.info("普通用户登录成功，用户信息：{}", memberRespVo);
            session.setAttribute(AuthServerConstant.LOGIN_USER, memberRespVo);
            return "redirect:http://gomall.com";
        } else {
            HashMap<String, String> errors = new HashMap<>();
            errors.put("msg", r.getData("msg", new TypeReference<String>() {
            }));
            redirectAttributes.addFlashAttribute("errors", errors);
            return "redirect:http://auth.gomall.com/login.html";
        }
    }


    @GetMapping("/login.html")
    public String loginPage(HttpSession session) {

        Object loginUser = session.getAttribute(AuthServerConstant.LOGIN_USER);
        if (loginUser == null) {
            return "login";
        } else {
            return "redirect:http://gomall.com";
        }

    }


}

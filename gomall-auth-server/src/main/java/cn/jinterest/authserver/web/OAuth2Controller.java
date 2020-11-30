package cn.jinterest.authserver.web;

import cn.jinterest.authserver.feign.MemberFeignService;
import cn.jinterest.authserver.vo.SocialUserVo;
import cn.jinterest.common.constant.ums.AuthServerConstant;
import cn.jinterest.common.utils.HttpUtils;
import cn.jinterest.common.utils.R;
import cn.jinterest.common.vo.MemberRespVo;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;


/**
 * @Description 处理社交登录
 * @Author ajun
 * @Date 2020/11/29 16:26
 * @Version 1.0
 **/
@Slf4j
@Controller
public class OAuth2Controller {

    @Autowired
    private MemberFeignService memberFeignService;


    /**
     * 根据code换取accessToken - 调用远程gomall-member服务进行社交登录或注册
     * @param code
     * @return
     * @throws Exception
     */
    @GetMapping("/oauth2.0/weibo/success")
    public String weibo(@RequestParam("code") String code, HttpSession session, HttpServletResponse servletResponse) throws Exception {

        // 根据code换取accessToken
        Map<String, String> map = new HashMap<>();
        map.put("client_id", "613213750");
        map.put("client_secret", "027d77a7c908d98ebd6296e4c982b51b");
        map.put("grant_type", "authorization_code");
        map.put("redirect_uri", "http://auth.gomall.com/oauth2.0/weibo/success");
        map.put("code", code);
        HttpResponse response = HttpUtils.doPost("https://api.weibo.com", "/oauth2/access_token", "post", new HashMap<String, String>(), null, map);

        // 2、处理响应数据
        if (response.getStatusLine().getStatusCode() == 200) {
            // 获取到了accessToken
            String json = EntityUtils.toString(response.getEntity()); // 将响应的内容转换成json字符串
            SocialUserVo socialUserVo = JSON.parseObject(json, SocialUserVo.class); // 将获取到的json转换成SocialUserVo对象

            // 1、会员第一次登陆，自动注册（为当前社交用户生成一个会员信息账号，以后这个社交账号就对应指定的会员信息）
            // 调用远程服务 gomall-member 登陆或注册操作
            R r = memberFeignService.oauthLogin(socialUserVo);
            if (r.getCode() == 0) {
                MemberRespVo memberRespVo = r.getData("data", new TypeReference<MemberRespVo>() {
                });
                log.info("微博用户登录成功，用户信息：{}", memberRespVo);
                session.setAttribute(AuthServerConstant.LOGIN_USER, memberRespVo);

                return "redirect:http://gomall.com";
            } else {
                return "redirect:http://auth.gomall.com/login.html";
            }

        } else { // 远程请求社交服务获取数据失败
            return "redirect:http://auth.gomall.com/login.html";
        }



    }
}

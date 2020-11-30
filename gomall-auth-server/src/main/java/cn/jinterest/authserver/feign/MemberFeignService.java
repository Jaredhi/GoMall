package cn.jinterest.authserver.feign;


import cn.jinterest.authserver.vo.SocialUserVo;
import cn.jinterest.authserver.vo.UserLoginVo;
import cn.jinterest.authserver.vo.UserRegistVo;
import cn.jinterest.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("gomall-member")
public interface MemberFeignService {

    @RequestMapping("/member/member/regist")
    public R regist(@RequestBody UserRegistVo userRegistVo);

    @PostMapping("/member/member/login")
    public R login(@RequestBody UserLoginVo userLoginVo);

    @PostMapping("/member/member/oauth/login")
    public R oauthLogin(@RequestBody SocialUserVo socialUserVo) throws Exception;
}

package cn.jinterest.member.feign;


import cn.jinterest.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 1、@FeignClient("gomall-coupon") 告诉springcloud 这个接口是一个远程客户端
 */
@FeignClient("gomall-coupon")
@Component
public interface CouponFeignService {

    @RequestMapping("/coupon/coupon/member/list")
    R memberCoupons();
}

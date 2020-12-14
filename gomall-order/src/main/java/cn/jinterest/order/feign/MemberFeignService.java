package cn.jinterest.order.feign;

import cn.jinterest.order.vo.MemberAddressVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;


@FeignClient("gomall-member")
public interface MemberFeignService {

    /**
     * 根据会员id查询收货地址
     * @param memberId
     * @return
     */
    @GetMapping("/member/memberreceiveaddress/{memberId}/addresses")
    List<MemberAddressVo> getAddresses(@PathVariable("memberId") Long memberId);

}

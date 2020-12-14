package cn.jinterest.member.feign;


import cn.jinterest.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient("gomall-order")
@Component
public interface OrderFeignService {


    @PostMapping("/order/order/listWithItem")
    R listWithItem(@RequestBody Map<String, Object> params);
}

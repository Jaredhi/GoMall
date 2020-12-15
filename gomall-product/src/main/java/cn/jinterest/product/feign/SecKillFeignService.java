package cn.jinterest.product.feign;

import cn.jinterest.common.utils.R;
import cn.jinterest.product.feign.fallback.SecKillFeignServiceFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(value = "gomall-seckill", fallback = SecKillFeignServiceFallBack.class)
@Component
public interface SecKillFeignService {

    /**
     * 查询某个sku的秒杀信息
     * @param skuId
     * @return
     */
    @GetMapping("/sku/seckill/{skuId}")
    R getSkuSecKillInfo(@PathVariable("skuId") Long skuId);
}

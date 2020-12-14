package cn.jinterest.order.feign;


import cn.jinterest.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("gomall-product")
public interface ProductFeignService {

    @GetMapping("/product/spuinfo/getSpuInfoBySkuId/{skuId}")
    R getSpuInfoBySkuId(@PathVariable("skuId") Long skuId);
}

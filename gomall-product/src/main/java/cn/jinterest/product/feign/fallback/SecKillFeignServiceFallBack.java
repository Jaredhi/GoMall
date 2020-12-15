package cn.jinterest.product.feign.fallback;

import cn.jinterest.common.exception.BizCodeEnume;
import cn.jinterest.common.utils.R;
import cn.jinterest.product.feign.SecKillFeignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class SecKillFeignServiceFallBack implements SecKillFeignService {

    @Override
    public R getSkuSecKillInfo(Long skuId) {
        log.info("熔断方法调用。。。getSkuSecKillInfo");
        return R.error(BizCodeEnume.TO_MANY_REQUEST.getCode(), BizCodeEnume.TO_MANY_REQUEST.getMsg());
    }
}

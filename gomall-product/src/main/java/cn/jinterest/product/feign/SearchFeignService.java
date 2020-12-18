package cn.jinterest.product.feign;


import cn.jinterest.common.to.es.SkuEsModel;
import cn.jinterest.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


@FeignClient("gomall-search")
public interface SearchFeignService {

    @PostMapping("/search/save/product")
    R productStstusUp(@RequestBody List<SkuEsModel> esModels);

    @PostMapping("/search/delete/product")
    R productStstusDown(@RequestBody List<Long> skuIds);
}

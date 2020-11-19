package cn.jinterest.search.controller;

import cn.jinterest.common.exception.BizCodeEnume;
import cn.jinterest.common.to.es.SkuEsModel;
import cn.jinterest.common.utils.R;
import cn.jinterest.search.service.ProductSaveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Auther: AJun
 * @version:1.0
 * @Date: 2020/11/19  17:09
 * @Description:
 */
@Slf4j
@RequestMapping("/search/save")
@RestController
public class ElasticSaveController {

    @Autowired
    ProductSaveService productSaveService;

    @PostMapping("/product")
    public R productStstusUp(@RequestBody List<SkuEsModel> esModels) {
        Boolean flag = false;
        try {
            flag = productSaveService.productStatusUp(esModels);
        } catch (Exception e) {
            log.error("ElasticSaveController商品上架产生了错误：{}",e);
            return R.error(BizCodeEnume.PRODUCT_UP_TO_ES_EXCETION.getCode(), BizCodeEnume.PRODUCT_UP_TO_ES_EXCETION.getMsg());
        }
        if (!flag) {
            return R.ok();
        } else {
            return R.error(BizCodeEnume.PRODUCT_UP_TO_ES_EXCETION.getCode(), BizCodeEnume.PRODUCT_UP_TO_ES_EXCETION.getMsg());
        }
    }
}

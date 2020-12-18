package cn.jinterest.search.service;

import cn.jinterest.common.to.es.SkuEsModel;

import java.io.IOException;
import java.util.List;

/**
 * @Auther: AJun
 * @version:1.0
 * @Date: 2020/11/19  17:10
 * @Description:
 */
public interface ProductSaveService {

    /**
     * 上传到es
     * @param esModels
     * @return true 有错误 false 无错误
     * @throws IOException
     */
    Boolean productStatusUp(List<SkuEsModel> esModels) throws IOException;
    /**
     * 从es 删除索引
     * @param
     * @param skuIds
     * @return true 有错误 false 无错误
     * @throws IOException
     */
    Boolean productStatusDown(List<Long> skuIds);
}


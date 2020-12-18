package cn.jinterest.search.service.impl;

import cn.jinterest.common.to.es.SkuEsModel;
import cn.jinterest.search.config.GomallElasticSearchConfig;
import cn.jinterest.search.constant.EsConstant;
import cn.jinterest.search.service.ProductSaveService;
import com.alibaba.fastjson.JSON;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductSaveServiceImpl implements ProductSaveService {

    @Autowired
    RestHighLevelClient restHighLevelClient;

    /**
     * 上传到es
     * @param esModels
     * @return true 有错误 false 无错误
     * @throws IOException
     */
    @Override
    public Boolean productStatusUp(List<SkuEsModel> esModels) throws IOException {

        BulkRequest bulkRequest = new BulkRequest();

        for (SkuEsModel esModel : esModels) {
            IndexRequest indexRequest = new IndexRequest(EsConstant.PRODUCT_INDEX);
            indexRequest.id(esModel.getSkuId().toString());
            String json = JSON.toJSONString(esModel);
            indexRequest.source(json, XContentType.JSON);
            bulkRequest.add(indexRequest);
        }
        //批量添加操作
        BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, GomallElasticSearchConfig.COMMON_OPTIONS);

        // TODO 处理产生的错误
        boolean hasFaile = bulk.hasFailures();
        List<String> collect = Arrays.stream(bulk.getItems()).map(item -> {
            return item.getId();
        }).collect(Collectors.toList());
        log.info("ProductSaveServiceImpl.productStstusUp 上架成功商品：{} ,返回的数据：{}",collect,bulk.toString());

        return hasFaile;

    }

    @Override
    public Boolean productStatusDown(List<Long> skuIds) {

        DeleteResponse delete = null;

        DeleteRequest deleteRequest = new DeleteRequest(EsConstant.PRODUCT_INDEX,"_doc");
        for (Long skuId:skuIds
             ) {
            deleteRequest.id(skuId.toString());

            try {
                delete = restHighLevelClient.delete(deleteRequest, GomallElasticSearchConfig.COMMON_OPTIONS);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (delete!= null && delete.status().getStatus()!=200){
            log.info("ProductSaveServiceImpl.productStstusDown 下架失败：{} ，失败状态码：{}", skuIds,delete.status().getStatus());
            return false;
        }else {
            return true;
        }
    }
}

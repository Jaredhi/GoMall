package cn.jinterest.search;


import cn.jinterest.search.config.GomallElasticSearchConfig;
import cn.jinterest.search.constant.EsConstant;
import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class GomallSearchApplicationTests {

    RestHighLevelClient restHighLevelClient =  new RestHighLevelClient(RestClient.builder(
            new HttpHost("192.168.108.131", 9200, "http")));

    @Test
    public void contextLoads() {
        List<Long> ids = new ArrayList<>();
        ids.add(17L);
        ids.add(18L);
        ids.add(19L);
        DeleteRequest deleteRequest = new DeleteRequest(EsConstant.PRODUCT_INDEX,"_doc");
        for (Long id:ids
             ) {
            deleteRequest.id(id.toString());
        }

        try {
            restHighLevelClient.delete(deleteRequest, GomallElasticSearchConfig.COMMON_OPTIONS);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

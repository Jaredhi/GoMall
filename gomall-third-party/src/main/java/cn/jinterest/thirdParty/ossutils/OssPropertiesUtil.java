package cn.jinterest.thirdParty.ossutils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Auther: AJun
 * @version:1.0
 * @Date: 2020/11/04  19:23
 * @Description:读取阿里云access
 */
@Component
public class OssPropertiesUtil implements InitializingBean {
    //读取配置文件内容
    @Value("${spring.cloud.alicloud.oss.endpoint}")
    private String endpoint;
    @Value("${spring.cloud.alicloud.access-key}")
    private String keyId;
    @Value("${spring.cloud.alicloud.secret-key}")
    private String keySecret;
    @Value("${spring.cloud.alicloud.oss.bucket}")
    private String bucketName;


    //定义公开静态常量
    public static String END_POIND;
    public static String ACCESS_KEY_ID;
    public static String ACCESS_KEY_SECRET;
    public static String BUCKET_NAME;

    @Override
    public void afterPropertiesSet() throws Exception {
        END_POIND = endpoint;
        ACCESS_KEY_ID = keyId;
        ACCESS_KEY_SECRET = keySecret;
        BUCKET_NAME = bucketName;
    }
}

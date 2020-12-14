package cn.jinterest.order.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@ConfigurationProperties(prefix = "gomall.thread")
@Component
@Data
public class ThreadPoolProperties {
    private Integer coreSize;
    private Integer maxSize;
    private Integer keepAliveTime;
}

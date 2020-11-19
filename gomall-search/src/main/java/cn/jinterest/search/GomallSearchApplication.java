package cn.jinterest.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;



@EnableDiscoveryClient
@EnableFeignClients(basePackages = "cn.jinterest.search.feign")
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class GomallSearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(GomallSearchApplication.class, args);
    }

}

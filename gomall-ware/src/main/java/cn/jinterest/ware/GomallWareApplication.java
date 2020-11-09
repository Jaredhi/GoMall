package cn.jinterest.ware;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients("cn.jinterest.ware.feign")
@EnableDiscoveryClient
@SpringBootApplication
public class GomallWareApplication {

    public static void main(String[] args) {
        SpringApplication.run(GomallWareApplication.class, args);
    }

}

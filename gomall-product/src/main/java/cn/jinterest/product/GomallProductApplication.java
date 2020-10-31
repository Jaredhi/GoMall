package cn.jinterest.product;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
@MapperScan("cn.jinterest.product.dao")
public class GomallProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(GomallProductApplication.class, args);
    }

}

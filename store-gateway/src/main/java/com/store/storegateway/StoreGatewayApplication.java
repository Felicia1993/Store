package com.store.storegateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 1.开启服务注册发现
 * 2.配置nacos注册中心地址
 */
@EnableDiscoveryClient
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class StoreGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(StoreGatewayApplication.class, args);
    }

}

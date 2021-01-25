package com.store.storeware;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@MapperScan("com.store.storeware.dao")
@SpringBootApplication
@EnableDiscoveryClient
public class StoreWareApplication {
    public static void main(String[] args) {
        SpringApplication.run(StoreWareApplication.class,args);
    }
}

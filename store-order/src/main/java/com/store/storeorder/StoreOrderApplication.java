package com.store.storeorder;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.store.storeorder.dao")
public class StoreOrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(StoreOrderApplication.class,args);
    }
}

package com.store.storeware;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.mybatis.spring.annotation.MapperScan;

@MapperScan("com.store.storeware.dao")
@SpringBootApplication
public class StoreWareApplication {
    public static void main(String[] args) {
        SpringApplication.run(StoreWareApplication.class,args);
    }
}

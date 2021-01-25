package com.store.storecart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;

@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
@FeignClient
public class StoreCartApplication {

    public static void main(String[] args) {
        SpringApplication.run(StoreCartApplication.class, args);
    }

}

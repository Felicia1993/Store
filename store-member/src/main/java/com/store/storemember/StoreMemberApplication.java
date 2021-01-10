package com.store.storemember;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 远程调用coupon的步骤
 * 1.引入openFeign
 * 2.编写一个接口，告诉SpringCloud这个接口需要调用远程服务
 *  1.声明接口的每一个方法都是调用哪个远程服务的哪个请求
 * 3.开启远程调用的功能
 */
@MapperScan("com.store.storemember.dao")
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.store.storemember.feign")
public class StoreMemberApplication {
    public static void main(String[] args) {
        SpringApplication.run(StoreMemberApplication.class,args);
    }
}

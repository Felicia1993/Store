package com.store.storeproduct;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 整合mybatis-plus
 * 1.导入依赖
 * 2.配置
 *  配置数据源
 *      1、导入数据库驱动
 *      2.在application.yml配置数据源相关信息
 *
 *  2.配置mybatis-plus相关信息
 *      1.使用mapperscan
 *      2.告诉哦mybatis-plus，sql映射文件
 * 3。
 *
 * 整合redis
 * 1.引入data-redis-starter
 * 2.简单配置redis的host等信息
 * 3.使用springboot自动配置好的StringRedisTemplate来操作redis
 */

@MapperScan("com.store.storeproduct.dao")
@EnableDiscoveryClient
@SpringBootApplication
public class StoreProductApplication {
    public static void main(String[] args) {
        SpringApplication.run(StoreProductApplication.class,args);
    }
}

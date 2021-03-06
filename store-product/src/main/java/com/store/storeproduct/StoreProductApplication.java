package com.store.storeproduct;

import org.mybatis.spring.annotation.MapperScan;
import org.redisson.spring.session.config.EnableRedissonHttpSession;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

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
 *
 *
 * 整合redission
 * 1.引入依赖
 * <dependency>
 *             <groupId>org.redisson</groupId>
 *             <artifactId>redisson</artifactId>
 *             <version>3.12.0</version>
 *         </dependency>
 * 2.配置redission
 *
 *
 */

@MapperScan("com.store.storeproduct.dao")
@EnableDiscoveryClient
@SpringBootApplication
@EnableRedisHttpSession
public class StoreProductApplication {
    public static void main(String[] args) {
        SpringApplication.run(StoreProductApplication.class,args);
    }
}

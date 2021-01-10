package com.store.storecoupon;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
/**
 * 如何使用nacos作为统一的配置中心
 * 1.引入依赖
 * spring-cloud-starter-alibaba-nacos-config
 * 2.创建一个bootstrap.properties
 * 配置spring.application.name=store-coupon
 * spring.cloud.nacos.config.server-addr=127.0.0.1:8848
 * 3.在配置中心默认添加一个叫数据集(Data Id) store-coupon.properties 默认规则，应用名properties
 * 4.给应用名.properties添加任何配置
 * 5.动态获取配置
 * @RefreshScope:动态获取并刷新配置
 * @Value("${配置项的名}")：获取到配置
 * 如果配置中心和当前应用的配置文件中，都配置了相同的项，有限使用配置中心的配置
 */
@MapperScan("com.store.storecoupon.dao")
@SpringBootApplication
@EnableDiscoveryClient
public class StoreCouponApplication {
    public static void main(String[] args) {
        SpringApplication.run(StoreCouponApplication.class,args);
    }

}

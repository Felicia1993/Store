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
 * 2.细节
 *  1.命名空间 环境隔离
 *      默认public（保留空间），默认新增的所有配置都在public空间
 *      1.开发、测试、生产：利用命名空间做隔离
 *      注意：在bootstrap.properties里配置上，需要使用的那个命名空间下的配置
 *      2.基于每个微服务之间，互相隔离配置，每个微服务都创建自己的命名空间，只加载自己命名空间下的所有配置
 *  2.配置集 一组相关或不相关的配置集合
 *  3.配置集ID 类似文件名ID
 *  Data ID：
 *  4.配置分组
 *  默认所有的配置集都属于default_group：
 *  1111、618、1212
 *  每个微服务创建自己的命名空间，使用配置分组区分环境，dev test prod
 *  3.同时加载多个配置集
 *  
 */
@MapperScan("com.store.storecoupon.dao")
@SpringBootApplication
@EnableDiscoveryClient
public class StoreCouponApplication {
    public static void main(String[] args) {
        SpringApplication.run(StoreCouponApplication.class,args);
    }

}

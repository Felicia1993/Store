package com.store.storeproduct;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
 */

@MapperScan("com.store.storeproduct.dao")
@SpringBootApplication
public class StoreProductApplication {
    public static void main(String[] args) {
        SpringApplication.run(StoreProductApplication.class,args);
    }
}

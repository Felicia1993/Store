package com.store.storeauthserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * springSession核心原理
 * 1）导入@Import({RedisHttpSessionConfiguration.class})
 *  1.给容器中添加了一个组件
 *      SessionRepository==>RedisOperationsSessionRepository：redis操作session，session的增删改查封装类
 *  2.SessionRepositoryFilter==>Filter:session存储过滤器：每个请求过滤必须经过filter
 *      1.创建的时候，自动从容器汇中获取到了sessionRepository
 *      2.原始的request、response都被包装。SessionRepositoryRequestWrapper
 *      3.以后获取session request.getSession()
 *      4.wrappedRequest.getSession();
 *
 */

@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
@EnableRedisHttpSession
public class StoreAuthServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(StoreAuthServerApplication.class, args);
    }

}

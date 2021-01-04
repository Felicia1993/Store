package com.store.order.controller;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 1.引入了amqp场景，RabbitAutoConfiguration
 * 2.给容器中自动配置
 *  RabbitTemplate
 *  所有的属性都是在@ConfigurationProperties(prefix = "spring.rabbitmq")中配置
 *  3.@EnableRabbit:
 */
@EnableRabbit
@SpringBootApplication
public class StoreOrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(StoreOrderApplication.class,args);
    }
}

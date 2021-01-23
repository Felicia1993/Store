package com.store.storeorder;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 使用RabbitMq
 * 1.引入amqp场景，RabbitAutoConfiguration就会自动生效
 * 2.给容器中自动配置了
 *  Rabbittemplate、AmqpAdmin,CachingConnectionFactory RabbitMessagingTemplate
 * 所有属性都是Spring.rabbitmq
 * @ConfigurationProperties(prefix="spring.rabbitmq")
 * public class RabbitProperties
 *
 * 3.给配置文件中配置spring.rabbitmq信息
 * 4.@EnableRabbit: @EnableXXXX开启功能
 * 5.监听消息RabbitListener
 *
 * Queue可以很多人监听，只要收到消息，队列就会删除消息，而且之鞥呢有一个收到此消息
 * 场景：
 *  1）订单服务启动多个,同一个消息只能有一个客户端收到
 *  2）只有一个消息一个消息完全处理完，方法运行结束，我们就可以接受下一个消息
 *
 * @RabbitListener：类+方法上 监听哪些队列
 * @RabbitHandler：标在方法上 重载区分不同消息
 */
@EnableRabbit
@SpringBootApplication
@MapperScan("com.store.storeorder.dao")
public class StoreOrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(StoreOrderApplication.class,args);
    }
}

package com.store.storeorder;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

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
 *
 * seata控制分布式事务
 * 1）每一个微服务创建undo_log表
 * 2）安装事务协调器：seata-server:http://github.com/seata/seata/release
 *  整合：
 *      1.导入依赖spring-cloud-alibaba-seataSeaTac seata-all:0.7.1
 *      2.解压并启动seata服务器
 *          registry.conf：注册中心配置：修改registry type=nacos
 *          file.conf
 *      3.所有想要用到分布式事务的微服务，都要使用seata DataSourceProxy代理自己的数据源
 *      properties.initializeDataSourceBuilder().type(type).build();
 *      4.每个微服务，都必须导入 register.conf
 *      file.conf
 *      5.启动测试分布式事务
 *      6.给分布式大事务的入口标注@GlobalTransactional全局事务，每个远程小事务用@Transaction
 */
@EnableAspectJAutoProxy(exposeProxy = true)
@EnableRabbit
@SpringBootApplication
@MapperScan("com.store.storeorder.dao")
@EnableRedisHttpSession
@EnableFeignClients
public class StoreOrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(StoreOrderApplication.class,args);
    }
}

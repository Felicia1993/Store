package com.store.order;

import com.store.order.controller.StoreOrderApplication;
import javafx.application.Application;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = StoreOrderApplicationTests.class)
public class StoreOrderApplicationTests {
    /**
     * 1.如何创建Exchange[hello-java-exchange] queue binding
     *  使用AmqpAdmin
     *  2.如何收发消息
     */
    @Autowired
    AmqpAdmin amqpAdmin;
    Logger logger = LoggerFactory.getLogger(StoreOrderApplicationTests.class);


    @Test
    public void createExchange() {
        //amqpAdmin
        //Exchange DirectExchange(String name, boolean durable, boolean autoDelete)
        DirectExchange directExchange = new DirectExchange("hello.java.exchange", true, false);
        amqpAdmin.declareExchange(directExchange);
        logger.info("Exchange[{}]创建成功", "hello-java-exchange");
    }
    @Test
    public void createQueue() {
        //Queue(String name, boolean durable, boolean exclusive, boolean autoDelete, @Nullable Map<String, Object> arguments)
        Queue queue = new Queue("hello-java-queue", true, false, false);
        amqpAdmin.declareQueue(queue);
        logger.info("Queue[{}]创建成功", "hello-java-exchange");
    }
    @Test
    public void createBinding() {
        //Binding(String destination[目的地],
        // Binding.DestinationType destinationType【目的地类型】,
        // String exchange【交换机】,
        // String routingKey【路由件】,
        // @Nullable Map<String, Object> arguments) {
        //将exchange指定的交换机和destination目的地进行绑定，使用routineey作为指定的路由件
        Binding binding = new Binding("hello-java-queue",
                Binding.DestinationType.QUEUE,
                "hello-java-exchange",
                "hello.java", null);
        amqpAdmin.declareBinding(binding);
        logger.info("Queue[{}]绑定成功", "hello-java-exchange");
    }
}

package com.store.storeorder.config;

import com.rabbitmq.client.Channel;
import com.store.storeorder.entity.OrderEntity;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.HashMap;

@Configuration
public class MyMQConfig {
    /**
     * 容器中的Binding、Queue、Exchange都会自动创建（RabbitMQ没有）
     * 即使发生变化也不会覆盖队列
     * @return
     */
    @RabbitListener(queues = "order.release.order.queue")
    public void listener(OrderEntity orderEntity, Channel channel, Message message) throws IOException {
        System.out.println("收到过期的订单信息，准备关闭订单");
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
    @Bean
    public Queue orderDelayQueue() {
        HashMap<String, Object> argument = new HashMap<>();

        /**
         * x-dead-letter-exchange:order-exent-exchange
         * x-dead-letter-routing-key:order.release.order
         * x-message-ttl:60000
         */
        argument.put("x-dead-letter-exchange", "order-exent-exchange");
        argument.put("x-dead-letter-routing-key", "order.release.order");
        argument.put("x-message-ttl", 60000);

        Queue queue = new Queue("order.delay.queue", true, false, false,argument);
        return  queue;
    }
    @Bean
    public Queue orderReleaseOrderQueue() {
        Queue queue = new Queue("order.release.order.queue", true, false, false);
        return queue;
    }
    @Bean
    public Exchange orderEventExchange() {
        //String name, boolean durable, boolean autoDelete, Map<String, Object> arguments
        TopicExchange exchange = new TopicExchange("order_event_exchange", true, false);
        return exchange;
    }
    @Bean
    public Binding orderCreateOrder() {
        //String destination, Binding.DestinationType destinationType, String exchange, String routingKey, Map<String, Object> arguments
        return new Binding("order.delay.queue",
                Binding.DestinationType.QUEUE,
                "order_event_exchange",
                "order.create.order",
                null
                );
    }
    @Bean
    public Binding orderReleaseOrder() {
        return new Binding("order.release.order.queue",
                Binding.DestinationType.QUEUE,
                "order_event_exchange",
                "order.release.order",
                null
        );
    }
}

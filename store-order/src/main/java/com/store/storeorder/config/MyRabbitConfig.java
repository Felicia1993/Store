package com.store.storeorder.config;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class MyRabbitConfig {
    @Autowired
    RabbitTemplate rabbitTemplate;
    /*@Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }*/

    /**
     * 定制RabbitTemplate
     * 1.spring.rabbit.publisth_confirms=true
     * 2.设置确认回调
     */
    @PostConstruct//MyRabbitConfig对象 创建完成以后，执行这个方法
    public void initRabbitTemplate() {
        //设置确认回调
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            /**
             * @param correlationData 当前消息的唯一关联数据
             * @param ack
             * @param cause
             */
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {

                System.out.println("confirm....correlationData[" + correlationData + "]==>ack[" + ack + "]===>cause["+cause+"]");
            }
        });
    }

}

package com.store.storeorder.config;

import org.springframework.amqp.core.Message;
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
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * 定制RabbitTemplate
     * 1.服务器收到消息就回调
     *  1.spring.rabbit.publisth_confirms=true
     *  2.设置确认回调
     * 2.消息正确抵达队列回调
     */
    @PostConstruct//MyRabbitConfig对象 创建完成以后，执行这个方法
    public void initRabbitTemplate() {
        //设置确认回调
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            /**
             * 只要消息地道Broker就ack=true
             * @param correlationData 当前消息的唯一关联数据
             * @param ack
             * @param cause
             */
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {

                System.out.println("confirm....correlationData[" + correlationData + "]==>ack[" + ack + "]===>cause["+cause+"]");
            }
        });
        //消息正确抵达队列回调
        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            /**
             * 只要消息没有投递指定队列，就触发这个失败回调
             * @param message 投递失败的消息详细信息
             * @param replyCode 恢复的状态码
             * @param replyTest 回复的文本内容
             * @param exchange 当时这个消息发给那个交换机
             * @param routingKey 当时这个消息用那个路由键
             */
            @Override
            public void returnedMessage(Message message, int replyCode, String replyTest, String exchange, String routingKey) {
                System.out.println("Fail Message["+message +"]==>replyCode["+replyCode+"]=>exchange["+exchange+"]==>routingKey["+routingKey+"]");
            }
        });
    }

}

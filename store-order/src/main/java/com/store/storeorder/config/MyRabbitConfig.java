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
     *  1.spring.rabbitmq.publisher-returns=true
     *  2.spring.rabbitmq.template.mandatory=true
     * 3.消费端确认（保证每个消息被正确消费，此时才可以broker删除这个消息）
     * spring.rabbitmq.listener.simple.acknowledge-mode=manual
     *  1.默认是自动确认，只要消息接受到， 服务端就会移除这个消息
     *      问题：
     *      收到很多消息，自动恢复给服务器ack，只有一个消息处理成功，发生消息丢失
     *      手动确认：只要我们没有明确告诉mq，被签收，没有ack，消息一直就是unacked状态，消息不会丢失，会重新变为ready，下一次有新的consumer
     *  2.如何签收
     *      channel.basicAck(deliveryTag, false);
     *      channel.basicNack；拒签，业务失败
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

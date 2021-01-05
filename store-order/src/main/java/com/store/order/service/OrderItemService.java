package com.store.order.service;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service("orderItemService")
public class OrderItemService {
    /**
     * queues声明监听的所有队列
     * org.springframework.ampa.core.Message
     * 参数可以写以下类型
     * 1.Message message：原生消息详细信息
     * 2.T<发送的消息的类型>
     * 3.Channel channel:当前传输数据的通道
     *
     * Queue可以有很多监听。只要收到消息队列就会删除消息，最终消息只能有一个人收到
     * 场景
     *  1）订单服务启动多个
     */
    @RabbitListener(queues = {"hello-java-queue"})
    public void receiveMessage(Message message, Channel channel) {
        byte[] body = message.getBody();
        //消息头属性信息

        System.out.println("接受到消息。。。内容"+message + "==>类型 " + message);
    }
}

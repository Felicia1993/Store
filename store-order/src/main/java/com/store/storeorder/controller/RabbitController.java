package com.store.storeorder.controller;

import com.store.storeorder.entity.OrderEntity;
import com.store.storeorder.entity.OrderReturnReasonEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.UUID;

@RestController
public class RabbitController {
    @Autowired
    RabbitTemplate template;
    Logger logger = LoggerFactory.getLogger(RabbitController.class);
    @GetMapping("/sendMq")
    public String sendMq(@RequestParam(value = "num", defaultValue = "10") Integer num) {
        for(int i = 0; i < num; i++) {
            if (i%2==0) {
                OrderReturnReasonEntity orderReturnReasonEntity  = new OrderReturnReasonEntity();
                orderReturnReasonEntity.setId(1L);
                orderReturnReasonEntity.setCreateTime(new Date());
                orderReturnReasonEntity.setName("哈哈");
                //发送消息,如果发送的消息是个对象，会使用序列化机制，将对象写出去，对象必须实现serialize
                String msg = "Hello World!";
                template.convertAndSend("hello.java.exchange", "hello.java", orderReturnReasonEntity);
                logger.info("消息发送完成[{}]", orderReturnReasonEntity);
            } else {
                OrderEntity orderEntity = new OrderEntity();
                orderEntity.setOrderSn(UUID.randomUUID().toString());
                template.convertAndSend("hello.java.exchange","hello.java", orderEntity);
            }
        }
        return "OK";
    }
}

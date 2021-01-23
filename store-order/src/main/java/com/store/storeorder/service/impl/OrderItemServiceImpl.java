package com.store.storeorder.service.impl;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import com.store.storeorder.entity.OrderEntity;
import com.store.storeorder.entity.OrderReturnReasonEntity;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.store.common.utils.PageUtils;
import com.store.common.utils.Query;

import com.store.storeorder.dao.OrderItemDao;
import com.store.storeorder.entity.OrderItemEntity;
import com.store.storeorder.service.OrderItemService;


@Service("orderItemService")
public class OrderItemServiceImpl extends ServiceImpl<OrderItemDao, OrderItemEntity> implements OrderItemService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OrderItemEntity> page = this.page(
                new Query<OrderItemEntity>().getPage(params),
                new QueryWrapper<OrderItemEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * queues:声明需要监听的所有队列
     * import org.springframework.amqp.core.Message;
     * 参数可以写以下类型
     * 1.Message message：原生消息详细信息：头+体
     * 2.T<发送的消息类型>OrderReturnReasonEntity content</>
     * @param message
     */
    @Override
  //  @RabbitListener(queues = {"hello-java-queue"})
    @RabbitHandler
    public void receiveMessage(Message message, OrderReturnReasonEntity content, Channel channel) {
        byte[] body = message.getBody();
        JSON.parse(body);
        System.out.println("接受到消息。。。内容："+message+"==>类型：" + message);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("接受到的消息。。。" + message + "====》内容：" +content);
    }

    @RabbitHandler
    public void receiveMessage2(OrderEntity content) {
        System.out.println("接受到消息。。。" + content);

    }

}
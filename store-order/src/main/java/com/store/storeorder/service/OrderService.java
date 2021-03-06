package com.store.storeorder.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.store.common.utils.PageUtils;
import com.store.storeorder.entity.OrderEntity;
import com.store.storeorder.vo.OrderConfirmVo;
import com.store.storeorder.vo.OrderSubmitVo;
import com.store.storeorder.vo.SubmitOrderRespVo;

import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * ¶©µ¥
 *
 * @author xieqiqi
 * @email 359468250@qq.com
 * @date 2021-01-10 16:33:00
 */
public interface OrderService extends IService<OrderEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 订单确认页返回需要用的数据
     * @return
     */
    OrderConfirmVo confirmOrder() throws ExecutionException, InterruptedException;

    SubmitOrderRespVo submitOrder(OrderSubmitVo vo);
}


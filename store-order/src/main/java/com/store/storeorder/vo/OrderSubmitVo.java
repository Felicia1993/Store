package com.store.storeorder.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 下单功能
 */
@Data
public class OrderSubmitVo {
    private Long addrId;
    private Integer payType;//支付方式
    //无需提交购买的商品，去购物车再获取一遍
    //优惠发票
    private String orderToken;
    private BigDecimal payPrice;//验价
    private String node;//订单备注
    //用户相关信息都在session，直接去session中取出
}

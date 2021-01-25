package com.store.storeorder.vo;

import com.store.storeorder.entity.OrderEntity;
import lombok.Data;

@Data
public class SubmitOrderRespVo {
    private OrderEntity orderEntity;
    private Integer code;//0 成功 错误状态码

}

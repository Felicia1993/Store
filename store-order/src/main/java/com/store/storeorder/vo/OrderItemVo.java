package com.store.storeorder.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
@Data
public class OrderItemVo {
    private Long skuId;
    private String title;
    private String image;
    private List<String> skuAttr;
    private Integer count;
    private BigDecimal totalPrice;
    private BigDecimal price;
}

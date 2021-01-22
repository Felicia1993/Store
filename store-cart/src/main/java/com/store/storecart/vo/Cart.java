package com.store.storecart.vo;

import java.math.BigDecimal;
import java.util.List;

/**
 * 整个购物车
 */
public class Cart {
    List<CartItem> items;
    private Integer countNum;//商品数量
    private Integer countType;//商品类型
    private BigDecimal totalAmount;//商品总价
    private BigDecimal reduce = new BigDecimal(0);//减免价格

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    public Integer getCountNum() {
        int countNum = 0;
        //当前购物车有商品
        if(items != null && items.size() > 0) {
            for(CartItem item:items) {
                countNum += item.getCount();
            }
        }
        return countNum;
    }

    public Integer getCountType() {
        int countType = 0;
        if(items != null && items.size() > 0) {
            for(CartItem item:items) {
                countType += 1;
            }
        }
        return countType;
    }

    public BigDecimal getTotalAmount() {
        BigDecimal amount = new BigDecimal("0");
        if(items != null && items.size() > 0) {
            for(CartItem item:items) {
                BigDecimal totalPrice = item.getTotalPrice();
                amount.add(totalPrice);
            }
        }
        return amount.subtract(getReduce());
    }

    public BigDecimal getReduce() {
        return reduce;
    }

    public void setReduce(BigDecimal reduce) {
        this.reduce = reduce;
    }
}

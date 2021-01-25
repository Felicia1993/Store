package com.store.storeorder.vo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

//订单确认页需要用的数据
@Data
public class OrderConfirmVo {
    //收货地址列表ums_member_receive_address
    @Setter @Getter
    private List<MemberAddressVo> address;
    //送货清单,所有选中的购物项
    @Setter @Getter
    List<OrderItemVo> items;
    @Setter @Getter
    String orderToken;
    //发票记录。。。
    //优惠券信息
    Integer integration;
    BigDecimal total;//订单总额
    public BigDecimal getTotal() {
        BigDecimal total = new BigDecimal("0");
        if (items != null) {
            for (OrderItemVo item : items) {
                BigDecimal sum = item.getPrice().multiply(new BigDecimal(item.getCount().toString()));
                total=total.add(sum);
            }
        }
        return total;
    }
    BigDecimal payPrice;//应付价格
    public BigDecimal getPayPrice() {
        return getTotal();
    }
}

package com.store.storemember.feign;

import com.store.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("store-coupon")
public interface CouponFeignService {
    @RequestMapping("storecoupon/coupon/member/list")
    public R membercoupons();
}

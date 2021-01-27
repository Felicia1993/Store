package com.store.storeware.feign;

import com.store.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
@FeignClient("store-product")
public interface ProductFeignService {
    @RequestMapping("/info/{skuId}")
    R info(@PathVariable("skuId") Long skuId);
}

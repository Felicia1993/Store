package com.store.storecart.feign;

import com.store.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.List;
@FeignClient("store-product")
public interface ProductFeignService {
    @RequestMapping("/storeproduct/spuinfo/info/{skuId}")
    R getSkuInfo(@PathVariable("skuId") Long skuId);

    @GetMapping("/storeproduct/skusaleattrvalue/stringlist/{skuId}")
    List<String> getSkuSaleAttrValues(@PathVariable("skuId") Long skuId);
    @GetMapping("/storeproduct/skusaleattrvalue/{skuId}/price")
    R getPrice(@PathVariable("spuId") Long skuId);
}

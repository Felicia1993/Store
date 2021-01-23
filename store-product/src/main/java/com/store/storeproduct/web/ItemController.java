package com.store.storeproduct.web;

import com.store.storeproduct.service.SkuInfoService;
import com.store.storeproduct.vo.SkuItemVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public class ItemController {
    /**
     * 展示当前sku的属性
     */
    @Autowired
    SkuInfoService skuInfoService;
    @GetMapping("/{skuId}.html")
    public String skuItemm(@PathVariable("skuId") Long skuId) {
        System.out.println("准备查询"+skuId+"详情");
        SkuItemVo vo =skuInfoService.item(skuId);


        return "item";
    }
}

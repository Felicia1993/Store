package com.store.storesearch.controller;

import com.store.common.exception.BizCodeEnum;
import com.store.common.to.es.SkuEsModel;
import com.store.common.utils.R;
import com.store.storesearch.service.ProductSaveService;
import org.apache.juli.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/search/save")
@RestController
public class ElasticSaveController {
    @Autowired
    ProductSaveService productSaveService;
    //上架商品
    @PostMapping("/product")
    public R productStatusUp(@RequestBody List<SkuEsModel> skuEsModels){
        boolean b = false;
        try {
            b = productSaveService.productStatusUp(skuEsModels);
        } catch (Exception e) {
            return R.error(BizCodeEnum.PRODUCT_UP_EXCEPTION.getCode(), BizCodeEnum.PRODUCT_UP_EXCEPTION.getMsg());
        }
        if (b) {
            return R.ok();
        } else {
            return R.error();
        }

    }
}

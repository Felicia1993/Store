package com.store.storesearch.controller;

import com.store.common.to.es.SkuEsModel;
import com.store.common.utils.R;
import com.store.storesearch.service.ProductSaveService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public R productStatusUp(@RequestBody List<SkuEsModel> skuEsModels){
        productSaveService.productStatusUp(skuEsModels);
        return R.ok();
    }
}

package com.store.storesearch.service;

import com.store.common.to.es.SkuEsModel;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface ProductSaveService {

    void productStatusUp(List<SkuEsModel> skuEsModels);
}

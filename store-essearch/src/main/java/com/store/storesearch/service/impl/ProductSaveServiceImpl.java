package com.store.storesearch.service.impl;

import com.alibaba.fastjson.JSON;
import com.store.common.to.es.SkuEsModel;
import com.store.storesearch.config.ElasticSearchConfig;
import com.store.storesearch.constant.EsConstant;
import com.store.storesearch.service.ProductSaveService;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ProductSaveServiceImpl implements ProductSaveService {
    protected Log log = LogFactory.getLog(this.getClass());
    @Autowired
    RestHighLevelClient restHighLevelClient;
    @Override
    public boolean productStatusUp(List<SkuEsModel> skuEsModels){
        //1.给es建立索引,建立好映射关系
        //2.给es中保存数据
        BulkRequest bulkRequest = new BulkRequest();
        try {
            for(SkuEsModel model:skuEsModels) {
                IndexRequest indexRequest = new IndexRequest(EsConstant.PRODUCT_INDEX);
                indexRequest.id(model.getSkuId().toString());
                String s = JSON.toJSONString(model);
                indexRequest.source(s, XContentType.JSON);
                bulkRequest.add(indexRequest);
            }
            BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, ElasticSearchConfig.COMMON_OPTIONS);
            //todo:如果批量错误
            boolean b= bulk.hasFailures();
            List<String> collect = Arrays.stream(bulk.getItems()).map(item -> {
                return item.getId();
            }).collect(Collectors.toList());
            log.error("商品上架错误{}", (Throwable) collect);
            return b;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}

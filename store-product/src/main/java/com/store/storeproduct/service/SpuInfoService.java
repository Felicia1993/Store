package com.store.storeproduct.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.store.common.to.es.SkuEsModel;
import com.store.common.utils.PageUtils;
import com.store.storeproduct.entity.SpuInfoEntity;

import java.util.List;
import java.util.Map;

/**
 * spuÐÅÏ¢
 *
 * @author xieqiqi
 * @email 359468250@qq.com
 * @date 2021-01-18 16:44:43
 */
public interface SpuInfoService extends IService<SpuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 商品上架
     * @param spuId
     */
    List<SkuEsModel> up(Long spuId);
}


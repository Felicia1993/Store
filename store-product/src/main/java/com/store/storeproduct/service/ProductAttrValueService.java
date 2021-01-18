package com.store.storeproduct.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.store.common.utils.PageUtils;
import com.store.storeproduct.entity.ProductAttrValueEntity;

import java.util.List;
import java.util.Map;

/**
 * spuÊôÐÔÖµ
 *
 * @author xieqiqi
 * @email 359468250@qq.com
 * @date 2021-01-18 18:20:07
 */
public interface ProductAttrValueService extends IService<ProductAttrValueEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<ProductAttrValueEntity> baseAttrlistforspu(Long spuId);

    List<Long> selectSearchAttrIds(List<Object> attrIds);
}


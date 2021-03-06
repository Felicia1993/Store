package com.store.storeproduct.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.store.common.utils.PageUtils;
import com.store.storeproduct.entity.SkuInfoEntity;
import com.store.storeproduct.entity.SpuInfoEntity;
import com.store.storeproduct.vo.SkuItemVo;

import java.util.List;
import java.util.Map;

/**
 * skuÐÅÏ¢
 *
 * @author xieqiqi
 * @email 359468250@qq.com
 * @date 2021-01-09 23:09:10
 */
public interface SkuInfoService extends IService<SkuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<SkuInfoEntity>  getSkusBySpuId(Long spuId);

    SkuItemVo item(Long skuId);
}


package com.store.storeware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.store.common.utils.PageUtils;
import com.store.storeware.entity.WareSkuEntity;

import java.util.Map;

/**
 * ÉÌÆ·¿â´æ
 *
 * @author xieqiqi
 * @email 359468250@qq.com
 * @date 2021-01-10 16:46:40
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);
}


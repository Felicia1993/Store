package com.store.storeware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.store.common.utils.PageUtils;
import com.store.storeware.entity.PurchaseEntity;

import java.util.Map;

/**
 * ²É¹ºÐÅÏ¢
 *
 * @author xieqiqi
 * @email 359468250@qq.com
 * @date 2021-01-10 16:46:40
 */
public interface PurchaseService extends IService<PurchaseEntity> {

    PageUtils queryPage(Map<String, Object> params);
}


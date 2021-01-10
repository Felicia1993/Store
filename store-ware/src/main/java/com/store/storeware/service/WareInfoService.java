package com.store.storeware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.store.common.utils.PageUtils;
import com.store.storeware.entity.WareInfoEntity;

import java.util.Map;

/**
 * ²Ö¿âÐÅÏ¢
 *
 * @author xieqiqi
 * @email 359468250@qq.com
 * @date 2021-01-10 16:46:39
 */
public interface WareInfoService extends IService<WareInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);
}


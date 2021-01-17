package com.store.storeproduct.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.store.common.utils.PageUtils;
import com.store.storeproduct.entity.AttrEntity;

import java.util.List;
import java.util.Map;

/**
 * ÉÌÆ·ÊôÐÔ
 *
 * @author xieqiqi
 * @email 359468250@qq.com
 * @date 2021-01-09 23:09:10
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);
    List<Long> selectSearchAttrIds(List<Long> attrIds);
}


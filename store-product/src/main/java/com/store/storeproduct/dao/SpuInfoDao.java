package com.store.storeproduct.dao;

import com.store.storeproduct.entity.SpuInfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * spuÐÅÏ¢
 * 
 * @author xieqiqi
 * @email 359468250@qq.com
 * @date 2021-01-18 16:44:43
 */
@Mapper
public interface SpuInfoDao extends BaseMapper<SpuInfoEntity> {

    void updateSpuStatus(@Param("spuId") Long spuId,@Param("code") int code);
}

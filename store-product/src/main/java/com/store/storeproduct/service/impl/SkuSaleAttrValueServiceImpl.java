package com.store.storeproduct.service.impl;

import com.store.storeproduct.dao.SkuInfoDao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.store.common.utils.PageUtils;
import com.store.common.utils.Query;

import com.store.storeproduct.dao.SkuSaleAttrValueDao;
import com.store.storeproduct.entity.SkuSaleAttrValueEntity;
import com.store.storeproduct.service.SkuSaleAttrValueService;


@Service("skuSaleAttrValueService")
public class SkuSaleAttrValueServiceImpl extends ServiceImpl<SkuSaleAttrValueDao, SkuSaleAttrValueEntity> implements SkuSaleAttrValueService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuSaleAttrValueEntity> page = this.page(
                new Query<SkuSaleAttrValueEntity>().getPage(params),
                new QueryWrapper<SkuSaleAttrValueEntity>()
        );

        return new PageUtils(page);
    }
    @Override
    public List<String> getSkuSaleAttrValues(Long skuId) {
        SkuSaleAttrValueDao dao = this.baseMapper;
        return dao.getSkuSaleAttrValues(skuId);
    }

}
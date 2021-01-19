package com.store.storeproduct.service.impl;

import com.store.common.constant.ProductConstant;
import com.store.common.to.SkuHasStockVo;
import com.store.common.to.es.SkuEsModel;
import com.store.common.utils.R;
import com.store.storeproduct.entity.*;
import com.store.storeproduct.feign.SearchFeignService;
import com.store.storeproduct.feign.WareFeignService;
import com.store.storeproduct.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.store.common.utils.PageUtils;
import com.store.common.utils.Query;

import com.store.storeproduct.dao.SpuInfoDao;


@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {
    @Autowired
    SkuInfoService skuInfoService;
    @Autowired
    BrandService brandService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    ProductAttrValueService productAttrValueService;
    @Autowired
    WareFeignService feignService;
    @Autowired
    SearchFeignService searchFeignService;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>()
        );

        return new PageUtils(page);
    }

    /**
     *
     * @param spuId
     */
    @Override
    public List<SkuEsModel> up(Long spuId){
        //组装需要的数据
        //1.当前spuId对应的所有sku信息，品牌的名字
        List<SkuInfoEntity> skus = skuInfoService.getSkusBySpuId(spuId);
        List<Long> skuIdList = skus.stream().map(SkuInfoEntity::getSkuId).collect(Collectors.toList());
        //todo:4.查询当前sku所有可以被用来检索的规格属性
        List<ProductAttrValueEntity> baseAttrs = productAttrValueService.baseAttrlistforspu(spuId);
        List<Object> attrIds = baseAttrs.stream().map(attr -> {
            return attr.getAttrId();
        }).collect(Collectors.toList());
    //在指定的所有集合属性里，挑出检索属性
        List<Long> searchAttrIds = productAttrValueService.selectSearchAttrIds(attrIds);

        HashSet<Long> idSets = new HashSet<>(searchAttrIds);
        List<SkuEsModel.Attrs> attrs = new ArrayList<>();
        List<SkuEsModel.Attrs> attrsList = baseAttrs.stream().filter(item -> {
            return idSets.contains(item);
        }).map(item -> {
            SkuEsModel.Attrs attrs1 = new SkuEsModel.Attrs();
            BeanUtils.copyProperties(item, attrs1);
            return attrs1;
        }).collect(Collectors.toList());
        //todo:1.发送远程调用，查询是否有库存
        Map<Long, Integer>stockMap = null;
        try {
            R<List<SkuHasStockVo>> skuHasStock = feignService.getSkuHasStock(skuIdList);
            stockMap = skuHasStock.getData().stream().collect(Collectors.toMap(SkuHasStockVo::getSkuId, item -> item.getHasStock()));

        } catch (Exception e){
            log.error("库存服务查询异常，原因{}",e);
        }
        //2.封装每个sku的信息
        Map<Long, Integer> finalStockMap = stockMap;
        List<SkuEsModel> upProducts = skus.stream().map(sku -> {
            SkuEsModel skuEsModel = new SkuEsModel();
            BeanUtils.copyProperties(sku,skuEsModel);

            //skuPrices skuImg private String brandName;
            //    private String brandImg;
            //    private String catalogName;

            skuEsModel.setSkuPrice(sku.getPrice());
            skuEsModel.setSkuImg(sku.getSkuDefaultImg());
            if (finalStockMap == null) {
                skuEsModel.setHasStock(1);
            } else {
                skuEsModel.setHasStock(finalStockMap.get(sku.getSkuId()));
            }


            //todo:2.热度评分 0
            skuEsModel.setHotScore(0L);
            //todo:3.查询品牌和分类的名字信息
            BrandEntity brand = brandService.getById(skuEsModel.getBrandId());
            skuEsModel.setBrandName(brand.getName());
            skuEsModel.setBrandImg(brand.getLogo());

            CategoryEntity category = categoryService.getById(skuEsModel.getCatalogId());
            skuEsModel.setCatalogName(category.getName());
            //设置检索属性
            skuEsModel.setAttrs(attrsList);


            return skuEsModel;
        }).collect(Collectors.toList());

        //todo 5.将数据发送给es进行保存，store-essearch
        R r = searchFeignService.productStatusUp(upProducts);
        if (r.getCode() == 0) {
            //todo 6.修改当前spu的状态
            baseMapper.updateSpuStatus(spuId, ProductConstant.StatusEnum.SPU_UP.getCode());
        } else {
            //远程调用失败
            //todo 7 接口幂等性：重试机制
        }
        return upProducts;

    }

}
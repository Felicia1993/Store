package com.store.storeproduct.vo;

import com.store.storeproduct.entity.SkuImagesEntity;
import com.store.storeproduct.entity.SkuInfoEntity;
import com.store.storeproduct.entity.SpuInfoDescEntity;
import lombok.Data;

import java.util.List;
@Data
public class SkuItemVo {
    //1.基本sku信息获取 pms_sku_info
    SkuInfoEntity info;
    //2.sku的图片信息 pms_sku_images
    List<SkuImagesEntity> images;
    //3.获取spu的销售属性组合
    List<ItemSaleAttrVo> saleAttr;
    //4.获取spu的价格
    SpuInfoDescEntity desp;
    //5.获取spu的规格参数信息
    List<SpuItemAttrGroupVo> groupAttrs;


}

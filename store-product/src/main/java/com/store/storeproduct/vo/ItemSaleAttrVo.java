package com.store.storeproduct.vo;

import lombok.Data;

import java.util.List;
@Data
public class ItemSaleAttrVo {
    private Long attrId;
    private String attrName;
    private List<String> attrValues;

}

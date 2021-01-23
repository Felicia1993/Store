package com.store.storeproduct.vo;

import lombok.Data;

import java.util.List;

@Data
public class SpuItemBaseAttrVo {
    private String groupName;
    private List<SpuItemAttrGroupVo> attrs;

}

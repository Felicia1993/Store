package com.store.common.es;

import jdk.internal.util.xml.impl.Attrs;
import sun.rmi.runtime.Log;

import java.math.BigDecimal;
import java.util.List;

public class SkuEsModel {
    private Long skuId;
    private Long spuId;
    private String skuTitle;
    private BigDecimal skuPrice;
    private String skuImg;
    private Long saleCount;
    private Boolean hasStock;
    private Long brandId;
    private String brandName;
    private String brandImg;
    private String catalogName;
    private List<Attrs> attrs;
}

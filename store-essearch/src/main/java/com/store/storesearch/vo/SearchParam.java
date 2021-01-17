package com.store.storesearch.vo;

import lombok.Data;

import java.util.List;

/**
 * 封装页面所有可能传递的查询条件
 * 全文检索：skuTitle=>keyword
 * 排序：saleCount，hotSource，skuPrice
 * 过滤：hasStock是否有货，skuPrice区间，brandId，catalogId，attrs
 * 聚合：attrs
 */
@Data
public class SearchParam {
    private String keyword;//全文匹配
    private Long catelog3Id;//三级分类ID
    /**
     * sort=soleCount_asc/desc
     * sort=skuPrice_asc/desc
     * sort=hotScore_asc/desc
     */
    private String sort;//排序条件
    private Integer hasStock = 1;//是否仅显示有货
    private String skuPrice;
    private List<Long> brandId;//按照品牌进行筛选
    private List<String> attrs;//按照属性进行筛选
    private Integer pageNum;//页码
}

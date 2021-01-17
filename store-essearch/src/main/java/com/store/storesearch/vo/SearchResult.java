package com.store.storesearch.vo;

import com.store.storesearch.model.SkuEsModel;
import lombok.Data;

import java.util.List;
@Data
public class SearchResult {
    //查询到的所有商品信息
    private List<SkuEsModel> products;
    //以下是所有分页数据
    private Integer pageNum;//当前页码
    private Long total;//总记录数
    private Integer totalPages;//总页码
    //
    private List<BrandVo> brands;//当前查询到的结果，所有涉及到的品牌
    private List<CatalogVo> catalogs;//当前查询到的结果，所有涉及的所有分类
    private List<AttrVo> attrs;//当前查询到的结果，所有涉及到的所有属性

    @Data
    public static class BrandVo{
        private Long brandId;
        private String brandName;
        private String brandImg;
    }
    @Data
    public static class AttrVo{
        private Long attrId;
        private String attrName;
        private List<String> attrValue;
    }

    @Data
    public static class CatalogVo{
        private Long catalogId;
        private String catalogName;
        private List<String> attrValue;
    }
}

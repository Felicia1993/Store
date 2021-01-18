package com.store.storesearch.service.impl;

import com.store.common.to.es.SkuEsModel;
import com.store.storesearch.config.ElasticSearchConfig;
import com.store.storesearch.constant.EsConstant;
import com.store.storesearch.service.MallSearchService;
import com.store.storesearch.vo.SearchParam;
import com.store.storesearch.vo.SearchResult;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class MallSearchServiceImpl implements MallSearchService {
    @Autowired
    private RestHighLevelClient client;
    @Override
    public SearchResult search(SearchParam searchParam) {
        //1.动态构建出检索的DSL语句
        SearchResult result = null;
        //1.准备检索请求
        //2.执行检索请求
        //3.分析响应数据，封装成需要的格式
        SearchRequest searchRequest = buildSearchRequest(searchParam);
        try {
            SearchResponse response = client.search(searchRequest, ElasticSearchConfig.COMMON_OPTIONS);
            result = buildSearchResult(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 构建结果数据
     * @param response
     * @return
     */
    private SearchResult buildSearchResult(SearchResponse response) {
        SearchResult result = new SearchResult();
        SearchHits hits = response.getHits();
        List<SkuEsModel> esModels = new ArrayList<>();
        if (hits.getHits() != null && hits.getHits().length > 0) {
            for(SearchHit hit:hits.getHits()) {
                String sourceAsString = hit.getSourceAsString();
                SkuEsModel esModel = new SkuEsModel();
                esModels.add(esModel);
            }
        }
        //返回的所有查询到的商品
        /*result.setProducts(esModels);
        //当前所有商品设计的所有属性信息
        result.setCatalogs();
        //当前素有商品设计的所有品牌信息
        result.setBrands();
        //======从聚合信息中获取到=====
        result.setPageNum();*/
        Long total = hits.getTotalHits().value;
        result.setTotal(total);
        int totalPages = (int) (total%EsConstant.PRODUCT_PAGESIZE == 0 ? total/EsConstant.PRODUCT_PAGESIZE : (total/EsConstant.PRODUCT_PAGESIZE + 1));
        result.setTotalPages(totalPages);
        return null;
    }

    /**
     * 准备检索请求
     * 模糊匹配、过滤（按照属性、分类、品牌、价格区间、库存），排序，分页，高亮，聚合分析
     * @return
     */
    private SearchRequest buildSearchRequest(SearchParam searchParam) {
        SearchSourceBuilder builder = new SearchSourceBuilder();
        /**
         * 模糊匹配、过滤（按照属性、分类、品牌、价格区间、库存）
         */
        //1.构建bool --query
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //1.1 must
        if (!StringUtils.isEmpty(searchParam.getKeyword())) {
            boolQueryBuilder.must(QueryBuilders.matchQuery("skuTitle",searchParam.getKeyword()));
        }
        //1.2 filter 按照三级分类Id查询
        if (searchParam.getCatelog3Id() != null) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("catelogId",searchParam.getCatelog3Id()));
        }
        //1.2 bool filter 按照品牌Id进行查询
        if (searchParam.getBrandId() != null && searchParam.getBrandId().size() > 0){
            boolQueryBuilder.filter(QueryBuilders.termQuery("brandId", searchParam.getBrandId()));
        }
        //1.2 bool filter按照所有指定的属性进行查询

        //1.2 bool filter按照是否有库存
        boolQueryBuilder.filter(QueryBuilders.termQuery("hasStock", searchParam.getHasStock()==1));
        //1.2 bool filter按照价格区间
        if (!StringUtils.isEmpty(searchParam.getSkuPrice())) {
            //1_500/_500/500_
            /**
             * "range":{
             *     "skuPrice":{
             *         "gte":0.
             *         "lte":5000
             *     }
             *
             * }
             */
            RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("skuPrice");
            String[] s = searchParam.getSkuPrice().split("_");
            if (s.length == 2) {//区间
                rangeQuery.gte(s[0]).lte(s[1]);
            } else if(s.length ==1) {
                if (searchParam.getSkuPrice().startsWith("_")){
                    rangeQuery.lte(s[0]);
                }
                if (searchParam.getSkuPrice().endsWith("_")) {
                    rangeQuery.gte(s[1]);
                }
            }
            boolQueryBuilder.filter(rangeQuery);
        }
        //1.2 bool filter按照属性进行查询
        if (searchParam.getAttrs() != null && searchParam.getAttrs().size() > 0) {

            for (String attr : searchParam.getAttrs()) {
                BoolQueryBuilder nestedboolQuery = QueryBuilders.boolQuery();
                //attr:1_5寸，1_8寸
                String[] s = attr.split("_");
                String attrId = s[0];
                String[] attrValues = s[1].split(":");//这个属性的检索用的值
                nestedboolQuery.must(QueryBuilders.termQuery("attrs.attrId", attrId));
                //每一个必须都得生成一个nested查询
                nestedboolQuery.must(QueryBuilders.termQuery("attrs.attrValue", attrValues));
                NestedQueryBuilder nestedQuery = QueryBuilders.nestedQuery("attrs", nestedboolQuery, ScoreMode.None);
                boolQueryBuilder.filter(nestedQuery);
            }
        }
        builder.query(boolQueryBuilder);
        /**
         * 排序，分页，高亮，聚合分析
         */
        //2.1排序
        if (!StringUtils.isEmpty(searchParam.getSort())) {
            String sort = searchParam.getSort();
            String[] s = sort.split("_");
            SortOrder order = s[1].equalsIgnoreCase("asc") ? SortOrder.ASC : SortOrder.DESC;
            builder.sort(s[0], order);
        }
        //2.2分页
        //from = (pageNum-1) * pageSize
        builder.from(((searchParam.getPageNum()-1) * EsConstant.PRODUCT_PAGESIZE));
        builder.size(EsConstant.PRODUCT_PAGESIZE);

        //2.3高亮
        if (!StringUtils.isEmpty(searchParam.getKeyword())) {
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.field("skuTitle");
            highlightBuilder.preTags("<b style='color:red'>");
            highlightBuilder.postTags("</b>");
            builder.highlighter(highlightBuilder);
        }
        /**
         * 聚合分析
         */
        //1.品牌聚合
        TermsAggregationBuilder brand_agg = AggregationBuilders.terms("brand_agg");
        brand_agg.field("brandId").size(50);
        brand_agg.subAggregation(AggregationBuilders.terms("brand_name_agg").field("brandName").size(10));
        brand_agg.subAggregation(AggregationBuilders.terms("brand_img_agg").field("brandImg").size(10));
        builder.aggregation(brand_agg);
        //2.分类聚合 catelog_agg
        TermsAggregationBuilder catelog_agg = AggregationBuilders.terms("catelog_agg").field("catelogId").size(20);
        catelog_agg.subAggregation(AggregationBuilders.terms("catelog_name_agg").field("catelogName").size(10));
        builder.aggregation(catelog_agg);
        //3.属性聚合
        NestedAggregationBuilder attr_agg = AggregationBuilders.nested("attr_agg", "attrs");
        //聚合出当前所有的attrId
        TermsAggregationBuilder attr_id_agg = AggregationBuilders.terms("attr_id_agg").field("attrs.attrId");
        //聚合分析当前attr_id对应的名字
        attr_id_agg.subAggregation(AggregationBuilders.terms("attr_name_agg").field("attrs.attrName").size(1));
        attr_id_agg.subAggregation(AggregationBuilders.terms("attr_value_agg").field("attrs.attrValue").size(50));
        attr_agg.subAggregation(attr_id_agg);
        builder.aggregation(attr_agg);


        String s = builder.toString();
        System.out.println("构建的dsl语句"+s);
        SearchRequest searchRequest = new SearchRequest(new String[]{EsConstant.PRODUCT_INDEX}, builder);
        return searchRequest;
    }
}

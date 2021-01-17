package com.store.storesearch.service.impl;

import com.store.common.utils.Query;
import com.store.storesearch.config.ElasticSearchConfig;
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
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;

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

    }

    /**
     * 构建结果数据
     * @param response
     * @return
     */
    private SearchResult buildSearchResult(SearchResponse response) {
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
        SearchRequest searchRequest = new SearchRequest(new String[]{EsConstant.PRODUCT_INDEX}, builder);
        return searchRequest;
    }
}

package com.store.storesearch;

import com.alibaba.fastjson.JSON;
import com.store.storesearch.config.ElasticSearchConfig;
import lombok.Data;
import lombok.ToString;
import net.minidev.json.JSONObject;
import org.apache.lucene.util.QueryBuilder;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.Avg;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.naming.directory.SearchResult;
import java.io.IOException;
import java.util.Map;

@SpringBootTest
class StoreSearchApplicationTests {
    @Autowired
    private RestHighLevelClient client;

    /**
     * Copyright 2021 bejson.com
     */

    /**
     * Auto-generated: 2021-01-09 18:27:26
     *
     * @author bejson.com (i@bejson.com)
     * @website http://www.bejson.com/java2pojo/
     */
    @Data
    @ToString
    public static class Account {

        private int account_number;
        private int balance;
        private String firstname;
        private String lastname;
        private int age;
        private String gender;
        private String address;
        private String employer;
        private String email;
        private String city;
        private String state;
        public void setAccount_number(int account_number) {
            this.account_number = account_number;
        }
        public int getAccount_number() {
            return account_number;
        }

        public void setBalance(int balance) {
            this.balance = balance;
        }
        public int getBalance() {
            return balance;
        }

        public void setFirstname(String firstname) {
            this.firstname = firstname;
        }
        public String getFirstname() {
            return firstname;
        }

        public void setLastname(String lastname) {
            this.lastname = lastname;
        }
        public String getLastname() {
            return lastname;
        }

        public void setAge(int age) {
            this.age = age;
        }
        public int getAge() {
            return age;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }
        public String getGender() {
            return gender;
        }

        public void setAddress(String address) {
            this.address = address;
        }
        public String getAddress() {
            return address;
        }

        public void setEmployer(String employer) {
            this.employer = employer;
        }
        public String getEmployer() {
            return employer;
        }

        public void setEmail(String email) {
            this.email = email;
        }
        public String getEmail() {
            return email;
        }

        public void setCity(String city) {
            this.city = city;
        }
        public String getCity() {
            return city;
        }

        public void setState(String state) {
            this.state = state;
        }
        public String getState() {
            return state;
        }

    }

    @Test
    public void searchData() throws IOException {
        //1.创建检索请求
        SearchRequest searchRequest = new SearchRequest();
        //2.指定索引
        searchRequest.indices("bank");
        //指定DSL，检索条件
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //1.1 构造检索条件
        /*searchSourceBuilder.query();
        searchSourceBuilder.from();
        searchSourceBuilder.size();
        searchSourceBuilder.aggregations();*/
        searchSourceBuilder.query(QueryBuilders.matchQuery("address", "mill"));
        //按照年龄聚合
        TermsAggregationBuilder ageAgg = AggregationBuilders.terms("ageAgg").field("age").size(10);
        searchSourceBuilder.aggregation(ageAgg);
        //计算平均年龄
        TermsAggregationBuilder field = AggregationBuilders.terms("ageAvg").field("age");
        searchSourceBuilder.aggregation(field);
        //求出平均薪资
        TermsAggregationBuilder balanceAgg = AggregationBuilders.terms("balanceAgg").field("balance");
        searchSourceBuilder.aggregation(balanceAgg);

        System.out.println("检索条件"+searchSourceBuilder.toString());
        searchRequest.source(searchSourceBuilder);
        //2.执行检索
        SearchResponse search = client.search(searchRequest, ElasticSearchConfig.COMMON_OPTIONS);
        //分析结果
        //3.1获取所有命中的记录
        SearchHits hits = search.getHits();
        SearchHit[] searchHits = hits.getHits();
        for(SearchHit hit : searchHits) {
            hit.getIndex();hit.getId();
            String sourceAsString = hit.getSourceAsString();
            Account account = JSON.parseObject(sourceAsString, Account.class);
            System.out.println("account" + account);
        }
        //3.2获取这次检索到的分析信息
        Aggregations aggregations = search.getAggregations();
       /* for(Aggregation aggregation:aggregations) {
            System.out.println("当前聚合的名字" + aggregation.getName());
            //获取聚合数据

        }*/
        Terms ageAgg1 = aggregations.get("ageAgg");
        for (Terms.Bucket bucket: ageAgg1.getBuckets()) {
            String keyAsString = bucket.getKeyAsString();
            System.out.println("年龄"+keyAsString);
        }
        Terms ageAvg = aggregations.get("ageAvg");
        System.out.println("平均薪资"+ageAvg);

   //     System.out.println(search.toString());
    }

    /**
     * 测试存储数据到es
     * @throws IOException
     */
    @Test
    void indexData() throws IOException {
        IndexRequest indexRequest = new IndexRequest("user");
        indexRequest.id("1");
   //     indexRequest.source("userName","zhangsan","age",18,"gender","男");
        User user = new User();
        user.setUserName("zhangsan");
        user.setGender("M");
        user.setAge(30);
        String jsonString = JSON.toJSONString(user);
        indexRequest.source(jsonString, XContentType.JSON);//要保存的内容
//执行操作
        final IndexResponse index = client.index(indexRequest, ElasticSearchConfig.COMMON_OPTIONS);
        //提取有用的响应
        System.out.println(index);

    }
    @Data
    class User{
        private String userName;
        private String gender;
        private Integer age;

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }
    }
    @Test
    void contextLoads() {
        System.out.println(client);
    }

}

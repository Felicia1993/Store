package com.store.storesearch.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.*;
import org.springframework.context.annotation.Configuration;

/**
 * 1.导入依赖
 * 2.编写配置
 * 3.
 */
@Configuration
public class ElasticSearchConfig {
    public static final RequestOptions COMMON_OPTIONS;
    static {
        RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();
        /*builder.addHeader("Authorization", "Bearer " + TOKEN);
        builder.setHttpAsyncResponseConsumerFactory(
                new HttpAsyncResponseConsumerFactory
                        .HeapBufferedResponseConsumerFactory(30 * 1024 * 1024 * 1024));*/
        COMMON_OPTIONS = builder.build();
    }

    public RestHighLevelClient esRestClient() {
        RestClientBuilder builder=  null;
        builder = RestClient.builder(new HttpHost("127.0.0.1", 9200, "http"));
        RestHighLevelClient client = new RestHighLevelClient(builder);
        return client;
    }
}

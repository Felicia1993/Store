package com.store.common.utils;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpUtils {
    public static HttpResponse doGet(String host, String path, String method, Map<String, String>headers,Map<String,String> querys) throws IOException {
        HttpClient httpClient = wrapClient(host);
        HttpGet request = new HttpGet(buildUrl(host,path,querys));
        for(Map.Entry<String,String> e:headers.entrySet()) {
            request.addHeader(e.getKey(), e.getValue());
        }
        return httpClient.execute(request);
    }

    public static HttpResponse doPost(String host, String path, String method, Map<String, String>headers,Map<String,String> querys,Map<String,String> body) throws IOException {
        HttpClient httpClient = wrapClient(host);
        HttpPost request = new HttpPost(buildUrl(host, path, querys));
        for (Map.Entry<String, String> e : headers.entrySet()) {
            request.addHeader(e.getKey(), e.getValue());
        }
        if (body != null) {

        }
        return httpClient.execute(request);
    }

    private static String buildUrl(String host, String path, Map<String, String> querys) {
        return null;
    }

    private static HttpClient wrapClient(String host) {
        return null;
    }
}

package com.store.storesearch.service;

import com.store.storesearch.vo.SearchParam;
import com.store.storesearch.vo.SearchResult;

public interface MallSearchService {
    /**
     * 检索的所有参数，返回检索的结果
     *
     * @return
     */
    public SearchResult search(SearchParam searchParam);
}

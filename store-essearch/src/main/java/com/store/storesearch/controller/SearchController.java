package com.store.storesearch.controller;

import com.store.storesearch.service.MallSearchService;
import com.store.storesearch.vo.SearchParam;
import com.store.storesearch.vo.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

public class SearchController {
    @Autowired
    MallSearchService mallSearchService;
    @GetMapping("/list")
    public String listPage(SearchParam searchParam, Model model) {
        //根据传递来的页面的查询参数，去es中检索
        SearchResult result =  mallSearchService.search(searchParam);
        model.addAttribute("result",result);

        return "list";
    }
}

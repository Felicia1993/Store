package com.store.storeorder.feign;

import com.store.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
@FeignClient("store-ware")
public interface WmsFeignService {
    @GetMapping("/storeware/wareinfo/fare")
    R getFare(@RequestParam("addrId" ) Long addrId);
}

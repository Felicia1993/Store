package com.store.storeorder.feign;

import com.store.common.utils.R;
import com.store.storeorder.vo.WareSkuLockVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
@FeignClient("store-ware")
public interface WmsFeignService {
    @GetMapping("/storeware/wareinfo/fare")
    R getFare(@RequestParam("addrId" ) Long addrId);

    @PostMapping("/storeware/wareinfo/lock/order")
    R orderLockStock(@RequestBody WareSkuLockVo vo);
}

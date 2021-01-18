package com.store.storeproduct.feign;

import com.store.common.to.SkuHasStockVo;
import com.store.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient("store-ware")
public interface WareFeignService {

    /**
     * 1.R设计的时候可以加上泛型
     * 2.直接返回我们想要的结果
     * 3.自己封装解析结果
     * @param skuIds
     * @return
     */
    @PostMapping("/storeware/waresku/hasstock")
    R<List<SkuHasStockVo>> getSkuHasStock(@RequestBody List<Long> skuIds);
}

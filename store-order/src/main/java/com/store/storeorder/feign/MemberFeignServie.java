package com.store.storeorder.feign;

import com.store.storeorder.vo.MemberAddressVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient
public interface MemberFeignServie {
    @GetMapping("/{memberId}/addresses")
    List<MemberAddressVo> getAddress(Long memberId);
}

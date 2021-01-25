package com.store.storeorder.feign;

import com.store.storeorder.vo.MemberAddressVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient("store-member")
public interface MemberFeignServie {
    @GetMapping("/storemember/member/{memberId}/addresses")
    List<MemberAddressVo> getAddress(Long memberId);
}

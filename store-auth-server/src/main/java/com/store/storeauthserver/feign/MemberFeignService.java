package com.store.storeauthserver.feign;

import com.store.common.utils.R;
import com.store.storeauthserver.vo.SocialUser;
import com.store.storeauthserver.vo.UserRegistVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("store-member")
public interface MemberFeignService {
    @PostMapping("storemember/member/regist")
    R regist(@RequestBody UserRegistVo vo) ;

    @PostMapping("storemember/member/login")
    R oauthlogin(@RequestBody SocialUser vo);
}

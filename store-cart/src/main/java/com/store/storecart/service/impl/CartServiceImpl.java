package com.store.storecart.service.impl;

import com.store.storecart.interceptor.CartInterceptor;
import com.store.storecart.service.CartService;
import com.store.storecart.vo.CartItem;
import com.store.storecart.vo.UserInfoTo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CartServiceImpl implements CartService {
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Override
    public List<CartItem> getUserCartItems(){
        UserInfoTo userInfoTo= CartInterceptor.threadLocal.get();
        return null;
    }
}

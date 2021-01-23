package com.store.storecart.service.impl;

import com.store.storecart.service.CartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
@Slf4j
@Service
public class CartServiceImpl implements CartService {
    StringRedisTemplate stringRedisTemplate;

}

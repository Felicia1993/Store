package com.store.storeproduct.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class MyRedissonConfig {
    /**
     * 所有对redisson的使用都使用redissonCLient
     * @return
     * @throws IOException
     */
    @Bean(destroyMethod="shutdown")
    public RedissonClient redisson() throws IOException {
        //创建配置
        Config config = new Config();
        config.useSingleServer().setAddress("192.168.124.8:6379");
        //创建实例
        RedissonClient redissonClient = Redisson.create();
        return redissonClient;
    }


}

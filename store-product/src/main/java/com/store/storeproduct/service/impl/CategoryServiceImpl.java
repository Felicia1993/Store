package com.store.storeproduct.service.impl;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.store.common.utils.PageUtils;
import com.store.common.utils.Query;

import com.store.storeproduct.dao.CategoryDao;
import com.store.storeproduct.entity.CategoryEntity;
import com.store.storeproduct.service.CategoryService;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {
    private Map<String,Object> cache = new HashMap<String, Object>();
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }
    //TODO 产生堆外内存溢出：OutofDirectMemoryError
    //1)springboot2.0以后默认使用lettuce作为操作redis的客户端，使用Netty进行网络通信，
    //2)lettuce的bug导致对外内存溢出，netty如果没有指定对外内存，就会使用Xmx300m
    //3)没有得到及时的内存释放，可以通过-Dio.netty.maxDirectMemory进行设置
    //解决方案：不能使用-Dio.netty.maxDirectMemory只去调大对外内存
    //1）升级 lettuce客户端 2）使用Jedis

    /**
     * 缓存击穿：
     * 高频热点请求失效
     * 解决方案：
     * 加锁，大量并发只让1人去查，其他人等待，查到以后释放锁，其他人获取到锁，先查缓存
     *
     * 缓存穿透：
     * 查询不存在的数据
     * 解决方案：
     * null结果缓存，加过期时间
     *
     * 缓存雪崩：
     * 大面积key集中过期
     */

    /**
     * redisTemplate:
     * lettuce、jedis操作redis的底层。Spring对前两者再次封装成redisTemplate
     *
     * @param catelogId
     * @return
     */

    @Override
    public Long[] findCatelogPath(Long catelogId) {

        List<Long> paths = new ArrayList<>();
        //只要是同一把锁，就能锁住需要这个锁的所有线程
        /**
         * springboot所有的组件在容器中都是单例的
         */
        RedisTemplate<String, Object> redisTemplate = null;
        String s = UUID.randomUUID().toString();
        Boolean lock = redisTemplate.opsForValue().setIfAbsent("lock", s, 300, TimeUnit.SECONDS);
        if (lock) {
            //设置锁的过期时间
            redisTemplate.expire("lock",30, TimeUnit.SECONDS);

            //删除锁 保证原子操作，结合lua脚本
            /*Object lockValue = redisTemplate.opsForValue().get("lock");
            if (lockValue.equals(s)) {
                redisTemplate.delete("lock");//删除锁
            }*/
            /**
             * if redis.call('get', KEYS[1]) == ARGV[1]
             *     then
             *  -- 执行删除操作
             *         return redis.call('del', KEYS[1])
             *     else
             *  -- 不成功，返回0
             *         return 0
             * end
             */

            //锁的自动续机
            try{
                //加锁成功
                List<Long> parentPath = findParentPath(catelogId, paths);
                Collections.reverse(parentPath);
                Long[] longs = parentPath.toArray(new Long[parentPath.size()]);
                return (Long[]) longs;
            } finally {
                //原子性删除锁
                String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
                redisTemplate.execute(new DefaultRedisScript<Long>(script), Arrays.asList("lock",s));

            }
        } else {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //加锁失败。。。重试
            return findCatelogPath(catelogId);
        }




    }
    //225
    private List<Long> findParentPath(Long catelogId,List<Long> paths) {
        //1.收集当前节点id
        paths.add(catelogId);
        CategoryEntity byId = this.getById(catelogId);
        if (byId.getParentCid() != 0) {
            findParentPath(byId.getParentCid(), paths);
        }
        return paths;
    }

}
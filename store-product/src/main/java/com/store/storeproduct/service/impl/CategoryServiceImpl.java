package com.store.storeproduct.service.impl;

import org.springframework.stereotype.Service;

import java.util.*;

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
     * redisTemplate:
     * lettuce、jedis操作redis的底层。Spring对前两者再次封装成redisTemplate
     *
     * @param catelogId
     * @return
     */

    @Override
    public Long[] findCatelogPath(Long catelogId) {

        List<Long> paths = new ArrayList<>();
        List<Long> parentPath = findParentPath(catelogId, paths);
        Collections.reverse(parentPath);

        return (Long[]) parentPath.toArray(new Long[parentPath.size()]);
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
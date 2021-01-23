package com.store.storeproduct;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.store.storeproduct.dao.AttrGroupDao;
import com.store.storeproduct.entity.BrandEntity;
import com.store.storeproduct.service.BrandService;

import com.store.storeproduct.service.CategoryService;
import com.store.storeproduct.vo.SpuItemAttrGroupVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class StoreProductApplicationTests {
    @Autowired
    BrandService brandService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    AttrGroupDao attrGroupDao;

    @Autowired
    RedissonClient client;

    @Test
    public void test() {
        List<SpuItemAttrGroupVo> attrgetAttrGroupWithAttrsBySpuId = attrGroupDao.getAttrgetAttrGroupWithAttrsBySpuId(13L, 225L);
        System.out.println(attrgetAttrGroupWithAttrsBySpuId);
    }
    @Test
    public void testStringRedisTemplate() {
        //hello world
        ValueOperations<String, String> stringStringValueOperations = stringRedisTemplate.opsForValue();
        //保存
        stringStringValueOperations.set("hello","world" + UUID.randomUUID().toString());
        //查询
        String hello = stringStringValueOperations.get("hello");
        System.out.println("之前保存的数据是："+hello);
    }
    @Test
    public void contextLoads() {
        /*BrandEntity brandEntity = new BrandEntity();
        brandEntity.setBrandId(1L);
        brandEntity.setDescript("华为");
        brandEntity.setName("华为");
        brandService.save(brandEntity);
        System.out.println("保存成功。。。。");*/

        BrandService brandService = this.brandService;
        List<BrandEntity> brand_id = brandService.list(new QueryWrapper<BrandEntity>().eq("brand_id", 1L));
        System.out.println("brand_id" + brand_id);
    }
    @Test
    public void testFindPath() {
        Long[] catelogPath = categoryService.findCatelogPath(225L);
        log.info("完整路径：{}", Arrays.asList(catelogPath));
    }

    @Test
    public void testRedisson() {
        System.out.println(client);
    }

}

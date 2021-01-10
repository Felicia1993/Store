package com.store.storeproduct;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.store.storeproduct.entity.BrandEntity;
import com.store.storeproduct.service.BrandService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
class StoreProductApplicationTests {
    @Autowired
    BrandService brandService;

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




}

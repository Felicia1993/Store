package com.store.storeware.service.impl;

import com.store.common.utils.R;
import com.store.storeware.exception.NoStockException;
import com.store.storeware.feign.ProductFeignService;
import com.store.storeware.vo.LockStockResult;
import com.store.storeware.vo.OrderItemVo;
import com.store.storeware.vo.SkuHasStockVo;
import com.store.storeware.vo.WareSkuLockVo;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.store.common.utils.PageUtils;
import com.store.common.utils.Query;

import com.store.storeware.dao.WareSkuDao;
import com.store.storeware.entity.WareSkuEntity;
import com.store.storeware.service.WareSkuService;
import org.springframework.transaction.annotation.Transactional;


@Service("wareSkuService")
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {
    @Autowired
    WareSkuDao wareSkuDao;
    @Autowired
    ProductFeignService productFeignService;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WareSkuEntity> page = this.page(
                new Query<WareSkuEntity>().getPage(params),
                new QueryWrapper<WareSkuEntity>()
        );

        return new PageUtils(page);
    }
    @Override
    public List<SkuHasStockVo> getSkuHasStock(List<Long> skuIds){
        List<SkuHasStockVo> collect = skuIds.stream().map(skuId -> {
            SkuHasStockVo vo = new SkuHasStockVo();
            //查询当前库存的总库存量 SELECT sum(stock-stock_locked) FROM store_wms.wms_ware_sku;
            Long count = baseMapper.getSkuStock(skuId);
            vo.setSkuId(skuId);
            vo.setHasStock(count>0);
            return vo;
        }).collect(Collectors.toList());

        return collect;
    }

    /**
     * 默认只要是运行时异常，事务都会回滚
     * @param vo
     * @return
     */
    @Transactional(rollbackFor = NoStockException.class)
    @Override
    public Boolean orderLockStock(WareSkuLockVo vo) {
        //1.按照下单的收货地址，找到一个就近仓库，锁定库存
        //1.找到每个商品，在哪个仓库都有库存
        List<OrderItemVo> locks = vo.getLocks();

        List<SkuWareHasStock> collect = locks.stream().map(item -> {
            SkuWareHasStock stock = new SkuWareHasStock();
            Long skuId = item.getSkuId();
            stock.setSkuId(skuId);
            stock.setWareNum(item.getCount());
            //查询这个商品在哪里有库存
            List<Long> wareIds = wareSkuDao.listWareIdHasSkuStock(skuId);
            return stock;
        }).collect(Collectors.toList());

        Boolean allLock = true;
        //2.锁定库存
        for (SkuWareHasStock hasStock : collect) {
            Boolean skuStock = false;
            Long skuId = hasStock.getSkuId();
            List<Long> wareIds = hasStock.getWareId();
            if (wareIds == null || wareIds.size() == 0) {
                //没有任何仓库有商品的库存
                throw new NoStockException(skuId);
            }
            for (Long wareId : wareIds) {
                //判断几行受影响确定锁没锁成功，成功就返回1，否则是0
                Long count =  wareSkuDao.lockSkuStock(skuId, wareId,hasStock.getWareNum());
                if (count == 1) {
                    skuStock = true;
                    break;
                } else {
                    //当前仓库锁失败，重试下一个仓库
                    allLock = false;
                }
            }
            if (allLock == false) {
                //当前商品所有仓库都没有锁住
                throw new NoStockException(skuId);
            }
        }
        return true;
    }
    @Data
    class SkuWareHasStock{
        private Long skuId;
        private List<Long> wareId;
        private Integer wareNum;
    }
    @Override
    public void addStock(Long skuId, Long wareId, Integer skuNum) {
        //1.判断如果还没有这个库存记录新增
        List<WareSkuEntity> entities = wareSkuDao.selectList(new QueryWrapper<WareSkuEntity>().eq("skuId", skuId));
        if (entities == null || entities.size() == 0) {
            WareSkuEntity skuEntity = new WareSkuEntity();
            skuEntity.setSkuId(skuId);
            skuEntity.setStock(skuNum);
            skuEntity.setWareId(wareId);
            skuEntity.setStockLocked(0);
            //todo: 元亨查询sku的名字，如果失败，整个事务无需回滚
            //TODO 还可以用什么办法让异常以后不会滚？
            try{
                R info = productFeignService.info(skuId);
                Map<String, Object> data = (Map<String, Object>) info.get("skuInfo");
                if (info.getCode() == 0) {
                    skuEntity.setSkuName((String) data.get("skuName"));
                }
            } catch (Exception e) {

            }
            wareSkuDao.insert(skuEntity);
        } else {
            wareSkuDao.addStock(skuId, wareId, skuNum);
        }

    }

}
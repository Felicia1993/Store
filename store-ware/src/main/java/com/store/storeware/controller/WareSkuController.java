package com.store.storeware.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.store.common.exception.BizCodeEnum;
import com.store.storeware.vo.LockStockResult;
import com.store.storeware.vo.SkuHasStockVo;
import com.store.storeware.vo.WareSkuLockVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.store.storeware.entity.WareSkuEntity;
import com.store.storeware.service.WareSkuService;
import com.store.common.utils.PageUtils;
import com.store.common.utils.R;



/**
 * ÉÌÆ·¿â´æ
 *
 * @author xieqiqi
 * @email 359468250@qq.com
 * @date 2021-01-10 16:46:40
 */
@RestController
@RequestMapping("storeware/waresku")
public class WareSkuController {
    @Autowired
    private WareSkuService wareSkuService;
    @PostMapping("/lock/order")
    public R orderLockStock(@RequestBody WareSkuLockVo vo) {
        try {
            Boolean stock =  wareSkuService.orderLockStock(vo);
        } catch (Exception e) {
            return R.error(BizCodeEnum.NO_STOCK_EXCEPTION.getCode(), BizCodeEnum.NO_STOCK_EXCEPTION.getMsg());
        }
        return R.ok();
    }

    /**
     * 查询sku是否有库存
     */
    @PostMapping("/hasstock")
    public R<List<SkuHasStockVo>> getSkuHasStock(@RequestBody  List<Long>skuIds) {
        //sku_id,stock
        List<SkuHasStockVo> vos= wareSkuService.getSkuHasStock(skuIds);
        R ok = R.ok();
        ok.setData(vos);
        return ok;
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R<PageUtils> list(@RequestParam Map<String, Object> params){
        PageUtils page = wareSkuService.queryPage(params);
        R ok = R.ok();
        ok.setData(page);
        return ok;
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		WareSkuEntity wareSku = wareSkuService.getById(id);

        return R.ok().put("wareSku", wareSku);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody WareSkuEntity wareSku){
		wareSkuService.save(wareSku);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody WareSkuEntity wareSku){
		wareSkuService.updateById(wareSku);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		wareSkuService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}

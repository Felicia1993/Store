package com.store.storeorder.service.impl;

import com.store.storeauthserver.vo.MemberRespVo;
import com.store.storeorder.feign.MemberFeignServie;
import com.store.storeorder.interceptor.LoginUserInterceptor;
import com.store.storeorder.vo.MemberAddressVo;
import com.store.storeorder.vo.OrderConfirmVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.store.common.utils.PageUtils;
import com.store.common.utils.Query;

import com.store.storeorder.dao.OrderDao;
import com.store.storeorder.entity.OrderEntity;
import com.store.storeorder.service.OrderService;


@Service("orderService")
public class OrderServiceImpl extends ServiceImpl<OrderDao, OrderEntity> implements OrderService {
    @Autowired
    MemberFeignServie memberFeignService;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OrderEntity> page = this.page(
                new Query<OrderEntity>().getPage(params),
                new QueryWrapper<OrderEntity>()
        );

        return new PageUtils(page);
    }
    @Override
    public OrderConfirmVo confirmOrder(){
        OrderConfirmVo confirmVo = new OrderConfirmVo();
        MemberRespVo memberRespVo = LoginUserInterceptor.loginUser.get();
        //1.远程查询所有的收货地址列表
        List<MemberAddressVo> address = memberFeignService.getAddress(memberRespVo.getId());
        //2.远程查询购物车选中的购物项

        return confirmVo;
    }

}
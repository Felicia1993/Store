package com.store.storeorder.service.impl;

import com.store.storeauthserver.vo.MemberRespVo;
import com.store.storeorder.feign.CartFeignService;
import com.store.storeorder.feign.MemberFeignServie;
import com.store.storeorder.interceptor.LoginUserInterceptor;
import com.store.storeorder.vo.MemberAddressVo;
import com.store.storeorder.vo.OrderConfirmVo;
import com.store.storeorder.vo.OrderItemVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.store.common.utils.PageUtils;
import com.store.common.utils.Query;

import com.store.storeorder.dao.OrderDao;
import com.store.storeorder.entity.OrderEntity;
import com.store.storeorder.service.OrderService;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;


@Service("orderService")
public class OrderServiceImpl extends ServiceImpl<OrderDao, OrderEntity> implements OrderService {
    @Autowired
    MemberFeignServie memberFeignService;
    @Autowired
    CartFeignService cartFeignService;
    @Autowired
    ThreadPoolExector executor;

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
        //异步调用丢失请求头的问题，每一个线程都要共享之前的请求数据
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        CompletableFuture<void> getAddressFuture = CompletableFuture.runAsync(()->{
            RequestContextHolder.setRequestAttributes(requestAttributes);
            //1.远程查询所有的收货地址列表
            List<MemberAddressVo> address = memberFeignService.getAddress(memberRespVo.getId());
            confirmVo.setAddress(address);
        }, executor);
        CompletableFuture<void> getCartItemsfuture = CompletableFuture.runAsync(()->{
            RequestContextHolder.setRequestAttributes(requestAttributes);
            //2.远程查询购物车选中的购物项
            List<OrderItemVo> currentUserCartItems = cartFeignService.getCurrentUserCartItems();
            confirmVo.setItems(currentUserCartItems);
        },executor);

        //feign在远程调用前要构造请求，调用很多的拦截器
        //3.查询用户积分
        String integration = memberRespVo.getIntegration();

        //4.其他数据自动计算
        //todo:5.防重令牌
        CompletableFuture.allOf(getAddressFuture,getCartItemsfuture ).get();
        return confirmVo;
    }

}
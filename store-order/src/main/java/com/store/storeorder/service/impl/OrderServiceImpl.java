package com.store.storeorder.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.store.common.constant.ProductConstant;
import com.store.common.utils.R;
import com.store.storeauthserver.vo.MemberRespVo;
import com.store.storeorder.contant.OrderConstant;
import com.store.storeorder.entity.OrderItemEntity;
import com.store.storeorder.feign.CartFeignService;
import com.store.storeorder.feign.MemberFeignServie;
import com.store.storeorder.feign.ProductFeignService;
import com.store.storeorder.feign.WmsFeignService;
import com.store.storeorder.interceptor.LoginUserInterceptor;
import com.store.storeorder.service.OrderItemService;
import com.store.storeorder.to.OrderCreateTo;
import com.store.storeorder.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.store.common.utils.PageUtils;
import com.store.common.utils.Query;

import com.store.storeorder.dao.OrderDao;
import com.store.storeorder.entity.OrderEntity;
import com.store.storeorder.service.OrderService;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;


@Service("orderService")
public class OrderServiceImpl extends ServiceImpl<OrderDao, OrderEntity> implements OrderService {
    @Autowired
    MemberFeignServie memberFeignService;
    @Autowired
    CartFeignService cartFeignService;
    @Autowired
    ThreadPoolExecutor executor;
    @Autowired
    StringRedisTemplate redisTemplate;
    @Autowired
    MemberRespVo memberRespVo;
    @Autowired
    WmsFeignService wmsFeignService;
    @Autowired
    ProductFeignService productFeignService;


    private ThreadLocal<OrderSubmitVo> confirmVoThreadLocal = new ThreadLocal<>();

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OrderEntity> page = this.page(
                new Query<OrderEntity>().getPage(params),
                new QueryWrapper<OrderEntity>()
        );

        return new PageUtils(page);
    }
    @Override
    public OrderConfirmVo confirmOrder() throws ExecutionException, InterruptedException {
        OrderConfirmVo confirmVo = new OrderConfirmVo();
        MemberRespVo memberRespVo = LoginUserInterceptor.loginUser.get();
        //异步调用丢失请求头的问题，每一个线程都要共享之前的请求数据
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        CompletableFuture<Void> getAddressFuture = CompletableFuture.runAsync(()->{
            RequestContextHolder.setRequestAttributes(requestAttributes);
            //1.远程查询所有的收货地址列表
            List<MemberAddressVo> address = memberFeignService.getAddress(memberRespVo.getId());
            confirmVo.setAddress(address);
        }, executor);
        CompletableFuture<Void> getCartItemsfuture = CompletableFuture.runAsync(()->{
            RequestContextHolder.setRequestAttributes(requestAttributes);
            //2.远程查询购物车选中的购物项
            List<OrderItemVo> currentUserCartItems = cartFeignService.getCurrentUserCartItems();
            confirmVo.setItems(currentUserCartItems);
        },executor);

        //feign在远程调用前要构造请求，调用很多的拦截器
        //3.查询用户积分
        String integration = memberRespVo.getIntegration();

        //4.其他数据自动计算
        // todo:5.防重令牌
        String token = UUID.randomUUID().toString().replace("-", "");
        redisTemplate.opsForValue().set(OrderConstant.USER_ORDER_TOKEN_PREFIX + memberRespVo.getId(), token, 30, TimeUnit.MINUTES);
        confirmVo.setOrderToken(token);
        CompletableFuture.allOf(getAddressFuture,getCartItemsfuture ).get();
        return confirmVo;
    }
    @Override
    public SubmitOrderRespVo submitOrder(OrderSubmitVo vo) {
        confirmVoThreadLocal.set(vo);
        SubmitOrderRespVo respVo = new SubmitOrderRespVo();

        //1.下单、创建订单、验令牌，验证价格，锁库存。。。
        String script = "if redis.call('get',KEYS[1]) == ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";
        //0令牌失败 1 删除成功
        Long result = redisTemplate.execute(new DefaultRedisScript<Long>(script, Long.class), Arrays.asList(OrderConstant.USER_ORDER_TOKEN_PREFIX + memberRespVo.getId()));
        if (result == 0L) {
            //令牌验证失败
            respVo.setCode(1);
            return respVo;
        } else {
            //令牌验证成功
            //1.下单 创建订单
            // ，验令牌 验价格 锁库存
            OrderCreateTo order = createOrder();
        }
        /*String orderToken = vo.getOrderToken();
        String redisToken = redisTemplate.opsForValue().get(OrderConstant.USER_ORDER_TOKEN_PREFIX + memberRespVo.getId());
        if (orderToken != null && orderToken.equals(redisToken)) {
            //令牌验证通过
            redisTemplate.delete(OrderConstant.USER_ORDER_TOKEN_PREFIX + memberRespVo.getId());
        } else {
            return respVo;
        }*/
        return null;
    }

    private OrderCreateTo createOrder() {
        OrderCreateTo createTo = new OrderCreateTo();
        //1.生成订单号
        String orderSn = IdWorker.getTimeId();
        OrderEntity orderEntity = buildOrder(orderSn);
        //2.获取到所有的订单项
        List<OrderItemEntity> orderItemEntities = buildOrderItems(orderSn);
        //3.计算价格相关
        computePrice(orderEntity, orderItemEntities);
        return createTo;
    }

    private void computePrice(OrderEntity orderEntity, List<OrderItemEntity> orderItemEntities) {
        //1.订单相关的价格
        for (OrderItemEntity orderItemEntity : orderItemEntities) {
            BigDecimal multiply = orderItemEntity.getSkuPrice().multiply(new BigDecimal(orderItemEntity.getSkuQuantity().toString()));

        }
    }

    private List<OrderItemEntity> buildOrderItems(String orderSn) {
        //最后确定每个订单项的总价格
        List<OrderItemVo> currentUserCartItems = cartFeignService.getCurrentUserCartItems();
        if (currentUserCartItems != null && currentUserCartItems.size() > 0) {
            List<OrderItemEntity> itemEntities = currentUserCartItems.stream().map(cartItem -> {
                OrderItemEntity itemEntity = buildOrderItem(cartItem);
                itemEntity.setOrderSn(orderSn);
                return itemEntity;
            }).collect(Collectors.toList());
            return itemEntities;
        }
        return null;
    }

    /**
     * 构建某一个订单项
     * @return
     */


    private OrderEntity buildOrder(String orderSn) {

        OrderEntity orderEntity = new OrderEntity();
        //获取收货地址信息
        R fare = wmsFeignService.getFare(confirmVoThreadLocal.get().getAddrId());
        FareVo fareResp = (FareVo) fare.getData(new TypeReference<FareVo>() {
        });
        //设置运费信息
        orderEntity.setFreightAmount(fareResp.getFare());
        //设置收货人信息
        orderEntity.setReceiverCity(fareResp.getAddress().getCity());
        orderEntity.setReceiverName(fareResp.getAddress().getName());
        orderEntity.setReceiverPostCode(fareResp.getAddress().getPostCode());
        orderEntity.setReceiverProvince(fareResp.getAddress().getProvince());
        orderEntity.setReceiverRegion(fareResp.getAddress().getRegion());
        return orderEntity;
    }

    //构建订单项的数据
    private OrderItemEntity buildOrderItem(OrderItemVo cartItem) {
        OrderItemEntity itemEntity = new OrderItemEntity();
        ///1.订单信息，订单号
        //2.商品的sku信息
        itemEntity.setSkuId(cartItem.getSkuId());
        itemEntity.setSkuName(cartItem.getTitle());
        itemEntity.setSkuPic(cartItem.getImage());
        itemEntity.setSkuPrice(cartItem.getPrice());
        String skuAttr = StringUtils.collectionToDelimitedString(cartItem.getSkuAttr(), ";");
        itemEntity.setSkuAttrsVals(skuAttr);
        itemEntity.setSkuQuantity(cartItem.getCount());
        //3.商品的spu信息
        Long skuId = cartItem.getSkuId();
        R spuInfo = productFeignService.getSpuInfoBySkuId(skuId);
        SpuInfoVo spu = (SpuInfoVo) spuInfo.getData(new TypeReference<SpuInfoVo>() {
        });
        itemEntity.setSpuId(spu.getId());
        itemEntity.setSpuBrand(spu.getBrandId().toString());
        itemEntity.setSpuName(spu.getSpuName());
        itemEntity.setCategoryId(spu.getCatalogId());

        //4.优惠信息
        //5.积分信息
        itemEntity.setGiftGrowth(cartItem.getPrice().intValue());
        itemEntity.setGiftIntegration(cartItem.getPrice().intValue());

        return itemEntity;
    }
}
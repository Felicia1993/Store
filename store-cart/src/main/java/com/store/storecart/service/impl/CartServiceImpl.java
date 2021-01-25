package com.store.storecart.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.store.common.utils.R;
import com.store.storecart.feign.ProductFeignService;
import com.store.storecart.interceptor.CartInterceptor;
import com.store.storecart.service.CartService;
import com.store.storecart.vo.Cart;
import com.store.storecart.vo.CartItem;
import com.store.storecart.vo.SkuInfoVo;
import com.store.storecart.vo.UserInfoTo;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CartServiceImpl implements CartService {
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    StringRedisTemplate redisTemplate;
    @Autowired
    ProductFeignService productFeignService;
    @Autowired
    ThreadPoolExecutor threadPoolExecutor;

    private final String CART_PREFIX = "store:cart:";
    @Override
    public CartItem addToCart(Long skuId, Integer num) throws ExecutionException, InterruptedException {
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();

        String res = (String) cartOps.get(skuId.toString());
        //2.添加新商品到购物车
        if (StringUtils.isEmpty(res)) {
            CartItem cartItem = new CartItem();
            CompletableFuture<Void> getSkuInfoTask = CompletableFuture.runAsync(() -> {
                //1.远程查询当前要添加商品的信息
                R skuInfo = productFeignService.getSkuInfo(skuId);
                SkuInfoVo data = (SkuInfoVo) skuInfo.getData("skuInfo", new TypeReference<SkuInfoVo>() {
                });
                cartItem.setCheck(true);
                cartItem.setCount(1);
                cartItem.setImage(data.getSkuDefaultImg());
                cartItem.setTitle(data.getSkuTitle());
                cartItem.setSkuId(skuId);
                cartItem.setPrice(data.getPrice());
            }, threadPoolExecutor);

            //2.远程查询sku组合信息
            CompletableFuture<Void> getSkuSaleVttrValues = CompletableFuture.runAsync(() -> {
                List<String> skuSaleAttrValues = productFeignService.getSkuSaleAttrValues(skuId);
                cartItem.setSkuAttr(skuSaleAttrValues);
            }, threadPoolExecutor);

            CompletableFuture.allOf(getSkuInfoTask, getSkuSaleVttrValues).get();
            String jsonString = JSON.toJSONString(cartItem);
            cartOps.put(skuId.toString(), jsonString);
            return cartItem;
        } else {
            //购物车有此商品，修改数量
            CartItem item = JSON.parseObject(res, CartItem.class);
            item.setCount(item.getCount() + num);
            cartOps.put(skuId.toString(), JSON.toJSONString(item));
            return item;
        }
    }

    @Override
    public CartItem getCartItem(Long skuId) {
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        String s = (String) cartOps.get(skuId.toString());
        CartItem cartItem = JSON.parseObject(s, CartItem.class);
        return cartItem;
    }

    /**
     * 获取到要操作的购物车
     * @return
     */
    private BoundHashOperations<String, Object, Object> getCartOps() {
        UserInfoTo userInfoTo = CartInterceptor.threadLocal.get();
        String cartKey = "";
        if (userInfoTo.getUserId() != null) {
            cartKey = CART_PREFIX + userInfoTo.getUserId();
        } else {
            cartKey = CART_PREFIX + userInfoTo.getUserKey();
        }
        // redisTemplate.opsForHash().get(cartKey, "1");
        BoundHashOperations<String, Object, Object> stringObjectObjectBoundHashOperations = redisTemplate.boundHashOps(cartKey);
        return stringObjectObjectBoundHashOperations;
    }

    @Override
    public List<CartItem> getUserCartItems(){
        UserInfoTo userInfoTo= CartInterceptor.threadLocal.get();
        if (userInfoTo.getUserId() == null) {
            return null;
        } else {
            String cartKey = CART_PREFIX + userInfoTo.getUserId();
         //   List<CartItem> cartItem = getCartItems(cartKey);
            //获取所有被选中的购物项

        }
        return null;
    }
    @Override
    public Cart getCart() throws ExecutionException, InterruptedException {
        Cart cart = new Cart();
        UserInfoTo userInfoTo = CartInterceptor.threadLocal.get();
        if (userInfoTo.getUserId() != null) {
            //1.登录
            String userKey = CART_PREFIX + userInfoTo.getUserId();
            BoundHashOperations<String, Object, Object> hashOps = redisTemplate.boundHashOps(userKey);
            //2.如果临时购物车的数据还没有进行合并
            List<CartItem> tempCartItems = getCartItems(CART_PREFIX + userInfoTo.getUserKey());
            if (tempCartItems != null) {
                //有数据 需要合并
                for(CartItem item : tempCartItems) {
                    addToCart(item.getSkuId(), item.getCount());
                }
                //清除临时购物车的数据
                clearCart(CART_PREFIX + userInfoTo.getUserKey());
            }
            //3.获取登录后的购物车数据
            List<CartItem> cartItemList = getCartItems(userKey);
            cart.setItems(cartItemList);
        } else {
            //2.没登录
            String userKey = CART_PREFIX + userInfoTo.getUserKey();
            //获取临时购物车的所有购物项
            List<CartItem> cartItems = getCartItems(userKey);
            cart.setItems(cartItems);
        }
        return cart;
    }

    private List<CartItem> getCartItems(String cartKey) {

        BoundHashOperations<String, Object, Object> hashOps = redisTemplate.boundHashOps(cartKey);
        List<Object> values = hashOps.values();
        if (values != null && values.size() > 0) {
            List<CartItem> collect = values.stream().map(obj -> {
                String str = (String) obj;
                CartItem cartItem = JSON.parseObject(str, CartItem.class);
                return cartItem;
            }).collect(Collectors.toList());
            return  collect;
        }
        return null;
    }

    @Override
    public void clearCart(String cartKey) {
        redisTemplate.delete(cartKey);
    }
}

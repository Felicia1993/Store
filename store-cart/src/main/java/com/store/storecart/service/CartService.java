package com.store.storecart.service;

import com.store.storecart.vo.Cart;
import com.store.storecart.vo.CartItem;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface CartService {

    List<CartItem> getUserCartItems();

    CartItem addToCart(Long skuId, Integer num) throws ExecutionException, InterruptedException;

    CartItem  getCartItem(Long skuId);

    Cart getCart() throws ExecutionException, InterruptedException;

    void clearCart(String cartKey);
}

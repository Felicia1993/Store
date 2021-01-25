package com.store.storecart.service;

import com.store.storecart.vo.CartItem;

import java.util.List;

public interface CartService {

    List<CartItem> getUserCartItems();
}

package com.store.storecart.controller;

import com.store.storecart.interceptor.CartInterceptor;
import com.store.storecart.service.CartService;
import com.store.storecart.vo.CartItem;
import com.store.storecart.vo.UserInfoTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import com.store.common.constant.AuthServerConstant;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class CartController {
    @Autowired
    CartService cartService;

    /**
     * 浏览器有一个cookie：user-key标识用户身份，一个月过期，如果第一次使用jd的购物车功能，
     * 都会给一个临时用户身份，浏览器以后保存，每次访问都会带上
     *
     * 登录：session有
     * 未登录：cookie user-key
     * 第一次如果没有临时用户，帮忙创建临时用户
     * @param session
     * @return
     */
    @GetMapping("/cart.html")
    public String cartListPage(HttpSession session) {
        //1.快速得到用户信息
        UserInfoTo userInfoTo = CartInterceptor.threadLocal.get();
        System.out.println("userInfoTo");
        Object attribute = session.getAttribute(AuthServerConstant.StatusEnum.LOGIN_USER);
        if(attribute != null) {

        } else {

        }
        return "cartList";
    }

    @GetMapping("/currentUserCartItems")
    public List<CartItem> getCurrentUserCartItems() {
        return cartService.getUserCartItems();
    }
}

package com.store.storecart.controller;

import com.store.storecart.interceptor.CartInterceptor;
import com.store.storecart.service.CartService;
import com.store.storecart.vo.Cart;
import com.store.storecart.vo.CartItem;
import com.store.storecart.vo.UserInfoTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.store.common.constant.AuthServerConstant;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.concurrent.ExecutionException;

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
     * @return
     */
    @GetMapping("/cart.html")
    public String cartListPage(Model model) throws ExecutionException, InterruptedException {
        //1.快速得到用户信息
    //    UserInfoTo userInfoTo = CartInterceptor.threadLocal.get();
        System.out.println("userInfoTo");
        Cart cart = cartService.getCart();
        model.addAttribute(cart);
        return "cartList";
    }

    /**
     * RedirectAttributes
     * addFlashAttribute将数据放入session，可以在页面取出，但是只能取一次
     * addAttribute 将数据放在url中
     *
     * @param skuId
     * @param num
     * @param redirectAttributes
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @GetMapping("/addToCart")
    public String addToCart(@RequestParam("skuId") Long skuId, @RequestParam("num") Integer num, RedirectAttributes redirectAttributes) throws ExecutionException, InterruptedException {
       CartItem cartItem = cartService.addToCart(skuId, num);
        redirectAttributes.addAttribute("skuId", skuId);
        return "redirect:http://store.com/addToCartSuccess.html";
    }
    @GetMapping("/addToCartSuccess.html")
    public String addToCartSuccessPage(@RequestParam("skuId") Long skuId, Model model) {
        //重定向到成功页面，再次查询购物车即可
        CartItem cartItem = cartService.getCartItem(skuId);
        model.addAttribute("item", cartItem);
        return "success";
    }

    @GetMapping("/currentUserCartItems")
    @ResponseBody
    public List<CartItem> getCurrentUserCartItems() {
        return cartService.getUserCartItems();
    }
}

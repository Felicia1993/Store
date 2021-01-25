package com.store.storeorder.web;

import com.store.storeorder.service.OrderService;
import com.store.storeorder.vo.OrderConfirmVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OrderWebController {
    @Autowired
    OrderService orderService;
    @GetMapping("/toTrade")
    public String toTrade(Model model){
       OrderConfirmVo confirmVo =  orderService.confirmOrder();
       model.addAttribute("orderConfirmData", confirmVo);
        return "confirm";
    }


}

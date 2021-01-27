package com.store.storeorder.web;

import com.store.storeorder.service.OrderService;
import com.store.storeorder.vo.OrderConfirmVo;
import com.store.storeorder.vo.OrderSubmitVo;
import com.store.storeorder.vo.SubmitOrderRespVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.concurrent.ExecutionException;

@Controller
public class OrderWebController {
    @Autowired
    OrderService orderService;
    @GetMapping("/toTrade")
    public String toTrade(Model model) throws ExecutionException, InterruptedException {
       OrderConfirmVo confirmVo =  orderService.confirmOrder();
       model.addAttribute("orderConfirmData", confirmVo);
        return "confirm";
    }
    @PostMapping("/submitOrder")
    public String submitOrder(OrderSubmitVo vo, Model model) {
        SubmitOrderRespVo responsevo =  orderService.submitOrder(vo);
        if (responsevo.getCode() == 0) {
            model.addAttribute("submitOrderResp", responsevo);
            return "pay";
        } else {
            return "redirect:http://store.com/toTrade";
        }
        //下单失败回到订单确认页重新确认订单信息
    }

}

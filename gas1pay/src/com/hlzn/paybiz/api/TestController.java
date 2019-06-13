package com.hlzn.paybiz.api;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.hlzn.entity.GasPayOrder;
import com.hlzn.paybiz.biz.PayService;

/**
 * TEST
 */
@Controller
@RequestMapping("/test")
public class TestController {
    
    @Autowired
    private PayService payService;

    @RequestMapping(value="/list.do", method=RequestMethod.GET)
    public String listOrders(Model model) {
        model.addAttribute("order", new GasPayOrder());
        List<GasPayOrder> orders = payService.listOrders();
        model.addAttribute("orders", orders);
        return "/test/list.jsp";
    }
    
    @RequestMapping(value="/insert.do", method=RequestMethod.POST)
    public String addOrder(GasPayOrder order) {
        payService.createOrder(order);
        return "redirect:/test/list.do";
    }
    
    @RequestMapping(value="/{id}/delete.do", method=RequestMethod.GET)
    public String delOrder(@PathVariable Serializable id) {
        if(id != null) {
            payService.deleteOrder(id);
        }
        return "redirect:/test/list.do";
    }
    
    @RequestMapping(value="/{id}/edit.do", method=RequestMethod.GET)
    public String editOrder(@PathVariable Serializable id, Model model) {
        GasPayOrder order = payService.getOrderById(id);
        model.addAttribute("order", order);
        return "/test/edit.jsp";
    }
    
    @RequestMapping(value="/{id}/update.do", method=RequestMethod.POST)
    public String editOrder(@PathVariable Serializable id, GasPayOrder order) {
        GasPayOrder e = new GasPayOrder();
        e.setId((String)id);
        e.setCompanyCode(order.getCompanyCode());
        e.setChargeFee(order.getChargeFee());
        e.setOrderTime(order.getOrderTime());
        payService.updateOrder(e);
        return "redirect:/test/list.do";
    }
    
    @RequestMapping(value="/select.do", method=RequestMethod.POST)
    public String selectOrder(@RequestParam String orderId, Model model) {
        GasPayOrder order = payService.getOrderById(orderId);
        if(order != null) {
            List<GasPayOrder> orders = new ArrayList<GasPayOrder>();
            orders.add(order);
            model.addAttribute("orders", orders);
        }
        model.addAttribute("order", new GasPayOrder());
        return "/test/list.jsp";
    }
    
}
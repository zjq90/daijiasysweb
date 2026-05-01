
package com.daijia.controller;

import com.daijia.entity.AppUser;
import com.daijia.entity.Order;
import com.daijia.service.AppUserService;
import com.daijia.service.OrderService;
import com.daijia.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/test")
public class TestController {
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private AppUserService appUserService;
    
    private boolean checkLogin(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }
    
    @GetMapping("/placeOrder")
    public String placeOrderPage(Model model, HttpSession session) {
        if (!checkLogin(session)) {
            return "redirect:/login";
        }
        List<AppUser> users = appUserService.findAll();
        model.addAttribute("users", users);
        return "test/placeOrder";
    }
    
    @PostMapping("/placeOrder")
    public String placeOrder(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String startAddress,
            @RequestParam(required = false) String endAddress,
            @RequestParam(required = false) BigDecimal startLat,
            @RequestParam(required = false) BigDecimal startLng,
            @RequestParam(required = false) BigDecimal endLat,
            @RequestParam(required = false) BigDecimal endLng,
            Model model,
            HttpSession session) {
        
        if (!checkLogin(session)) {
            return "redirect:/login";
        }
        
        if (userId == null) {
            model.addAttribute("error", "请选择下单用户");
            List<AppUser> users = appUserService.findAll();
            model.addAttribute("users", users);
            return "test/placeOrder";
        }
        
        AppUser user = appUserService.findById(userId).orElse(null);
        if (user == null) {
            model.addAttribute("error", "用户不存在");
            List<AppUser> users = appUserService.findAll();
            model.addAttribute("users", users);
            return "test/placeOrder";
        }
        
        if (!ValidationUtil.isValidAddress(startAddress)) {
            model.addAttribute("error", "出发地地址不能为空，长度2-200位");
            List<AppUser> users = appUserService.findAll();
            model.addAttribute("users", users);
            return "test/placeOrder";
        }
        
        if (!ValidationUtil.isValidAddress(endAddress)) {
            model.addAttribute("error", "目的地地址不能为空，长度2-200位");
            List<AppUser> users = appUserService.findAll();
            model.addAttribute("users", users);
            return "test/placeOrder";
        }
        
        if (!ValidationUtil.isValidLatitude(startLat)) {
            model.addAttribute("error", "出发地纬度必须在-90到90之间");
            List<AppUser> users = appUserService.findAll();
            model.addAttribute("users", users);
            return "test/placeOrder";
        }
        
        if (!ValidationUtil.isValidLongitude(startLng)) {
            model.addAttribute("error", "出发地经度必须在-180到180之间");
            List<AppUser> users = appUserService.findAll();
            model.addAttribute("users", users);
            return "test/placeOrder";
        }
        
        if (!ValidationUtil.isValidLatitude(endLat)) {
            model.addAttribute("error", "目的地纬度必须在-90到90之间");
            List<AppUser> users = appUserService.findAll();
            model.addAttribute("users", users);
            return "test/placeOrder";
        }
        
        if (!ValidationUtil.isValidLongitude(endLng)) {
            model.addAttribute("error", "目的地经度必须在-180到180之间");
            List<AppUser> users = appUserService.findAll();
            model.addAttribute("users", users);
            return "test/placeOrder";
        }
        
        Order order = new Order();
        order.setUser(user);
        order.setStartAddress(startAddress);
        order.setEndAddress(endAddress);
        order.setStartLat(startLat);
        order.setStartLng(startLng);
        order.setEndLat(endLat);
        order.setEndLng(endLng);
        
        orderService.createOrder(order);
        
        return "redirect:/order/pending";
    }
    
    @GetMapping("/generateTestOrders")
    public String generateTestOrders(HttpSession session) {
        if (!checkLogin(session)) {
            return "redirect:/login";
        }
        
        List<AppUser> users = appUserService.findAll();
        if (users.isEmpty()) {
            session.setAttribute("error", "没有可用的用户");
            return "redirect:/order/list";
        }
        
        String[][] testAddresses = {
            {"北京市朝阳区建国路88号", "北京市海淀区中关村大街1号", "39.9087", "116.4341", "39.9847", "116.3046"},
            {"北京市东城区王府井大街88号", "北京市西城区西单北大街120号", "39.9153", "116.4128", "39.9087", "116.3711"},
            {"北京市丰台区丰台路88号", "北京市石景山区古城大街1号", "39.8587", "116.2881", "39.9042", "116.2228"},
            {"北京市通州区新华大街1号", "北京市大兴区黄村东大街1号", "39.9042", "116.6570", "39.7289", "116.3388"}
        };
        
        for (int i = 0; i < testAddresses.length && i < users.size(); i++) {
            String[] addr = testAddresses[i];
            AppUser user = users.get(i);
            
            Order order = new Order();
            order.setUser(user);
            order.setStartAddress(addr[0]);
            order.setEndAddress(addr[1]);
            order.setStartLat(new BigDecimal(addr[2]));
            order.setStartLng(new BigDecimal(addr[3]));
            order.setEndLat(new BigDecimal(addr[4]));
            order.setEndLng(new BigDecimal(addr[5]));
            
            orderService.createOrder(order);
        }
        
        return "redirect:/order/pending";
    }
}

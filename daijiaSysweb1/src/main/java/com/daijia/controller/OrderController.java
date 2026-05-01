
package com.daijia.controller;

import com.daijia.entity.Driver;
import com.daijia.entity.Order;
import com.daijia.service.DriverService;
import com.daijia.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private DriverService driverService;
    
    private boolean checkLogin(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }
    
    @GetMapping("/list")
    public String list(Model model, HttpSession session) {
        if (!checkLogin(session)) {
            return "redirect:/login";
        }
        List<Order> list = orderService.findAll();
        model.addAttribute("orders", list);
        return "order/list";
    }
    
    @GetMapping("/pending")
    public String pendingList(Model model, HttpSession session) {
        if (!checkLogin(session)) {
            return "redirect:/login";
        }
        List<Order> list = orderService.findPendingOrders();
        List<Driver> availableDrivers = driverService.findAvailableDrivers();
        model.addAttribute("orders", list);
        model.addAttribute("availableDrivers", availableDrivers);
        return "order/pending";
    }
    
    @GetMapping("/view/{id}")
    public String view(@PathVariable Long id, Model model, HttpSession session) {
        if (!checkLogin(session)) {
            return "redirect:/login";
        }
        Order order = orderService.findById(id).orElse(null);
        List<Driver> availableDrivers = driverService.findAvailableDrivers();
        model.addAttribute("order", order);
        model.addAttribute("availableDrivers", availableDrivers);
        return "order/view";
    }
    
    @GetMapping("/autoDispatch/{id}")
    public String autoDispatch(@PathVariable Long id, HttpSession session) {
        if (!checkLogin(session)) {
            return "redirect:/login";
        }
        try {
            orderService.autoDispatch(id);
        } catch (Exception e) {
            session.setAttribute("error", e.getMessage());
        }
        return "redirect:/order/pending";
    }
    
    @PostMapping("/manualDispatch")
    public String manualDispatch(@RequestParam Long orderId, @RequestParam Long driverId, HttpSession session) {
        if (!checkLogin(session)) {
            return "redirect:/login";
        }
        try {
            orderService.manualDispatch(orderId, driverId);
        } catch (Exception e) {
            session.setAttribute("error", e.getMessage());
        }
        return "redirect:/order/pending";
    }
    
    @GetMapping("/startService/{id}")
    public String startService(@PathVariable Long id, HttpSession session) {
        if (!checkLogin(session)) {
            return "redirect:/login";
        }
        try {
            orderService.startService(id);
        } catch (Exception e) {
            session.setAttribute("error", e.getMessage());
        }
        return "redirect:/order/list";
    }
    
    @GetMapping("/endService/{id}")
    public String endService(@PathVariable Long id, HttpSession session) {
        if (!checkLogin(session)) {
            return "redirect:/login";
        }
        try {
            orderService.endService(id);
        } catch (Exception e) {
            session.setAttribute("error", e.getMessage());
        }
        return "redirect:/order/list";
    }
    
    @GetMapping("/cancel/{id}")
    public String cancel(@PathVariable Long id, HttpSession session) {
        if (!checkLogin(session)) {
            return "redirect:/login";
        }
        try {
            orderService.cancelOrder(id);
        } catch (Exception e) {
            session.setAttribute("error", e.getMessage());
        }
        return "redirect:/order/list";
    }
    
    @GetMapping("/pay/{id}")
    public String pay(@PathVariable Long id, HttpSession session) {
        if (!checkLogin(session)) {
            return "redirect:/login";
        }
        try {
            orderService.payOrder(id);
        } catch (Exception e) {
            session.setAttribute("error", e.getMessage());
        }
        return "redirect:/order/list";
    }
}

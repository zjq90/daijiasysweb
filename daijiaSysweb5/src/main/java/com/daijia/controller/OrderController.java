package com.daijia.controller;

import com.daijia.entity.Order;
import com.daijia.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 订单控制器
 * 
 * 处理订单管理相关功能
 * 
 * @author daijia
 * @version 1.0.0
 */
@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 订单列表页面
     * 
     * @param model 模型对象
     * @param session 会话对象
     * @param page 页码
     * @param size 每页大小
     * @param status 状态筛选
     * @return 订单列表视图
     */
    @GetMapping("/list")
    public String list(Model model, HttpSession session,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "10") int size,
                       @RequestParam(required = false) Integer status) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/login";
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("createTime").descending());
        Page<Order> orderPage;
        
        if (status != null) {
            orderPage = orderService.findAll(pageable);
        } else {
            orderPage = orderService.findAll(pageable);
        }

        model.addAttribute("orderPage", orderPage);
        model.addAttribute("orderService", orderService);
        model.addAttribute("currentPage", "order");
        model.addAttribute("status", status);

        return "order/list";
    }

    /**
     * 订单详情页面
     * 
     * @param id 订单ID
     * @param model 模型对象
     * @param session 会话对象
     * @return 订单详情视图
     */
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id, Model model, HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/login";
        }

        Optional<Order> orderOpt = orderService.findById(id);
        if (orderOpt.isPresent()) {
            model.addAttribute("order", orderOpt.get());
            model.addAttribute("orderService", orderService);
        } else {
            model.addAttribute("error", "订单不存在");
        }

        model.addAttribute("currentPage", "order");
        return "order/detail";
    }

    /**
     * 取消订单
     * 
     * @param id 订单ID
     * @param reason 取消原因
     * @param session 会话对象
     * @return JSON结果
     */
    @PostMapping("/cancel")
    @ResponseBody
    public Map<String, Object> cancel(@RequestParam Long id,
                                        @RequestParam(required = false) String reason,
                                        HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        
        if (session.getAttribute("admin") == null) {
            result.put("success", false);
            result.put("message", "未登录");
            return result;
        }

        Order order = orderService.cancelOrder(id, reason != null ? reason : "管理员取消");
        
        if (order != null) {
            result.put("success", true);
            result.put("message", "订单已取消");
        } else {
            result.put("success", false);
            result.put("message", "订单取消失败");
        }
        
        return result;
    }

    /**
     * 获取订单状态统计
     * 
     * @param session 会话对象
     * @return JSON结果
     */
    @GetMapping("/statistics")
    @ResponseBody
    public Map<String, Object> statistics(HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        
        if (session.getAttribute("admin") == null) {
            result.put("success", false);
            return result;
        }

        result.put("success", true);
        result.put("pending", orderService.countByStatus(0));
        result.put("accepted", orderService.countByStatus(1));
        result.put("active", orderService.countByStatus(3));
        result.put("completed", orderService.countByStatus(4));
        result.put("cancelled", orderService.countByStatus(5));
        
        return result;
    }
}

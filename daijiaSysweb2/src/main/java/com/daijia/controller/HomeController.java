package com.daijia.controller;

import com.daijia.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 首页控制器
 * 处理首页相关的请求
 */
@Controller
public class HomeController {

    @Autowired
    private DriverAccountService driverAccountService;

    @Autowired
    private OrderInfoService orderInfoService;

    @Autowired
    private CommissionOrderService commissionOrderService;

    @Autowired
    private ComplaintService complaintService;

    /**
     * 首页
     * @param model 模型
     * @return 首页视图
     */
    @GetMapping("/")
    public String index(Model model) {
        // 添加统计数据到模型
        model.addAttribute("totalAccounts", driverAccountService.findAll().size());
        model.addAttribute("activeAccounts", driverAccountService.findByStatus("ACTIVE").size());
        model.addAttribute("totalOrders", orderInfoService.findAll().size());
        model.addAttribute("completedOrders", orderInfoService.findByStatus("COMPLETED").size());
        model.addAttribute("pendingComplaints", complaintService.findByStatus("PENDING").size());
        model.addAttribute("pendingSettlements", commissionOrderService.findByStatus("PENDING").size());
        
        return "index";
    }

    /**
     * 登录页面（简单版本，实际项目中需要完整的认证机制）
     * @return 登录视图
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    /**
     * 处理登录请求（演示版本，实际项目中需要Spring Security）
     * @param username 用户名
     * @param password 密码
     * @param redirectAttributes 重定向属性
     * @return 重定向路径
     */
    @PostMapping("/login")
    public String doLogin(@RequestParam("username") String username,
                           @RequestParam("password") String password,
                           RedirectAttributes redirectAttributes) {
        // 简单的演示登录逻辑
        if ("admin".equals(username) && "admin123".equals(password)) {
            redirectAttributes.addFlashAttribute("success", "登录成功！欢迎使用代驾后台管理系统");
            return "redirect:/";
        } else {
            redirectAttributes.addFlashAttribute("error", "用户名或密码错误！演示账号：admin / admin123");
            return "redirect:/login";
        }
    }
}

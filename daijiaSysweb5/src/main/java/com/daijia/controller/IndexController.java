package com.daijia.controller;

import com.daijia.service.OrderService;
import com.daijia.service.EmergencyHelpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

/**
 * 主页控制器
 * 
 * 处理系统主页和仪表盘展示
 * 
 * @author daijia
 * @version 1.0.0
 */
@Controller
public class IndexController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private EmergencyHelpService emergencyHelpService;

    /**
     * 主页/仪表盘
     * 
     * 展示系统概览数据
     * 
     * @param model 模型对象
     * @param session 会话对象
     * @return 主页视图
     */
    @GetMapping("/index")
    public String index(Model model, HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/login";
        }

        long pendingOrders = orderService.countByStatus(0);
        long activeOrders = orderService.countByStatus(3);
        long completedOrders = orderService.countByStatus(4);
        
        long pendingHelp = emergencyHelpService.countByStatus(0);
        long processingHelp = emergencyHelpService.countByStatus(1);

        model.addAttribute("pendingOrders", pendingOrders);
        model.addAttribute("activeOrders", activeOrders);
        model.addAttribute("completedOrders", completedOrders);
        model.addAttribute("pendingHelp", pendingHelp);
        model.addAttribute("processingHelp", processingHelp);
        model.addAttribute("totalHelp", pendingHelp + processingHelp);

        model.addAttribute("activeOrderList", orderService.findActiveOrders());
        model.addAttribute("pendingHelpList", emergencyHelpService.findPendingAndProcessing());

        model.addAttribute("currentPage", "dashboard");

        return "index";
    }

    /**
     * 仪表盘页面（同index）
     * 
     * @param model 模型对象
     * @param session 会话对象
     * @return 仪表盘视图
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {
        return index(model, session);
    }
}

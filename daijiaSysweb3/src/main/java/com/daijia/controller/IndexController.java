package com.daijia.controller;

import com.daijia.service.DriverService;
import com.daijia.service.OperationReportService;
import com.daijia.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * 首页控制器
 * 处理系统首页和dashboard相关请求
 *
 * @author daijia
 * @version 1.0.0
 */
@Controller
@RequestMapping("/")
public class IndexController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private DriverService driverService;

    @Autowired
    private OperationReportService operationReportService;

    /**
     * 系统首页
     *
     * @param model 数据模型
     * @return 首页视图
     */
    @GetMapping
    public String index(Model model) {
        return "redirect:/dashboard";
    }

    /**
     * Dashboard仪表盘
     *
     * @param model 数据模型
     * @return dashboard视图
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // 获取订单状态统计
        Map<String, Object> orderStats = orderService.getOrderStatusStats();
        // 获取司机状态统计
        Map<String, Object> driverStats = driverService.getDriverStatusStats();
        // 获取运营报表统计
        Map<String, Object> reportStats = operationReportService.getDashboardStats();
        
        model.addAttribute("orderStats", orderStats);
        model.addAttribute("driverStats", driverStats);
        model.addAttribute("reportStats", reportStats);
        
        return "dashboard";
    }

    /**
     * 运营监控页面
     *
     * @param model 数据模型
     * @return 监控视图
     */
    @GetMapping("/monitor")
    public String monitor(Model model) {
        return "monitor";
    }

    /**
     * 运营报表页面
     *
     * @param model 数据模型
     * @return 报表视图
     */
    @GetMapping("/report")
    public String report(Model model) {
        return "report";
    }

    /**
     * 激励奖励活动页面
     *
     * @param model 数据模型
     * @return 激励活动视图
     */
    @GetMapping("/incentive")
    public String incentive(Model model) {
        return "incentive";
    }

    /**
     * 优惠活动页面
     *
     * @param model 数据模型
     * @return 优惠活动视图
     */
    @GetMapping("/promotion")
    public String promotion(Model model) {
        return "promotion";
    }

    /**
     * 订单管理页面
     *
     * @param model 数据模型
     * @return 订单管理视图
     */
    @GetMapping("/order")
    public String order(Model model) {
        return "order";
    }

    /**
     * 司机管理页面
     *
     * @param model 数据模型
     * @return 司机管理视图
     */
    @GetMapping("/driver")
    public String driver(Model model) {
        return "driver";
    }

    /**
     * 投诉管理页面
     *
     * @param model 数据模型
     * @return 投诉管理视图
     */
    @GetMapping("/complaint")
    public String complaint(Model model) {
        return "complaint";
    }

    /**
     * 服务区域管理页面
     *
     * @param model 数据模型
     * @return 服务区域管理视图
     */
    @GetMapping("/area")
    public String area(Model model) {
        return "area";
    }
}

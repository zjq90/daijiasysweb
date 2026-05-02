package com.daijia.controller;

import com.daijia.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 首页控制器
 * 处理系统首页和概览页面的请求
 */
@Controller
@RequestMapping("/")
public class HomeController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserEvaluationService userEvaluationService;

    @Autowired
    private DriverEvaluationService driverEvaluationService;

    @Autowired
    private PricingConfigService pricingConfigService;

    @Autowired
    private PaymentConfigService paymentConfigService;

    /**
     * 首页
     * 显示系统概览信息
     */
    @GetMapping
    public String index(Model model) {
        // 订单统计
        long pendingOrders = orderService.countByStatus(0);
        long inProgressOrders = orderService.countByStatus(2);
        long completedOrders = orderService.countByStatus(3);
        long paidOrders = orderService.countByStatus(5);

        // 评价统计
        long totalUserEvaluations = userEvaluationService.findAllValid().size();
        long totalDriverEvaluations = driverEvaluationService.findAllValid().size();
        long seriousIssues = driverEvaluationService.findEvaluationsWithSeriousIssues().size();

        // 配置统计
        long pricingConfigs = pricingConfigService.countEnabled();
        long paymentConfigs = paymentConfigService.countEnabled();

        // 最新数据
        var latestOrders = orderService.findLatestOrders(5);
        var latestUserEvaluations = userEvaluationService.findLatestEvaluations(5);
        var latestDriverEvaluations = driverEvaluationService.findLatestEvaluations(5);

        model.addAttribute("pendingOrders", pendingOrders);
        model.addAttribute("inProgressOrders", inProgressOrders);
        model.addAttribute("completedOrders", completedOrders);
        model.addAttribute("paidOrders", paidOrders);
        model.addAttribute("totalUserEvaluations", totalUserEvaluations);
        model.addAttribute("totalDriverEvaluations", totalDriverEvaluations);
        model.addAttribute("seriousIssues", seriousIssues);
        model.addAttribute("pricingConfigs", pricingConfigs);
        model.addAttribute("paymentConfigs", paymentConfigs);
        model.addAttribute("latestOrders", latestOrders);
        model.addAttribute("latestUserEvaluations", latestUserEvaluations);
        model.addAttribute("latestDriverEvaluations", latestDriverEvaluations);

        return "index";
    }

    /**
     * 服务信用管理页面
     */
    @GetMapping("/credit")
    public String credit(Model model) {
        var userEvaluations = userEvaluationService.findAllValid();
        var driverEvaluations = driverEvaluationService.findAllValid();
        var seriousIssues = driverEvaluationService.findEvaluationsWithSeriousIssues();

        model.addAttribute("userEvaluations", userEvaluations);
        model.addAttribute("driverEvaluations", driverEvaluations);
        model.addAttribute("seriousIssues", seriousIssues);

        return "credit/index";
    }

    /**
     * 用户评价管理页面
     */
    @GetMapping("/credit/user-evaluations")
    public String userEvaluations(Model model) {
        var evaluations = userEvaluationService.findAllValid();
        model.addAttribute("evaluations", evaluations);
        return "credit/user-evaluations";
    }

    /**
     * 司机评价管理页面
     */
    @GetMapping("/credit/driver-evaluations")
    public String driverEvaluations(Model model) {
        var evaluations = driverEvaluationService.findAllValid();
        var seriousIssues = driverEvaluationService.findEvaluationsWithSeriousIssues();
        model.addAttribute("evaluations", evaluations);
        model.addAttribute("seriousIssues", seriousIssues);
        return "credit/driver-evaluations";
    }

    /**
     * 计费设置页面
     */
    @GetMapping("/pricing")
    public String pricing(Model model) {
        var configs = pricingConfigService.findAllEnabled();
        var defaultConfig = pricingConfigService.findDefaultConfig();
        model.addAttribute("configs", configs);
        model.addAttribute("defaultConfig", defaultConfig.orElse(null));
        return "pricing/index";
    }

    /**
     * 支付方式配置页面
     */
    @GetMapping("/payment")
    public String payment(Model model) {
        var configs = paymentConfigService.findAllEnabled();
        var prepayConfigs = paymentConfigService.findPrepayEnabledConfigs();
        var postpayConfigs = paymentConfigService.findPostpayEnabledConfigs();
        model.addAttribute("configs", configs);
        model.addAttribute("prepayConfigs", prepayConfigs);
        model.addAttribute("postpayConfigs", postpayConfigs);
        return "payment/index";
    }

    /**
     * 订单管理页面
     */
    @GetMapping("/orders")
    public String orders(Model model) {
        var allOrders = orderService.findAll();
        var pendingOrders = orderService.findPendingOrders();
        var inProgressOrders = orderService.findInProgressOrders();
        model.addAttribute("allOrders", allOrders);
        model.addAttribute("pendingOrders", pendingOrders);
        model.addAttribute("inProgressOrders", inProgressOrders);
        return "orders/index";
    }

    /**
     * 测试功能页面
     */
    @GetMapping("/test")
    public String test(Model model) {
        return "test/index";
    }
}

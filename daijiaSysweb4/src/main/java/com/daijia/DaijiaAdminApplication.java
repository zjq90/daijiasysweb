package com.daijia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 代驾服务后台管理系统启动类
 * 系统功能包括：
 * 1. 服务信用管理（用户评价记录、司机评价记录）
 * 2. 系统计费设置（起步价、里程费、等待费、夜间加价）
 * 3. 支付方式配置（微信、支付宝、银联，支持预付/后付模式）
 * 4. 订单管理（订单创建、接单、完成、取消、支付）
 * 
 * 技术栈：
 * - Spring Boot 3.2.5
 * - Spring Data JPA
 * - H2 Database
 * - Thymeleaf 模板引擎
 * - Bootstrap 5 前端框架
 */
@SpringBootApplication
public class DaijiaAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(DaijiaAdminApplication.class, args);
        System.out.println("========================================");
        System.out.println("  代驾服务后台管理系统启动成功！");
        System.out.println("========================================");
        System.out.println("  访问地址: http://localhost:8080");
        System.out.println("  H2控制台: http://localhost:8080/h2-console");
        System.out.println("========================================");
        System.out.println("  功能模块:");
        System.out.println("  - 首页概览: /");
        System.out.println("  - 服务信用管理: /credit");
        System.out.println("    - 用户评价记录: /credit/user-evaluations");
        System.out.println("    - 司机评价记录: /credit/driver-evaluations");
        System.out.println("  - 计费设置: /pricing");
        System.out.println("  - 支付配置: /payment");
        System.out.println("  - 订单管理: /orders");
        System.out.println("  - 测试功能: /test");
        System.out.println("========================================");
    }
}

package com.daijia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 代驾后台管理系统主启动类
 * 功能模块：
 * 1. 运营监控：实时监控订单状态、司机分布、服务区域热力图
 * 2. 运营报表：日订单量、成交率、司机收入、投诉率等
 * 3. 营销活动：激励奖励政策活动、优惠活动管理
 *
 * @author daijia
 * @version 1.0.0
 */
@SpringBootApplication
public class DaijiaSyswebApplication {

    public static void main(String[] args) {
        SpringApplication.run(DaijiaSyswebApplication.class, args);
        System.out.println("========================================");
        System.out.println("  代驾后台管理系统启动成功！");
        System.out.println("  访问地址: http://localhost:8080");
        System.out.println("  H2控制台: http://localhost:8080/h2-console");
        System.out.println("========================================");
    }
}

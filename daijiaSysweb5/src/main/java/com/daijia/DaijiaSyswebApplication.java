package com.daijia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 代驾Web后台管理系统主启动类
 * 
 * 技术栈：
 * - Spring Boot 2.7.18
 * - H2 内存数据库
 * - JPA (Hibernate)
 * - Thymeleaf 模板引擎
 * - Bootstrap 5 前端框架
 * 
 * 功能模块：
 * 1. 实时跟踪与导航：高德地图集成，实时显示客户与司机位置，路线规划
 * 2. 安全求助功能：紧急求助按钮，定位与行程信息发送，录音功能
 * 
 * @author daijia
 * @version 1.0.0
 */
@SpringBootApplication
public class DaijiaSyswebApplication {

    /**
     * 应用程序入口点
     * 
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(DaijiaSyswebApplication.class, args);
        System.out.println("==============================================");
        System.out.println("    代驾Web后台管理系统启动成功！");
        System.out.println("==============================================");
        System.out.println("访问地址：http://localhost:8080/");
        System.out.println("H2控制台：http://localhost:8080/h2-console");
        System.out.println("管理员账号：admin / 123456");
        System.out.println("==============================================");
    }
}

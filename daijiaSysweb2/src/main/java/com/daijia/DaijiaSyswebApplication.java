package com.daijia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 代驾Web后台管理系统启动类
 * 这是Spring Boot应用的入口点
 */
@SpringBootApplication
public class DaijiaSyswebApplication {

    public static void main(String[] args) {
        SpringApplication.run(DaijiaSyswebApplication.class, args);
        System.out.println("==============================================");
        System.out.println("  代驾Web后台管理系统启动成功！");
        System.out.println("  访问地址: http://localhost:8080");
        System.out.println("  H2控制台: http://localhost:8080/h2-console");
        System.out.println("==============================================");
    }
}

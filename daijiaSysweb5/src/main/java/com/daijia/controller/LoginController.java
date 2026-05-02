package com.daijia.controller;

import com.daijia.entity.Admin;
import com.daijia.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

/**
 * 登录控制器
 * 
 * 处理管理员登录、登出功能
 * 
 * @author daijia
 * @version 1.0.0
 */
@Controller
public class LoginController {

    @Autowired
    private AdminService adminService;

    /**
     * 登录页面
     * 
     * @return 登录页面视图
     */
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    /**
     * 登录处理
     * 
     * @param username 用户名
     * @param password 密码
     * @param session 会话对象
     * @param model 模型对象
     * @return 重定向路径
     */
    @PostMapping("/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        HttpSession session,
                        Model model) {
        Admin admin = adminService.login(username, password);
        
        if (admin != null) {
            session.setAttribute("admin", admin);
            session.setAttribute("adminId", admin.getId());
            session.setAttribute("adminName", admin.getRealName() != null ? admin.getRealName() : admin.getUsername());
            return "redirect:/index";
        } else {
            model.addAttribute("error", "用户名或密码错误");
            model.addAttribute("username", username);
            return "login";
        }
    }

    /**
     * 登出
     * 
     * @param session 会话对象
     * @return 重定向到登录页
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("admin");
        session.removeAttribute("adminId");
        session.removeAttribute("adminName");
        session.invalidate();
        return "redirect:/login";
    }

    /**
     * 根路径重定向到登录页或主页
     * 
     * @param session 会话对象
     * @return 重定向路径
     */
    @GetMapping("/")
    public String index(HttpSession session) {
        if (session.getAttribute("admin") != null) {
            return "redirect:/index";
        }
        return "redirect:/login";
    }
}

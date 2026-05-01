
package com.daijia.controller;

import com.daijia.entity.SysUser;
import com.daijia.service.SysUserService;
import com.daijia.util.ValidationUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping
public class LoginController {
    
    @Autowired
    private SysUserService sysUserService;
    
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
    
    @PostMapping("/login")
    public String login(String username, String password, Model model, HttpSession session) {
        if (StringUtils.isBlank(username)) {
            model.addAttribute("error", "用户名不能为空");
            return "login";
        }
        
        if (!ValidationUtil.isValidUsername(username)) {
            model.addAttribute("error", "用户名格式不正确，必须为3-20位，只能包含字母、数字和下划线");
            return "login";
        }
        
        if (StringUtils.isBlank(password)) {
            model.addAttribute("error", "密码不能为空");
            return "login";
        }
        
        if (!ValidationUtil.isValidPassword(password)) {
            model.addAttribute("error", "密码长度必须为6-20位");
            return "login";
        }
        
        SysUser user = sysUserService.login(username, password);
        if (user != null) {
            session.setAttribute("currentUser", user);
            return "redirect:/index";
        }
        model.addAttribute("error", "用户名或密码错误");
        return "login";
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("currentUser");
        return "redirect:/login";
    }
    
    @GetMapping("/index")
    public String index(HttpSession session) {
        if (session.getAttribute("currentUser") == null) {
            return "redirect:/login";
        }
        return "index";
    }
    
    @GetMapping("/")
    public String root(HttpSession session) {
        if (session.getAttribute("currentUser") == null) {
            return "redirect:/login";
        }
        return "redirect:/index";
    }
}

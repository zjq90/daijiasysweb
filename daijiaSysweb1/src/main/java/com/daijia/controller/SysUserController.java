
package com.daijia.controller;

import com.daijia.entity.SysUser;
import com.daijia.service.SysUserService;
import com.daijia.util.ValidationUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/sysUser")
public class SysUserController {
    
    @Autowired
    private SysUserService sysUserService;
    
    private boolean checkLogin(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }
    
    @GetMapping("/list")
    public String list(Model model, HttpSession session) {
        if (!checkLogin(session)) {
            return "redirect:/login";
        }
        List<SysUser> list = sysUserService.findAll();
        model.addAttribute("sysUsers", list);
        return "sysUser/list";
    }
    
    @GetMapping("/add")
    public String addPage(Model model, HttpSession session) {
        if (!checkLogin(session)) {
            return "redirect:/login";
        }
        model.addAttribute("sysUser", new SysUser());
        model.addAttribute("action", "add");
        return "sysUser/form";
    }
    
    @GetMapping("/edit/{id}")
    public String editPage(@PathVariable Long id, Model model, HttpSession session) {
        if (!checkLogin(session)) {
            return "redirect:/login";
        }
        SysUser sysUser = sysUserService.findById(id).orElse(null);
        model.addAttribute("sysUser", sysUser);
        model.addAttribute("action", "edit");
        return "sysUser/form";
    }
    
    @PostMapping("/save")
    public String save(@ModelAttribute SysUser sysUser, Model model, HttpSession session) {
        if (!checkLogin(session)) {
            return "redirect:/login";
        }
        
        boolean isAdd = sysUser.getId() == null;
        
        if (StringUtils.isBlank(sysUser.getUsername())) {
            model.addAttribute("error", "用户名不能为空");
            model.addAttribute("sysUser", sysUser);
            model.addAttribute("action", isAdd ? "add" : "edit");
            return "sysUser/form";
        }
        
        if (!ValidationUtil.isValidUsername(sysUser.getUsername())) {
            model.addAttribute("error", "用户名必须为3-20位，只能包含字母、数字和下划线");
            model.addAttribute("sysUser", sysUser);
            model.addAttribute("action", isAdd ? "add" : "edit");
            return "sysUser/form";
        }
        
        if (isAdd && StringUtils.isBlank(sysUser.getPassword())) {
            model.addAttribute("error", "密码不能为空");
            model.addAttribute("sysUser", sysUser);
            model.addAttribute("action", "add");
            return "sysUser/form";
        }
        
        if (isAdd && !ValidationUtil.isValidPassword(sysUser.getPassword())) {
            model.addAttribute("error", "密码长度必须为6-20位");
            model.addAttribute("sysUser", sysUser);
            model.addAttribute("action", "add");
            return "sysUser/form";
        }
        
        if (StringUtils.isNotBlank(sysUser.getPhone()) && !ValidationUtil.isValidPhone(sysUser.getPhone())) {
            model.addAttribute("error", "请输入正确的11位手机号");
            model.addAttribute("sysUser", sysUser);
            model.addAttribute("action", isAdd ? "add" : "edit");
            return "sysUser/form";
        }
        
        try {
            sysUserService.save(sysUser);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("sysUser", sysUser);
            model.addAttribute("action", isAdd ? "add" : "edit");
            return "sysUser/form";
        }
        
        return "redirect:/sysUser/list";
    }
    
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, HttpSession session) {
        if (!checkLogin(session)) {
            return "redirect:/login";
        }
        sysUserService.deleteById(id);
        return "redirect:/sysUser/list";
    }
}

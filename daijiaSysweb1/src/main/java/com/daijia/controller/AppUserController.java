
package com.daijia.controller;

import com.daijia.entity.AppUser;
import com.daijia.service.AppUserService;
import com.daijia.util.ValidationUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/appUser")
public class AppUserController {
    
    @Autowired
    private AppUserService appUserService;
    
    private boolean checkLogin(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }
    
    @GetMapping("/list")
    public String list(Model model, HttpSession session) {
        if (!checkLogin(session)) {
            return "redirect:/login";
        }
        List<AppUser> list = appUserService.findAll();
        model.addAttribute("appUsers", list);
        return "appUser/list";
    }
    
    @GetMapping("/add")
    public String addPage(Model model, HttpSession session) {
        if (!checkLogin(session)) {
            return "redirect:/login";
        }
        model.addAttribute("appUser", new AppUser());
        model.addAttribute("action", "add");
        return "appUser/form";
    }
    
    @GetMapping("/edit/{id}")
    public String editPage(@PathVariable Long id, Model model, HttpSession session) {
        if (!checkLogin(session)) {
            return "redirect:/login";
        }
        AppUser appUser = appUserService.findById(id).orElse(null);
        model.addAttribute("appUser", appUser);
        model.addAttribute("action", "edit");
        return "appUser/form";
    }
    
    @PostMapping("/save")
    public String save(@ModelAttribute AppUser appUser, Model model, HttpSession session) {
        if (!checkLogin(session)) {
            return "redirect:/login";
        }
        
        boolean isAdd = appUser.getId() == null;
        
        if (StringUtils.isBlank(appUser.getUsername())) {
            model.addAttribute("error", "用户名不能为空");
            model.addAttribute("appUser", appUser);
            model.addAttribute("action", isAdd ? "add" : "edit");
            return "appUser/form";
        }
        
        if (!ValidationUtil.isValidUsername(appUser.getUsername())) {
            model.addAttribute("error", "用户名必须为3-20位，只能包含字母、数字和下划线");
            model.addAttribute("appUser", appUser);
            model.addAttribute("action", isAdd ? "add" : "edit");
            return "appUser/form";
        }
        
        if (isAdd && StringUtils.isBlank(appUser.getPassword())) {
            model.addAttribute("error", "密码不能为空");
            model.addAttribute("appUser", appUser);
            model.addAttribute("action", "add");
            return "appUser/form";
        }
        
        if (isAdd && !ValidationUtil.isValidPassword(appUser.getPassword())) {
            model.addAttribute("error", "密码长度必须为6-20位");
            model.addAttribute("appUser", appUser);
            model.addAttribute("action", "add");
            return "appUser/form";
        }
        
        if (StringUtils.isNotBlank(appUser.getRealName()) && !ValidationUtil.isValidRealName(appUser.getRealName())) {
            model.addAttribute("error", "真实姓名只能是中文，长度2-20位");
            model.addAttribute("appUser", appUser);
            model.addAttribute("action", isAdd ? "add" : "edit");
            return "appUser/form";
        }
        
        if (StringUtils.isNotBlank(appUser.getPhone()) && !ValidationUtil.isValidPhone(appUser.getPhone())) {
            model.addAttribute("error", "请输入正确的11位手机号");
            model.addAttribute("appUser", appUser);
            model.addAttribute("action", isAdd ? "add" : "edit");
            return "appUser/form";
        }
        
        if (StringUtils.isNotBlank(appUser.getIdCard()) && !ValidationUtil.isValidIdCard(appUser.getIdCard())) {
            model.addAttribute("error", "请输入正确的15或18位身份证号");
            model.addAttribute("appUser", appUser);
            model.addAttribute("action", isAdd ? "add" : "edit");
            return "appUser/form";
        }
        
        if (appUser.getBalance() != null && !ValidationUtil.isValidBalance(appUser.getBalance())) {
            model.addAttribute("error", "账户余额不能为负数");
            model.addAttribute("appUser", appUser);
            model.addAttribute("action", isAdd ? "add" : "edit");
            return "appUser/form";
        }
        
        try {
            appUserService.save(appUser);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("appUser", appUser);
            model.addAttribute("action", isAdd ? "add" : "edit");
            return "appUser/form";
        }
        
        return "redirect:/appUser/list";
    }
    
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, HttpSession session) {
        if (!checkLogin(session)) {
            return "redirect:/login";
        }
        appUserService.deleteById(id);
        return "redirect:/appUser/list";
    }
}

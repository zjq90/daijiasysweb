
package com.daijia.controller;

import com.daijia.entity.Driver;
import com.daijia.service.DriverService;
import com.daijia.util.ValidationUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/driver")
public class DriverController {
    
    @Autowired
    private DriverService driverService;
    
    private boolean checkLogin(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }
    
    @GetMapping("/list")
    public String list(Model model, HttpSession session) {
        if (!checkLogin(session)) {
            return "redirect:/login";
        }
        List<Driver> list = driverService.findAll();
        model.addAttribute("drivers", list);
        return "driver/list";
    }
    
    @GetMapping("/add")
    public String addPage(Model model, HttpSession session) {
        if (!checkLogin(session)) {
            return "redirect:/login";
        }
        model.addAttribute("driver", new Driver());
        model.addAttribute("action", "add");
        return "driver/form";
    }
    
    @GetMapping("/edit/{id}")
    public String editPage(@PathVariable Long id, Model model, HttpSession session) {
        if (!checkLogin(session)) {
            return "redirect:/login";
        }
        Driver driver = driverService.findById(id).orElse(null);
        model.addAttribute("driver", driver);
        model.addAttribute("action", "edit");
        return "driver/form";
    }
    
    @PostMapping("/save")
    public String save(@ModelAttribute Driver driver, Model model, HttpSession session) {
        if (!checkLogin(session)) {
            return "redirect:/login";
        }
        
        boolean isAdd = driver.getId() == null;
        
        if (StringUtils.isBlank(driver.getUsername())) {
            model.addAttribute("error", "用户名不能为空");
            model.addAttribute("driver", driver);
            model.addAttribute("action", isAdd ? "add" : "edit");
            return "driver/form";
        }
        
        if (!ValidationUtil.isValidUsername(driver.getUsername())) {
            model.addAttribute("error", "用户名必须为3-20位，只能包含字母、数字和下划线");
            model.addAttribute("driver", driver);
            model.addAttribute("action", isAdd ? "add" : "edit");
            return "driver/form";
        }
        
        if (isAdd && StringUtils.isBlank(driver.getPassword())) {
            model.addAttribute("error", "密码不能为空");
            model.addAttribute("driver", driver);
            model.addAttribute("action", "add");
            return "driver/form";
        }
        
        if (isAdd && !ValidationUtil.isValidPassword(driver.getPassword())) {
            model.addAttribute("error", "密码长度必须为6-20位");
            model.addAttribute("driver", driver);
            model.addAttribute("action", "add");
            return "driver/form";
        }
        
        if (StringUtils.isNotBlank(driver.getRealName()) && !ValidationUtil.isValidRealName(driver.getRealName())) {
            model.addAttribute("error", "真实姓名只能是中文，长度2-20位");
            model.addAttribute("driver", driver);
            model.addAttribute("action", isAdd ? "add" : "edit");
            return "driver/form";
        }
        
        if (StringUtils.isNotBlank(driver.getPhone()) && !ValidationUtil.isValidPhone(driver.getPhone())) {
            model.addAttribute("error", "请输入正确的11位手机号");
            model.addAttribute("driver", driver);
            model.addAttribute("action", isAdd ? "add" : "edit");
            return "driver/form";
        }
        
        if (StringUtils.isNotBlank(driver.getIdCard()) && !ValidationUtil.isValidIdCard(driver.getIdCard())) {
            model.addAttribute("error", "请输入正确的15或18位身份证号");
            model.addAttribute("driver", driver);
            model.addAttribute("action", isAdd ? "add" : "edit");
            return "driver/form";
        }
        
        if (StringUtils.isNotBlank(driver.getLicenseNumber()) && !ValidationUtil.isValidLicenseNumber(driver.getLicenseNumber())) {
            model.addAttribute("error", "驾照号只能包含字母和数字");
            model.addAttribute("driver", driver);
            model.addAttribute("action", isAdd ? "add" : "edit");
            return "driver/form";
        }
        
        if (StringUtils.isNotBlank(driver.getLicenseType()) && !ValidationUtil.isValidLicenseType(driver.getLicenseType())) {
            model.addAttribute("error", "驾照类型格式不正确，如：C1、B1、A1");
            model.addAttribute("driver", driver);
            model.addAttribute("action", isAdd ? "add" : "edit");
            return "driver/form";
        }
        
        if (driver.getBalance() != null && !ValidationUtil.isValidBalance(driver.getBalance())) {
            model.addAttribute("error", "账户余额不能为负数");
            model.addAttribute("driver", driver);
            model.addAttribute("action", isAdd ? "add" : "edit");
            return "driver/form";
        }
        
        if (driver.getCreditRating() != null && !ValidationUtil.isValidCreditRating(driver.getCreditRating())) {
            model.addAttribute("error", "信用等级必须在0-5分之间");
            model.addAttribute("driver", driver);
            model.addAttribute("action", isAdd ? "add" : "edit");
            return "driver/form";
        }
        
        if (!ValidationUtil.isValidLatitude(driver.getLatitude())) {
            model.addAttribute("error", "纬度必须在-90到90之间");
            model.addAttribute("driver", driver);
            model.addAttribute("action", isAdd ? "add" : "edit");
            return "driver/form";
        }
        
        if (!ValidationUtil.isValidLongitude(driver.getLongitude())) {
            model.addAttribute("error", "经度必须在-180到180之间");
            model.addAttribute("driver", driver);
            model.addAttribute("action", isAdd ? "add" : "edit");
            return "driver/form";
        }
        
        try {
            driverService.save(driver);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("driver", driver);
            model.addAttribute("action", isAdd ? "add" : "edit");
            return "driver/form";
        }
        
        return "redirect:/driver/list";
    }
    
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, HttpSession session) {
        if (!checkLogin(session)) {
            return "redirect:/login";
        }
        driverService.deleteById(id);
        return "redirect:/driver/list";
    }
}

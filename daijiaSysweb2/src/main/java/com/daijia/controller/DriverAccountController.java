package com.daijia.controller;

import com.daijia.entity.DriverAccount;
import com.daijia.service.AccountDetailService;
import com.daijia.service.DriverAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

/**
 * 代驾账户控制器
 * 处理代驾账户相关的请求
 */
@Controller
@RequestMapping("/account")
public class DriverAccountController {

    @Autowired
    private DriverAccountService driverAccountService;

    @Autowired
    private AccountDetailService accountDetailService;

    /**
     * 账户列表页面
     * @param model 模型
     * @return 账户列表视图
     */
    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("accounts", driverAccountService.findAll());
        return "account/list";
    }

    /**
     * 账户详情页面
     * @param id 账户ID
     * @param model 模型
     * @return 账户详情视图
     */
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Optional<DriverAccount> accountOpt = driverAccountService.findById(id);
        if (accountOpt.isPresent()) {
            model.addAttribute("account", accountOpt.get());
            model.addAttribute("details", accountDetailService.findByAccountId(id));
            return "account/detail";
        }
        return "redirect:/account/list";
    }

    /**
     * 新增账户页面
     * @param model 模型
     * @return 新增账户视图
     */
    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("account", new DriverAccount());
        return "account/form";
    }

    /**
     * 编辑账户页面
     * @param id 账户ID
     * @param model 模型
     * @return 编辑账户视图
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        Optional<DriverAccount> accountOpt = driverAccountService.findById(id);
        if (accountOpt.isPresent()) {
            model.addAttribute("account", accountOpt.get());
            return "account/form";
        }
        return "redirect:/account/list";
    }

    /**
     * 保存账户
     * @param account 账户信息
     * @param redirectAttributes 重定向属性
     * @return 重定向到列表页面
     */
    @PostMapping("/save")
    public String save(@ModelAttribute DriverAccount account, RedirectAttributes redirectAttributes) {
        try {
            if (account.getId() == null) {
                driverAccountService.save(account);
                redirectAttributes.addFlashAttribute("message", "账户创建成功");
            } else {
                driverAccountService.update(account);
                redirectAttributes.addFlashAttribute("message", "账户更新成功");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/account/list";
    }

    /**
     * 冻结账户
     * @param id 账户ID
     * @param redirectAttributes 重定向属性
     * @return 重定向到列表页面
     */
    @GetMapping("/freeze/{id}")
    public String freeze(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            driverAccountService.freezeAccount(id);
            redirectAttributes.addFlashAttribute("message", "账户已冻结");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/account/list";
    }

    /**
     * 解冻账户
     * @param id 账户ID
     * @param redirectAttributes 重定向属性
     * @return 重定向到列表页面
     */
    @GetMapping("/unfreeze/{id}")
    public String unfreeze(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            driverAccountService.unfreezeAccount(id);
            redirectAttributes.addFlashAttribute("message", "账户已解冻");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/account/list";
    }

    /**
     * 删除账户
     * @param id 账户ID
     * @param redirectAttributes 重定向属性
     * @return 重定向到列表页面
     */
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            driverAccountService.deleteById(id);
            redirectAttributes.addFlashAttribute("message", "账户已删除");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/account/list";
    }
}

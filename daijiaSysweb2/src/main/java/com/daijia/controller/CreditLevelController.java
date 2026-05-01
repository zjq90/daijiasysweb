package com.daijia.controller;

import com.daijia.entity.CreditLevel;
import com.daijia.service.CreditLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

/**
 * 信用等级控制器
 * 处理信用等级相关的请求
 */
@Controller
@RequestMapping("/credit")
public class CreditLevelController {

    @Autowired
    private CreditLevelService creditLevelService;

    /**
     * 信用等级列表页面
     * @param model 模型
     * @return 信用等级列表视图
     */
    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("creditLevels", creditLevelService.findAll());
        return "credit/list";
    }

    /**
     * 信用等级详情页面
     * @param id 等级ID
     * @param model 模型
     * @return 信用等级详情视图
     */
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Optional<CreditLevel> levelOpt = creditLevelService.findById(id);
        if (levelOpt.isPresent()) {
            model.addAttribute("creditLevel", levelOpt.get());
            return "credit/detail";
        }
        return "redirect:/credit/list";
    }

    /**
     * 新增信用等级页面
     * @param model 模型
     * @return 新增信用等级视图
     */
    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("creditLevel", new CreditLevel());
        return "credit/form";
    }

    /**
     * 编辑信用等级页面
     * @param id 等级ID
     * @param model 模型
     * @return 编辑信用等级视图
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        Optional<CreditLevel> levelOpt = creditLevelService.findById(id);
        if (levelOpt.isPresent()) {
            model.addAttribute("creditLevel", levelOpt.get());
            return "credit/form";
        }
        return "redirect:/credit/list";
    }

    /**
     * 保存信用等级
     * @param creditLevel 信用等级信息
     * @param redirectAttributes 重定向属性
     * @return 重定向到列表页面
     */
    @PostMapping("/save")
    public String save(@ModelAttribute CreditLevel creditLevel, RedirectAttributes redirectAttributes) {
        try {
            if (creditLevel.getId() == null) {
                creditLevelService.save(creditLevel);
                redirectAttributes.addFlashAttribute("message", "信用等级创建成功");
            } else {
                creditLevelService.update(creditLevel);
                redirectAttributes.addFlashAttribute("message", "信用等级更新成功");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/credit/list";
    }

    /**
     * 删除信用等级
     * @param id 等级ID
     * @param redirectAttributes 重定向属性
     * @return 重定向到列表页面
     */
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            creditLevelService.deleteById(id);
            redirectAttributes.addFlashAttribute("message", "信用等级已删除");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/credit/list";
    }
}

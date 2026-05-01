package com.daijia.controller;

import com.daijia.entity.CommissionConfig;
import com.daijia.service.CommissionConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

/**
 * 订单分成配置控制器
 * 处理订单分成配置相关的请求
 */
@Controller
@RequestMapping("/config")
public class CommissionConfigController {

    @Autowired
    private CommissionConfigService commissionConfigService;

    /**
     * 分成配置列表页面
     * @param model 模型
     * @return 分成配置列表视图
     */
    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("configs", commissionConfigService.findAll());
        return "config/list";
    }

    /**
     * 分成配置详情页面
     * @param id 配置ID
     * @param model 模型
     * @return 分成配置详情视图
     */
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Optional<CommissionConfig> configOpt = commissionConfigService.findById(id);
        if (configOpt.isPresent()) {
            model.addAttribute("config", configOpt.get());
            return "config/detail";
        }
        return "redirect:/config/list";
    }

    /**
     * 新增分成配置页面
     * @param model 模型
     * @return 新增分成配置视图
     */
    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("config", new CommissionConfig());
        return "config/form";
    }

    /**
     * 编辑分成配置页面
     * @param id 配置ID
     * @param model 模型
     * @return 编辑分成配置视图
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        Optional<CommissionConfig> configOpt = commissionConfigService.findById(id);
        if (configOpt.isPresent()) {
            model.addAttribute("config", configOpt.get());
            return "config/form";
        }
        return "redirect:/config/list";
    }

    /**
     * 保存分成配置
     * @param config 配置信息
     * @param redirectAttributes 重定向属性
     * @return 重定向到列表页面
     */
    @PostMapping("/save")
    public String save(@ModelAttribute CommissionConfig config, RedirectAttributes redirectAttributes) {
        try {
            if (config.getId() == null) {
                commissionConfigService.save(config);
                redirectAttributes.addFlashAttribute("message", "分成配置创建成功");
            } else {
                commissionConfigService.update(config);
                redirectAttributes.addFlashAttribute("message", "分成配置更新成功");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/config/list";
    }

    /**
     * 删除分成配置
     * @param id 配置ID
     * @param redirectAttributes 重定向属性
     * @return 重定向到列表页面
     */
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            commissionConfigService.deleteById(id);
            redirectAttributes.addFlashAttribute("message", "分成配置已删除");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/config/list";
    }
}

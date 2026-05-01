package com.daijia.controller;

import com.daijia.entity.Reconciliation;
import com.daijia.service.ReconciliationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

/**
 * 对账单控制器
 * 处理对账单相关的请求
 */
@Controller
@RequestMapping("/recon")
public class ReconciliationController {

    @Autowired
    private ReconciliationService reconciliationService;

    /**
     * 对账单列表页面
     * @param model 模型
     * @return 对账单列表视图
     */
    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("reconciliations", reconciliationService.findAll());
        return "recon/list";
    }

    /**
     * 对账单详情页面
     * @param id 对账单ID
     * @param model 模型
     * @return 对账单详情视图
     */
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Optional<Reconciliation> reconOpt = reconciliationService.findById(id);
        if (reconOpt.isPresent()) {
            model.addAttribute("reconciliation", reconOpt.get());
            return "recon/detail";
        }
        return "redirect:/recon/list";
    }

    /**
     * 新增对账单页面
     * @param model 模型
     * @return 新增对账单视图
     */
    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("reconciliation", new Reconciliation());
        return "recon/form";
    }

    /**
     * 编辑对账单页面
     * @param id 对账单ID
     * @param model 模型
     * @return 编辑对账单视图
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        Optional<Reconciliation> reconOpt = reconciliationService.findById(id);
        if (reconOpt.isPresent()) {
            model.addAttribute("reconciliation", reconOpt.get());
            return "recon/form";
        }
        return "redirect:/recon/list";
    }

    /**
     * 保存对账单
     * @param recon 对账单信息
     * @param redirectAttributes 重定向属性
     * @return 重定向到列表页面
     */
    @PostMapping("/save")
    public String save(@ModelAttribute Reconciliation recon, RedirectAttributes redirectAttributes) {
        try {
            reconciliationService.save(recon);
            redirectAttributes.addFlashAttribute("message", recon.getId() == null ? "对账单创建成功" : "对账单更新成功");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/recon/list";
    }

    /**
     * 确认对账单
     * @param id 对账单ID
     * @param redirectAttributes 重定向属性
     * @return 重定向到列表页面
     */
    @GetMapping("/confirm/{id}")
    public String confirm(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            reconciliationService.confirm(id);
            redirectAttributes.addFlashAttribute("message", "对账单已确认");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/recon/list";
    }

    /**
     * 标记对账单有异议（GET版本，用于页面链接）
     * @param id 对账单ID
     * @param redirectAttributes 重定向属性
     * @return 重定向到列表页面
     */
    @GetMapping("/dispute/{id}")
    public String disputeGet(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            reconciliationService.dispute(id, null);
            redirectAttributes.addFlashAttribute("message", "对账单已标记异议");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/recon/list";
    }

    /**
     * 标记对账单有异议
     * @param id 对账单ID
     * @param remark 异议备注
     * @param redirectAttributes 重定向属性
     * @return 重定向到列表页面
     */
    @PostMapping("/dispute/{id}")
    public String dispute(@PathVariable Long id,
                          @RequestParam(required = false) String remark,
                          RedirectAttributes redirectAttributes) {
        try {
            reconciliationService.dispute(id, remark);
            redirectAttributes.addFlashAttribute("message", "对账单已标记异议");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/recon/list";
    }

    /**
     * 删除对账单
     * @param id 对账单ID
     * @param redirectAttributes 重定向属性
     * @return 重定向到列表页面
     */
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            reconciliationService.deleteById(id);
            redirectAttributes.addFlashAttribute("message", "对账单已删除");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/recon/list";
    }
}

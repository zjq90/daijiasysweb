package com.daijia.controller;

import com.daijia.entity.CommissionOrder;
import com.daijia.service.CommissionOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

/**
 * 分成单控制器
 * 处理分成单相关的请求
 */
@Controller
@RequestMapping("/commission")
public class CommissionOrderController {

    @Autowired
    private CommissionOrderService commissionOrderService;

    /**
     * 分成单列表页面
     * @param model 模型
     * @return 分成单列表视图
     */
    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("commissionOrders", commissionOrderService.findAll());
        return "commission/list";
    }

    /**
     * 分成单详情页面
     * @param id 分成单ID
     * @param model 模型
     * @return 分成单详情视图
     */
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Optional<CommissionOrder> orderOpt = commissionOrderService.findById(id);
        if (orderOpt.isPresent()) {
            model.addAttribute("commissionOrder", orderOpt.get());
            return "commission/detail";
        }
        return "redirect:/commission/list";
    }

    /**
     * 新增分成单页面
     * @param model 模型
     * @return 新增分成单视图
     */
    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("commissionOrder", new CommissionOrder());
        return "commission/form";
    }

    /**
     * 编辑分成单页面
     * @param id 分成单ID
     * @param model 模型
     * @return 编辑分成单视图
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        Optional<CommissionOrder> orderOpt = commissionOrderService.findById(id);
        if (orderOpt.isPresent()) {
            model.addAttribute("commissionOrder", orderOpt.get());
            return "commission/form";
        }
        return "redirect:/commission/list";
    }

    /**
     * 保存分成单
     * @param commission 分成单信息
     * @param redirectAttributes 重定向属性
     * @return 重定向到列表页面
     */
    @PostMapping("/save")
    public String save(@ModelAttribute CommissionOrder commission, RedirectAttributes redirectAttributes) {
        try {
            commissionOrderService.save(commission);
            redirectAttributes.addFlashAttribute("message", commission.getId() == null ? "分成单创建成功" : "分成单更新成功");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/commission/list";
    }

    /**
     * 结算分成单
     * @param id 分成单ID
     * @param redirectAttributes 重定向属性
     * @return 重定向到列表页面
     */
    @GetMapping("/settle/{id}")
    public String settle(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            commissionOrderService.settle(id);
            redirectAttributes.addFlashAttribute("message", "分成单已结算");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/commission/list";
    }

    /**
     * 取消分成单
     * @param id 分成单ID
     * @param redirectAttributes 重定向属性
     * @return 重定向到列表页面
     */
    @GetMapping("/cancel/{id}")
    public String cancel(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            commissionOrderService.cancel(id);
            redirectAttributes.addFlashAttribute("message", "分成单已取消");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/commission/list";
    }

    /**
     * 删除分成单
     * @param id 分成单ID
     * @param redirectAttributes 重定向属性
     * @return 重定向到列表页面
     */
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            commissionOrderService.deleteById(id);
            redirectAttributes.addFlashAttribute("message", "分成单已删除");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/commission/list";
    }
}

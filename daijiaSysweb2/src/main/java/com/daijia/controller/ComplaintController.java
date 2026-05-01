package com.daijia.controller;

import com.daijia.entity.Complaint;
import com.daijia.service.ComplaintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

/**
 * 投诉单控制器
 * 处理投诉单相关的请求
 */
@Controller
@RequestMapping("/complaint")
public class ComplaintController {

    @Autowired
    private ComplaintService complaintService;

    /**
     * 投诉单列表页面
     * @param model 模型
     * @return 投诉单列表视图
     */
    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("complaints", complaintService.findAll());
        return "complaint/list";
    }

    /**
     * 投诉单详情页面
     * @param id 投诉单ID
     * @param model 模型
     * @return 投诉单详情视图
     */
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Optional<Complaint> complaintOpt = complaintService.findById(id);
        if (complaintOpt.isPresent()) {
            model.addAttribute("complaint", complaintOpt.get());
            return "complaint/detail";
        }
        return "redirect:/complaint/list";
    }

    /**
     * 新增投诉单页面
     * @param model 模型
     * @return 新增投诉单视图
     */
    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("complaint", new Complaint());
        return "complaint/form";
    }

    /**
     * 编辑投诉单页面
     * @param id 投诉单ID
     * @param model 模型
     * @return 编辑投诉单视图
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        Optional<Complaint> complaintOpt = complaintService.findById(id);
        if (complaintOpt.isPresent()) {
            model.addAttribute("complaint", complaintOpt.get());
            return "complaint/form";
        }
        return "redirect:/complaint/list";
    }

    /**
     * 保存投诉单
     * @param complaint 投诉单信息
     * @param redirectAttributes 重定向属性
     * @return 重定向到列表页面
     */
    @PostMapping("/save")
    public String save(@ModelAttribute Complaint complaint, RedirectAttributes redirectAttributes) {
        try {
            complaintService.save(complaint);
            redirectAttributes.addFlashAttribute("message", complaint.getId() == null ? "投诉单创建成功" : "投诉单更新成功");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/complaint/list";
    }

    /**
     * 处理投诉单页面
     * @param id 投诉单ID
     * @param model 模型
     * @return 处理投诉单视图
     */
    @GetMapping("/handle/{id}")
    public String handlePage(@PathVariable Long id, Model model) {
        Optional<Complaint> complaintOpt = complaintService.findById(id);
        if (complaintOpt.isPresent()) {
            model.addAttribute("complaint", complaintOpt.get());
            return "complaint/handle";
        }
        return "redirect:/complaint/list";
    }

    /**
     * 处理投诉单
     * @param id 投诉单ID
     * @param status 状态
     * @param handleRemark 处理备注
     * @param creditDeduct 信用扣分
     * @param redirectAttributes 重定向属性
     * @return 重定向到列表页面
     */
    @PostMapping("/handle/{id}")
    public String handle(@PathVariable Long id,
                         @RequestParam String status,
                         @RequestParam(required = false) String handleRemark,
                         @RequestParam(required = false) Integer creditDeduct,
                         RedirectAttributes redirectAttributes) {
        try {
            complaintService.handleComplaint(id, status, handleRemark, creditDeduct);
            redirectAttributes.addFlashAttribute("message", "投诉单处理成功");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/complaint/list";
    }

    /**
     * 删除投诉单
     * @param id 投诉单ID
     * @param redirectAttributes 重定向属性
     * @return 重定向到列表页面
     */
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            complaintService.deleteById(id);
            redirectAttributes.addFlashAttribute("message", "投诉单已删除");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/complaint/list";
    }
}

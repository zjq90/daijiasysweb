package com.daijia.controller;

import com.daijia.entity.EmergencyHelp;
import com.daijia.service.EmergencyHelpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 安全求助控制器
 * 
 * 处理安全求助功能
 * 支持：
 * 1. 紧急求助按钮触发
 * 2. 定位与行程信息发送
 * 3. 全程录音
 * 4. 平台紧急提醒
 * 
 * @author daijia
 * @version 1.0.0
 */
@Controller
@RequestMapping("/emergency")
public class EmergencyHelpController {

    @Autowired
    private EmergencyHelpService emergencyHelpService;

    /**
     * 求助列表页面
     * 
     * @param model 模型对象
     * @param session 会话对象
     * @param page 页码
     * @param size 每页大小
     * @param status 状态筛选
     * @return 求助列表视图
     */
    @GetMapping("/list")
    public String list(Model model, HttpSession session,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "10") int size,
                       @RequestParam(required = false) Integer status) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/login";
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("createTime").descending());
        Page<EmergencyHelp> helpPage;
        
        if (status != null) {
            helpPage = emergencyHelpService.findAll(pageable);
        } else {
            helpPage = emergencyHelpService.findAll(pageable);
        }

        model.addAttribute("helpPage", helpPage);
        model.addAttribute("helpService", emergencyHelpService);
        model.addAttribute("currentPage", "emergency");
        model.addAttribute("status", status);

        return "emergency/list";
    }

    /**
     * 求助详情页面
     * 
     * @param id 求助ID
     * @param model 模型对象
     * @param session 会话对象
     * @return 求助详情视图
     */
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id, Model model, HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/login";
        }

        Optional<EmergencyHelp> helpOpt = emergencyHelpService.findById(id);
        if (helpOpt.isPresent()) {
            model.addAttribute("help", helpOpt.get());
            model.addAttribute("helpService", emergencyHelpService);
        } else {
            model.addAttribute("error", "求助记录不存在");
        }

        model.addAttribute("currentPage", "emergency");
        return "emergency/detail";
    }

    /**
     * 开始处理求助
     * 
     * @param id 求助ID
     * @param session 会话对象
     * @return JSON结果
     */
    @PostMapping("/start")
    @ResponseBody
    public Map<String, Object> startProcessing(@RequestParam Long id, HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        
        if (session.getAttribute("admin") == null) {
            result.put("success", false);
            result.put("message", "未登录");
            return result;
        }

        Long adminId = (Long) session.getAttribute("adminId");
        EmergencyHelp help = emergencyHelpService.startProcessing(id, adminId);
        
        if (help != null) {
            result.put("success", true);
            result.put("message", "已开始处理");
        } else {
            result.put("success", false);
            result.put("message", "处理失败");
        }
        
        return result;
    }

    /**
     * 完成处理求助
     * 
     * @param id 求助ID
     * @param handleResult 处理结果
     * @param session 会话对象
     * @return JSON结果
     */
    @PostMapping("/complete")
    @ResponseBody
    public Map<String, Object> completeProcessing(@RequestParam Long id,
                                                    @RequestParam String handleResult,
                                                    HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        
        if (session.getAttribute("admin") == null) {
            result.put("success", false);
            result.put("message", "未登录");
            return result;
        }

        EmergencyHelp help = emergencyHelpService.completeProcessing(id, handleResult);
        
        if (help != null) {
            result.put("success", true);
            result.put("message", "处理完成");
        } else {
            result.put("success", false);
            result.put("message", "处理失败");
        }
        
        return result;
    }

    /**
     * 关闭求助
     * 
     * @param id 求助ID
     * @param session 会话对象
     * @return JSON结果
     */
    @PostMapping("/close")
    @ResponseBody
    public Map<String, Object> closeHelp(@RequestParam Long id, HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        
        if (session.getAttribute("admin") == null) {
            result.put("success", false);
            result.put("message", "未登录");
            return result;
        }

        EmergencyHelp help = emergencyHelpService.closeHelp(id);
        
        if (help != null) {
            result.put("success", true);
            result.put("message", "已关闭");
        } else {
            result.put("success", false);
            result.put("message", "关闭失败");
        }
        
        return result;
    }

    /**
     * API：创建紧急求助（模拟用户/司机触发）
     * 
     * @param helpType 求助类型
     * @param orderId 订单ID（可选）
     * @param userId 用户ID（可选）
     * @param driverId 司机ID（可选）
     * @param latitude 纬度
     * @param longitude 经度
     * @param address 地址
     * @param content 求助内容
     * @param emergencyContacts 紧急联系人JSON
     * @param session 会话对象
     * @return JSON结果
     */
    @PostMapping("/api/create")
    @ResponseBody
    public Map<String, Object> createHelp(@RequestParam(defaultValue = "0") Integer helpType,
                                           @RequestParam(required = false) Long orderId,
                                           @RequestParam(required = false) Long userId,
                                           @RequestParam(required = false) Long driverId,
                                           @RequestParam(required = false) BigDecimal latitude,
                                           @RequestParam(required = false) BigDecimal longitude,
                                           @RequestParam(required = false) String address,
                                           @RequestParam(required = false) String content,
                                           @RequestParam(required = false) String emergencyContacts,
                                           HttpSession session) {
        Map<String, Object> result = new HashMap<>();

        EmergencyHelp help = new EmergencyHelp();
        help.setHelpType(helpType);
        help.setOrderId(orderId);
        help.setUserId(userId);
        help.setDriverId(driverId);
        help.setLatitude(latitude);
        help.setLongitude(longitude);
        help.setAddress(address);
        help.setContent(content);
        help.setEmergencyContacts(emergencyContacts);
        help.setStatus(0);

        EmergencyHelp savedHelp = emergencyHelpService.createHelp(help);
        
        if (savedHelp != null) {
            result.put("success", true);
            result.put("message", "求助已创建");
            result.put("helpNo", savedHelp.getHelpNo());
            result.put("helpId", savedHelp.getId());
        } else {
            result.put("success", false);
            result.put("message", "创建失败");
        }

        return result;
    }

    /**
     * 获取待处理和处理中的求助
     * 
     * @param session 会话对象
     * @return JSON结果
     */
    @GetMapping("/api/pending")
    @ResponseBody
    public Map<String, Object> getPending(HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        
        if (session.getAttribute("admin") == null) {
            result.put("success", false);
            result.put("message", "未登录");
            return result;
        }

        List<EmergencyHelp> pendingList = emergencyHelpService.findPendingAndProcessing();
        result.put("success", true);
        result.put("list", pendingList);
        result.put("count", pendingList.size());
        
        return result;
    }

    /**
     * 获取求助统计
     * 
     * @param session 会话对象
     * @return JSON结果
     */
    @GetMapping("/api/statistics")
    @ResponseBody
    public Map<String, Object> getStatistics(HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        
        if (session.getAttribute("admin") == null) {
            result.put("success", false);
            return result;
        }

        result.put("success", true);
        result.put("pending", emergencyHelpService.countByStatus(0));
        result.put("processing", emergencyHelpService.countByStatus(1));
        result.put("completed", emergencyHelpService.countByStatus(2));
        result.put("closed", emergencyHelpService.countByStatus(3));
        
        return result;
    }
}

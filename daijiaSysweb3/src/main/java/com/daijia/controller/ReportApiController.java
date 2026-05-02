package com.daijia.controller;

import com.daijia.entity.OperationReport;
import com.daijia.service.ComplaintService;
import com.daijia.service.OperationReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 运营报表API控制器
 * 提供运营报表相关的RESTful接口：日订单量、成交率、司机收入、投诉率等
 *
 * @author daijia
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/report")
public class ReportApiController {

    @Autowired
    private OperationReportService operationReportService;

    @Autowired
    private ComplaintService complaintService;

    /**
     * 获取报表Dashboard数据
     * 包含今日数据、本月数据、订单趋势、收入趋势
     *
     * @return Dashboard数据
     */
    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> dashboard() {
        Map<String, Object> result = new HashMap<>();
        
        // 获取综合统计数据
        Map<String, Object> stats = operationReportService.getDashboardStats();
        result.put("stats", stats);
        
        // 获取投诉统计数据
        Map<String, Object> complaintStats = complaintService.getComplaintStats();
        result.put("complaintStats", complaintStats);
        
        return ResponseEntity.ok(result);
    }

    /**
     * 获取所有报表列表
     *
     * @return 报表列表
     */
    @GetMapping("/list")
    public ResponseEntity<List<OperationReport>> list() {
        List<OperationReport> reports = operationReportService.getAllReports();
        return ResponseEntity.ok(reports);
    }

    /**
     * 获取日报表列表
     *
     * @return 日报表列表
     */
    @GetMapping("/daily")
    public ResponseEntity<List<OperationReport>> dailyReports() {
        List<OperationReport> reports = operationReportService.getDailyReports();
        return ResponseEntity.ok(reports);
    }

    /**
     * 根据ID获取报表详情
     *
     * @param id 报表ID
     * @return 报表详情
     */
    @GetMapping("/detail/{id}")
    public ResponseEntity<OperationReport> detail(@PathVariable Long id) {
        OperationReport report = operationReportService.getReportById(id);
        if (report == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(report);
    }

    /**
     * 生成指定日期的日报表
     *
     * @param date 报表日期
     * @return 生成结果
     */
    @PostMapping("/generate/daily")
    public ResponseEntity<Map<String, Object>> generateDaily(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        Map<String, Object> result = new HashMap<>();
        try {
            OperationReport report = operationReportService.generateDailyReport(date);
            result.put("success", true);
            result.put("message", "生成日报表成功");
            result.put("data", report);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "生成日报表失败：" + e.getMessage());
            return ResponseEntity.badRequest().body(result);
        }
    }

    /**
     * 生成过去N天的测试报表数据
     *
     * @param days 天数
     * @return 生成结果
     */
    @PostMapping("/generate/test")
    public ResponseEntity<Map<String, Object>> generateTestReports(
            @RequestParam(defaultValue = "7") int days) {
        Map<String, Object> result = new HashMap<>();
        try {
            operationReportService.generateTestReports(days);
            result.put("success", true);
            result.put("message", "成功生成" + days + "天的测试报表数据");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "生成测试数据失败：" + e.getMessage());
            return ResponseEntity.badRequest().body(result);
        }
    }

    /**
     * 获取订单趋势数据
     *
     * @return 订单趋势数据
     */
    @GetMapping("/trend/order")
    public ResponseEntity<List<Map<String, Object>>> orderTrend() {
        List<Map<String, Object>> trend = operationReportService.getOrderTrendData();
        return ResponseEntity.ok(trend);
    }

    /**
     * 获取收入趋势数据
     *
     * @return 收入趋势数据
     */
    @GetMapping("/trend/revenue")
    public ResponseEntity<List<Map<String, Object>>> revenueTrend() {
        List<Map<String, Object>> trend = operationReportService.getRevenueTrendData();
        return ResponseEntity.ok(trend);
    }

    /**
     * 获取投诉统计数据
     *
     * @return 投诉统计
     */
    @GetMapping("/complaint/stats")
    public ResponseEntity<Map<String, Object>> complaintStats() {
        Map<String, Object> stats = complaintService.getComplaintStats();
        return ResponseEntity.ok(stats);
    }

    /**
     * 删除报表
     *
     * @param id 报表ID
     * @return 删除结果
     */
    @PostMapping("/delete/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        boolean deleted = operationReportService.deleteReport(id);
        if (deleted) {
            result.put("success", true);
            result.put("message", "删除报表成功");
            return ResponseEntity.ok(result);
        } else {
            result.put("success", false);
            result.put("message", "报表不存在");
            return ResponseEntity.badRequest().body(result);
        }
    }
}

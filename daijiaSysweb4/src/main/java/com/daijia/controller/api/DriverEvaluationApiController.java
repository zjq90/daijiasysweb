package com.daijia.controller.api;

import com.daijia.entity.DriverEvaluation;
import com.daijia.service.DriverEvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 司机评价记录API控制器
 * 处理司机评价记录的增删改查API请求
 */
@RestController
@RequestMapping("/api/driver-evaluations")
public class DriverEvaluationApiController {

    @Autowired
    private DriverEvaluationService driverEvaluationService;

    /**
     * 获取所有有效评价记录
     */
    @GetMapping
    public ResponseEntity<List<DriverEvaluation>> list() {
        return ResponseEntity.ok(driverEvaluationService.findAllValid());
    }

    /**
     * 根据ID获取评价详情
     */
    @GetMapping("/{id}")
    public ResponseEntity<DriverEvaluation> getById(@PathVariable Long id) {
        return driverEvaluationService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 根据用户ID获取评价列表
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<DriverEvaluation>> getByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(driverEvaluationService.findByUserId(userId));
    }

    /**
     * 根据司机ID获取评价列表
     */
    @GetMapping("/driver/{driverId}")
    public ResponseEntity<List<DriverEvaluation>> getByDriverId(@PathVariable Long driverId) {
        return ResponseEntity.ok(driverEvaluationService.findByDriverId(driverId));
    }

    /**
     * 创建评价
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@RequestBody DriverEvaluation evaluation) {
        Map<String, Object> result = new HashMap<>();
        try {
            DriverEvaluation saved = driverEvaluationService.create(evaluation);
            result.put("success", true);
            result.put("data", saved);
            result.put("message", "评价创建成功");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(result);
        }
    }

    /**
     * 更新评价
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id, @RequestBody DriverEvaluation evaluation) {
        evaluation.setId(id);
        Map<String, Object> result = new HashMap<>();
        try {
            DriverEvaluation updated = driverEvaluationService.update(evaluation);
            result.put("success", true);
            result.put("data", updated);
            result.put("message", "评价更新成功");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(result);
        }
    }

    /**
     * 删除评价（逻辑删除）
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        try {
            driverEvaluationService.delete(id);
            result.put("success", true);
            result.put("message", "评价删除成功");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(result);
        }
    }

    /**
     * 获取有严重问题的评价记录
     */
    @GetMapping("/serious-issues")
    public ResponseEntity<List<DriverEvaluation>> getSeriousIssues() {
        return ResponseEntity.ok(driverEvaluationService.findEvaluationsWithSeriousIssues());
    }

    /**
     * 获取有醉酒记录的评价
     */
    @GetMapping("/drunk")
    public ResponseEntity<List<DriverEvaluation>> getDrunkEvaluations() {
        return ResponseEntity.ok(driverEvaluationService.findDrunkEvaluations());
    }

    /**
     * 获取有更改目的地记录的评价
     */
    @GetMapping("/destination-changed")
    public ResponseEntity<List<DriverEvaluation>> getDestinationChangedEvaluations() {
        return ResponseEntity.ok(driverEvaluationService.findDestinationChangedEvaluations());
    }

    /**
     * 获取有异常行为记录的评价
     */
    @GetMapping("/abnormal-behavior")
    public ResponseEntity<List<DriverEvaluation>> getAbnormalBehaviorEvaluations() {
        return ResponseEntity.ok(driverEvaluationService.findAbnormalBehaviorEvaluations());
    }

    /**
     * 统计用户的评价数量
     */
    @GetMapping("/count/user/{userId}")
    public ResponseEntity<Map<String, Object>> countByUserId(@PathVariable Long userId) {
        Map<String, Object> result = new HashMap<>();
        result.put("userId", userId);
        result.put("count", driverEvaluationService.countByUserId(userId));
        result.put("seriousIssuesCount", driverEvaluationService.countSeriousIssuesByUserId(userId));
        return ResponseEntity.ok(result);
    }
}

package com.daijia.controller.api;

import com.daijia.entity.UserEvaluation;
import com.daijia.service.UserEvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户评价记录API控制器
 * 处理用户评价记录的增删改查API请求
 */
@RestController
@RequestMapping("/api/user-evaluations")
public class UserEvaluationApiController {

    @Autowired
    private UserEvaluationService userEvaluationService;

    /**
     * 获取所有有效评价记录
     */
    @GetMapping
    public ResponseEntity<List<UserEvaluation>> list() {
        return ResponseEntity.ok(userEvaluationService.findAllValid());
    }

    /**
     * 根据ID获取评价详情
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserEvaluation> getById(@PathVariable Long id) {
        return userEvaluationService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 根据司机ID获取评价列表
     */
    @GetMapping("/driver/{driverId}")
    public ResponseEntity<List<UserEvaluation>> getByDriverId(@PathVariable Long driverId) {
        return ResponseEntity.ok(userEvaluationService.findByDriverId(driverId));
    }

    /**
     * 根据用户ID获取评价列表
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserEvaluation>> getByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(userEvaluationService.findByUserId(userId));
    }

    /**
     * 创建评价
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@RequestBody UserEvaluation evaluation) {
        Map<String, Object> result = new HashMap<>();
        try {
            UserEvaluation saved = userEvaluationService.create(evaluation);
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
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id, @RequestBody UserEvaluation evaluation) {
        evaluation.setId(id);
        Map<String, Object> result = new HashMap<>();
        try {
            UserEvaluation updated = userEvaluationService.update(evaluation);
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
            userEvaluationService.delete(id);
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
     * 获取高评分评价
     */
    @GetMapping("/high-rating")
    public ResponseEntity<List<UserEvaluation>> getHighRating() {
        return ResponseEntity.ok(userEvaluationService.findHighRatingEvaluations());
    }

    /**
     * 获取低评分评价
     */
    @GetMapping("/low-rating")
    public ResponseEntity<List<UserEvaluation>> getLowRating() {
        return ResponseEntity.ok(userEvaluationService.findLowRatingEvaluations());
    }

    /**
     * 统计司机的评价数量
     */
    @GetMapping("/count/driver/{driverId}")
    public ResponseEntity<Map<String, Object>> countByDriverId(@PathVariable Long driverId) {
        Map<String, Object> result = new HashMap<>();
        result.put("driverId", driverId);
        result.put("count", userEvaluationService.countByDriverId(driverId));
        return ResponseEntity.ok(result);
    }
}

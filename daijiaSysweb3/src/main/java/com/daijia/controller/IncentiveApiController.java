package com.daijia.controller;

import com.daijia.entity.IncentiveActivity;
import com.daijia.service.IncentiveActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 激励奖励活动API控制器
 * 提供激励奖励活动相关的RESTful接口
 *
 * @author daijia
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/incentive")
public class IncentiveApiController {

    @Autowired
    private IncentiveActivityService incentiveActivityService;

    /**
     * 获取所有活动列表
     *
     * @return 活动列表
     */
    @GetMapping("/list")
    public ResponseEntity<List<IncentiveActivity>> list() {
        List<IncentiveActivity> activities = incentiveActivityService.getAllActivities();
        return ResponseEntity.ok(activities);
    }

    /**
     * 获取进行中的活动列表
     *
     * @return 活动列表
     */
    @GetMapping("/active")
    public ResponseEntity<List<IncentiveActivity>> active() {
        List<IncentiveActivity> activities = incentiveActivityService.getActiveActivities();
        return ResponseEntity.ok(activities);
    }

    /**
     * 根据ID获取活动详情
     *
     * @param id 活动ID
     * @return 活动详情
     */
    @GetMapping("/detail/{id}")
    public ResponseEntity<IncentiveActivity> detail(@PathVariable Long id) {
        IncentiveActivity activity = incentiveActivityService.getActivityById(id);
        if (activity == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(activity);
    }

    /**
     * 创建新活动
     *
     * @param activity 活动信息
     * @return 创建结果
     */
    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> create(@RequestBody IncentiveActivity activity) {
        Map<String, Object> result = new HashMap<>();
        try {
            IncentiveActivity savedActivity = incentiveActivityService.createActivity(activity);
            result.put("success", true);
            result.put("message", "创建激励活动成功");
            result.put("data", savedActivity);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "创建激励活动失败：" + e.getMessage());
            return ResponseEntity.badRequest().body(result);
        }
    }

    /**
     * 更新活动信息
     *
     * @param activity 活动信息
     * @return 更新结果
     */
    @PostMapping("/update")
    public ResponseEntity<Map<String, Object>> update(@RequestBody IncentiveActivity activity) {
        Map<String, Object> result = new HashMap<>();
        try {
            IncentiveActivity existingActivity = incentiveActivityService.getActivityById(activity.getId());
            if (existingActivity == null) {
                result.put("success", false);
                result.put("message", "活动不存在");
                return ResponseEntity.badRequest().body(result);
            }
            IncentiveActivity updatedActivity = incentiveActivityService.updateActivity(activity);
            result.put("success", true);
            result.put("message", "更新激励活动成功");
            result.put("data", updatedActivity);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "更新激励活动失败：" + e.getMessage());
            return ResponseEntity.badRequest().body(result);
        }
    }

    /**
     * 删除活动
     *
     * @param id 活动ID
     * @return 删除结果
     */
    @PostMapping("/delete/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        boolean deleted = incentiveActivityService.deleteActivity(id);
        if (deleted) {
            result.put("success", true);
            result.put("message", "删除激励活动成功");
            return ResponseEntity.ok(result);
        } else {
            result.put("success", false);
            result.put("message", "活动不存在");
            return ResponseEntity.badRequest().body(result);
        }
    }

    /**
     * 启动活动
     *
     * @param id 活动ID
     * @return 结果
     */
    @PostMapping("/start/{id}")
    public ResponseEntity<Map<String, Object>> start(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        IncentiveActivity activity = incentiveActivityService.startActivity(id);
        if (activity != null) {
            result.put("success", true);
            result.put("message", "启动活动成功");
            result.put("data", activity);
            return ResponseEntity.ok(result);
        } else {
            result.put("success", false);
            result.put("message", "启动失败：活动状态不正确");
            return ResponseEntity.badRequest().body(result);
        }
    }

    /**
     * 暂停活动
     *
     * @param id 活动ID
     * @return 结果
     */
    @PostMapping("/pause/{id}")
    public ResponseEntity<Map<String, Object>> pause(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        IncentiveActivity activity = incentiveActivityService.pauseActivity(id);
        if (activity != null) {
            result.put("success", true);
            result.put("message", "暂停活动成功");
            result.put("data", activity);
            return ResponseEntity.ok(result);
        } else {
            result.put("success", false);
            result.put("message", "暂停失败：活动状态不正确");
            return ResponseEntity.badRequest().body(result);
        }
    }

    /**
     * 恢复活动
     *
     * @param id 活动ID
     * @return 结果
     */
    @PostMapping("/resume/{id}")
    public ResponseEntity<Map<String, Object>> resume(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        IncentiveActivity activity = incentiveActivityService.resumeActivity(id);
        if (activity != null) {
            result.put("success", true);
            result.put("message", "恢复活动成功");
            result.put("data", activity);
            return ResponseEntity.ok(result);
        } else {
            result.put("success", false);
            result.put("message", "恢复失败：活动状态不正确");
            return ResponseEntity.badRequest().body(result);
        }
    }

    /**
     * 结束活动
     *
     * @param id 活动ID
     * @return 结果
     */
    @PostMapping("/end/{id}")
    public ResponseEntity<Map<String, Object>> end(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        IncentiveActivity activity = incentiveActivityService.endActivity(id);
        if (activity != null) {
            result.put("success", true);
            result.put("message", "结束活动成功");
            result.put("data", activity);
            return ResponseEntity.ok(result);
        } else {
            result.put("success", false);
            result.put("message", "结束失败：活动状态不正确");
            return ResponseEntity.badRequest().body(result);
        }
    }

    /**
     * 获取活动统计数据
     *
     * @return 统计数据
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> stats() {
        Map<String, Object> stats = incentiveActivityService.getActivityStats();
        return ResponseEntity.ok(stats);
    }

    /**
     * 根据状态获取活动列表
     *
     * @param status 活动状态
     * @return 活动列表
     */
    @GetMapping("/list/status/{status}")
    public ResponseEntity<List<IncentiveActivity>> listByStatus(@PathVariable String status) {
        List<IncentiveActivity> activities = incentiveActivityService.getActivitiesByStatus(status);
        return ResponseEntity.ok(activities);
    }

    /**
     * 根据类型获取活动列表
     *
     * @param type 活动类型
     * @return 活动列表
     */
    @GetMapping("/list/type/{type}")
    public ResponseEntity<List<IncentiveActivity>> listByType(@PathVariable String type) {
        List<IncentiveActivity> activities = incentiveActivityService.getActivitiesByType(type);
        return ResponseEntity.ok(activities);
    }

    /**
     * 生成测试活动数据
     *
     * @param count 数量
     * @return 结果
     */
    @PostMapping("/generate/test")
    public ResponseEntity<Map<String, Object>> generateTest(
            @RequestParam(defaultValue = "5") int count) {
        Map<String, Object> result = new HashMap<>();
        try {
            incentiveActivityService.generateTestActivities(count);
            result.put("success", true);
            result.put("message", "成功生成" + count + "条测试激励活动数据");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "生成测试数据失败：" + e.getMessage());
            return ResponseEntity.badRequest().body(result);
        }
    }
}

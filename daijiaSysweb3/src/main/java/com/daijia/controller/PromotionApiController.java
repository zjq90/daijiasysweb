package com.daijia.controller;

import com.daijia.entity.PromotionActivity;
import com.daijia.service.PromotionActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 优惠活动API控制器
 * 提供优惠活动相关的RESTful接口
 *
 * @author daijia
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/promotion")
public class PromotionApiController {

    @Autowired
    private PromotionActivityService promotionActivityService;

    /**
     * 获取所有活动列表
     *
     * @return 活动列表
     */
    @GetMapping("/list")
    public ResponseEntity<List<PromotionActivity>> list() {
        List<PromotionActivity> activities = promotionActivityService.getAllActivities();
        return ResponseEntity.ok(activities);
    }

    /**
     * 获取进行中的活动列表
     *
     * @return 活动列表
     */
    @GetMapping("/active")
    public ResponseEntity<List<PromotionActivity>> active() {
        List<PromotionActivity> activities = promotionActivityService.getActiveActivities();
        return ResponseEntity.ok(activities);
    }

    /**
     * 获取即将过期的活动列表
     *
     * @return 活动列表
     */
    @GetMapping("/expiring")
    public ResponseEntity<List<PromotionActivity>> expiring() {
        List<PromotionActivity> activities = promotionActivityService.getExpiringActivities();
        return ResponseEntity.ok(activities);
    }

    /**
     * 获取新人专享活动列表
     *
     * @return 活动列表
     */
    @GetMapping("/new-user")
    public ResponseEntity<List<PromotionActivity>> newUser() {
        List<PromotionActivity> activities = promotionActivityService.getNewUserActivities();
        return ResponseEntity.ok(activities);
    }

    /**
     * 根据ID获取活动详情
     *
     * @param id 活动ID
     * @return 活动详情
     */
    @GetMapping("/detail/{id}")
    public ResponseEntity<PromotionActivity> detail(@PathVariable Long id) {
        PromotionActivity activity = promotionActivityService.getActivityById(id);
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
    public ResponseEntity<Map<String, Object>> create(@RequestBody PromotionActivity activity) {
        Map<String, Object> result = new HashMap<>();
        try {
            PromotionActivity savedActivity = promotionActivityService.createActivity(activity);
            result.put("success", true);
            result.put("message", "创建优惠活动成功");
            result.put("data", savedActivity);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "创建优惠活动失败：" + e.getMessage());
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
    public ResponseEntity<Map<String, Object>> update(@RequestBody PromotionActivity activity) {
        Map<String, Object> result = new HashMap<>();
        try {
            PromotionActivity existingActivity = promotionActivityService.getActivityById(activity.getId());
            if (existingActivity == null) {
                result.put("success", false);
                result.put("message", "活动不存在");
                return ResponseEntity.badRequest().body(result);
            }
            PromotionActivity updatedActivity = promotionActivityService.updateActivity(activity);
            result.put("success", true);
            result.put("message", "更新优惠活动成功");
            result.put("data", updatedActivity);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "更新优惠活动失败：" + e.getMessage());
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
        boolean deleted = promotionActivityService.deleteActivity(id);
        if (deleted) {
            result.put("success", true);
            result.put("message", "删除优惠活动成功");
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
        PromotionActivity activity = promotionActivityService.startActivity(id);
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
        PromotionActivity activity = promotionActivityService.pauseActivity(id);
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
        PromotionActivity activity = promotionActivityService.resumeActivity(id);
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
        PromotionActivity activity = promotionActivityService.endActivity(id);
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
        Map<String, Object> stats = promotionActivityService.getActivityStats();
        return ResponseEntity.ok(stats);
    }

    /**
     * 计算优惠金额
     *
     * @param params 包含activityId和orderAmount
     * @return 优惠金额
     */
    @PostMapping("/calculate")
    public ResponseEntity<Map<String, Object>> calculateDiscount(@RequestBody Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        try {
            Long activityId = Long.valueOf(params.get("activityId").toString());
            BigDecimal orderAmount = new BigDecimal(params.get("orderAmount").toString());
            BigDecimal discount = promotionActivityService.calculateDiscount(activityId, orderAmount);
            result.put("success", true);
            result.put("discountAmount", discount);
            result.put("payableAmount", orderAmount.subtract(discount));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "计算优惠失败：" + e.getMessage());
            return ResponseEntity.badRequest().body(result);
        }
    }

    /**
     * 根据状态获取活动列表
     *
     * @param status 活动状态
     * @return 活动列表
     */
    @GetMapping("/list/status/{status}")
    public ResponseEntity<List<PromotionActivity>> listByStatus(@PathVariable String status) {
        List<PromotionActivity> activities = promotionActivityService.getActivitiesByStatus(status);
        return ResponseEntity.ok(activities);
    }

    /**
     * 根据类型获取活动列表
     *
     * @param type 活动类型
     * @return 活动列表
     */
    @GetMapping("/list/type/{type}")
    public ResponseEntity<List<PromotionActivity>> listByType(@PathVariable String type) {
        List<PromotionActivity> activities = promotionActivityService.getActivitiesByType(type);
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
            promotionActivityService.generateTestActivities(count);
            result.put("success", true);
            result.put("message", "成功生成" + count + "条测试优惠活动数据");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "生成测试数据失败：" + e.getMessage());
            return ResponseEntity.badRequest().body(result);
        }
    }
}

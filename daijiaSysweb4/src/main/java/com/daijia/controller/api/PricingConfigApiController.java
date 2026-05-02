package com.daijia.controller.api;

import com.daijia.entity.PricingConfig;
import com.daijia.service.PricingConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 计费配置API控制器
 * 处理计费配置的增删改查API请求
 */
@RestController
@RequestMapping("/api/pricing-configs")
public class PricingConfigApiController {

    @Autowired
    private PricingConfigService pricingConfigService;

    /**
     * 获取所有配置
     */
    @GetMapping
    public ResponseEntity<List<PricingConfig>> list() {
        return ResponseEntity.ok(pricingConfigService.findAll());
    }

    /**
     * 获取所有启用的配置
     */
    @GetMapping("/enabled")
    public ResponseEntity<List<PricingConfig>> listEnabled() {
        return ResponseEntity.ok(pricingConfigService.findAllEnabled());
    }

    /**
     * 根据ID获取配置详情
     */
    @GetMapping("/{id}")
    public ResponseEntity<PricingConfig> getById(@PathVariable Long id) {
        return pricingConfigService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 获取默认配置
     */
    @GetMapping("/default")
    public ResponseEntity<PricingConfig> getDefault() {
        return pricingConfigService.findDefaultConfig()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 创建配置
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@RequestBody PricingConfig config) {
        Map<String, Object> result = new HashMap<>();
        try {
            PricingConfig saved = pricingConfigService.create(config);
            result.put("success", true);
            result.put("data", saved);
            result.put("message", "配置创建成功");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(result);
        }
    }

    /**
     * 更新配置
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id, @RequestBody PricingConfig config) {
        config.setId(id);
        Map<String, Object> result = new HashMap<>();
        try {
            PricingConfig updated = pricingConfigService.update(config);
            result.put("success", true);
            result.put("data", updated);
            result.put("message", "配置更新成功");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(result);
        }
    }

    /**
     * 删除配置
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        try {
            pricingConfigService.delete(id);
            result.put("success", true);
            result.put("message", "配置删除成功");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(result);
        }
    }

    /**
     * 设置为默认配置
     */
    @PostMapping("/{id}/set-default")
    public ResponseEntity<Map<String, Object>> setAsDefault(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        try {
            PricingConfig config = pricingConfigService.setAsDefault(id);
            result.put("success", true);
            result.put("data", config);
            result.put("message", "已设置为默认配置");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(result);
        }
    }

    /**
     * 启用/禁用配置
     */
    @PostMapping("/{id}/toggle-status")
    public ResponseEntity<Map<String, Object>> toggleStatus(@PathVariable Long id, @RequestParam boolean enabled) {
        Map<String, Object> result = new HashMap<>();
        try {
            PricingConfig config = pricingConfigService.toggleStatus(id, enabled);
            result.put("success", true);
            result.put("data", config);
            result.put("message", enabled ? "配置已启用" : "配置已禁用");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(result);
        }
    }

    /**
     * 使用默认配置计算费用
     */
    @PostMapping("/calculate")
    public ResponseEntity<Map<String, Object>> calculatePrice(
            @RequestParam(required = false) Long configId,
            @RequestParam BigDecimal distance,
            @RequestParam(required = false, defaultValue = "0") Integer waitTime,
            @RequestParam(required = false) String currentTime) {
        
        Map<String, Object> result = new HashMap<>();
        try {
            LocalTime time = currentTime != null ? LocalTime.parse(currentTime) : LocalTime.now();
            BigDecimal totalAmount;
            
            if (configId != null) {
                totalAmount = pricingConfigService.calculatePrice(configId, distance, waitTime, time);
            } else {
                totalAmount = pricingConfigService.calculatePriceWithDefault(distance, waitTime, time);
            }
            
            result.put("success", true);
            result.put("totalAmount", totalAmount);
            result.put("distance", distance);
            result.put("waitTime", waitTime);
            result.put("currentTime", time.toString());
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(result);
        }
    }

    /**
     * 统计启用的配置数量
     */
    @GetMapping("/count/enabled")
    public ResponseEntity<Map<String, Object>> countEnabled() {
        Map<String, Object> result = new HashMap<>();
        result.put("count", pricingConfigService.countEnabled());
        return ResponseEntity.ok(result);
    }
}

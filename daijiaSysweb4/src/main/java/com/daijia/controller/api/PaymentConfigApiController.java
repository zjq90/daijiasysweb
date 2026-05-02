package com.daijia.controller.api;

import com.daijia.entity.PaymentConfig;
import com.daijia.service.PaymentConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 支付方式配置API控制器
 * 处理支付方式配置的增删改查API请求
 */
@RestController
@RequestMapping("/api/payment-configs")
public class PaymentConfigApiController {

    @Autowired
    private PaymentConfigService paymentConfigService;

    /**
     * 获取所有配置
     */
    @GetMapping
    public ResponseEntity<List<PaymentConfig>> list() {
        return ResponseEntity.ok(paymentConfigService.findAll());
    }

    /**
     * 获取所有启用的配置
     */
    @GetMapping("/enabled")
    public ResponseEntity<List<PaymentConfig>> listEnabled() {
        return ResponseEntity.ok(paymentConfigService.findAllEnabled());
    }

    /**
     * 获取支持预付模式的配置
     */
    @GetMapping("/prepay")
    public ResponseEntity<List<PaymentConfig>> listPrepayEnabled() {
        return ResponseEntity.ok(paymentConfigService.findPrepayEnabledConfigs());
    }

    /**
     * 获取支持后付模式的配置
     */
    @GetMapping("/postpay")
    public ResponseEntity<List<PaymentConfig>> listPostpayEnabled() {
        return ResponseEntity.ok(paymentConfigService.findPostpayEnabledConfigs());
    }

    /**
     * 根据ID获取配置详情
     */
    @GetMapping("/{id}")
    public ResponseEntity<PaymentConfig> getById(@PathVariable Long id) {
        return paymentConfigService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 根据支付类型获取配置
     */
    @GetMapping("/type/{paymentType}")
    public ResponseEntity<PaymentConfig> getByPaymentType(@PathVariable String paymentType) {
        return paymentConfigService.findByPaymentType(paymentType)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 创建配置
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@RequestBody PaymentConfig config) {
        Map<String, Object> result = new HashMap<>();
        try {
            PaymentConfig saved = paymentConfigService.create(config);
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
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id, @RequestBody PaymentConfig config) {
        config.setId(id);
        Map<String, Object> result = new HashMap<>();
        try {
            PaymentConfig updated = paymentConfigService.update(config);
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
            paymentConfigService.delete(id);
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
     * 启用/禁用配置
     */
    @PostMapping("/{id}/toggle-status")
    public ResponseEntity<Map<String, Object>> toggleStatus(@PathVariable Long id, @RequestParam boolean enabled) {
        Map<String, Object> result = new HashMap<>();
        try {
            PaymentConfig config = paymentConfigService.toggleStatus(id, enabled);
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
     * 检查支付类型是否支持预付模式
     */
    @GetMapping("/supports-prepay/{paymentType}")
    public ResponseEntity<Map<String, Object>> supportsPrepay(@PathVariable String paymentType) {
        Map<String, Object> result = new HashMap<>();
        result.put("paymentType", paymentType);
        result.put("supportsPrepay", paymentConfigService.supportsPrepay(paymentType));
        return ResponseEntity.ok(result);
    }

    /**
     * 检查支付类型是否支持后付模式
     */
    @GetMapping("/supports-postpay/{paymentType}")
    public ResponseEntity<Map<String, Object>> supportsPostpay(@PathVariable String paymentType) {
        Map<String, Object> result = new HashMap<>();
        result.put("paymentType", paymentType);
        result.put("supportsPostpay", paymentConfigService.supportsPostpay(paymentType));
        return ResponseEntity.ok(result);
    }

    /**
     * 模拟支付（测试用）
     */
    @PostMapping("/simulate-payment")
    public ResponseEntity<Map<String, Object>> simulatePayment(
            @RequestParam String paymentType,
            @RequestParam String payMode,
            @RequestParam java.math.BigDecimal amount,
            @RequestParam(required = false) Long orderId) {
        
        Map<String, Object> result = new HashMap<>();
        try {
            // 验证支付类型是否支持该支付模式
            if ("PREPAY".equals(payMode) && !paymentConfigService.supportsPrepay(paymentType)) {
                throw new RuntimeException("该支付方式不支持预付模式");
            }
            if ("POSTPAY".equals(payMode) && !paymentConfigService.supportsPostpay(paymentType)) {
                throw new RuntimeException("该支付方式不支持后付模式");
            }

            // 模拟支付处理
            result.put("success", true);
            result.put("paymentType", paymentType);
            result.put("payMode", payMode);
            result.put("amount", amount);
            result.put("orderId", orderId);
            result.put("transactionId", "TXN" + System.currentTimeMillis());
            result.put("status", "SUCCESS");
            result.put("message", "支付成功（模拟）");
            result.put("timestamp", java.time.LocalDateTime.now().toString());

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
        result.put("count", paymentConfigService.countEnabled());
        return ResponseEntity.ok(result);
    }
}

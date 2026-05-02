package com.daijia.controller.api;

import com.daijia.entity.Order;
import com.daijia.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 订单API控制器
 * 处理订单的增删改查API请求
 */
@RestController
@RequestMapping("/api/orders")
public class OrderApiController {

    @Autowired
    private OrderService orderService;

    /**
     * 获取所有订单
     */
    @GetMapping
    public ResponseEntity<List<Order>> list() {
        return ResponseEntity.ok(orderService.findAll());
    }

    /**
     * 根据ID获取订单详情
     */
    @GetMapping("/{id}")
    public ResponseEntity<Order> getById(@PathVariable Long id) {
        return orderService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 根据订单号获取订单
     */
    @GetMapping("/order-no/{orderNo}")
    public ResponseEntity<Order> getByOrderNo(@PathVariable String orderNo) {
        return orderService.findByOrderNo(orderNo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 根据用户ID获取订单列表
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(orderService.findByUserId(userId));
    }

    /**
     * 根据司机ID获取订单列表
     */
    @GetMapping("/driver/{driverId}")
    public ResponseEntity<List<Order>> getByDriverId(@PathVariable Long driverId) {
        return ResponseEntity.ok(orderService.findByDriverId(driverId));
    }

    /**
     * 根据状态获取订单列表
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Order>> getByStatus(@PathVariable Integer status) {
        return ResponseEntity.ok(orderService.findByStatus(status));
    }

    /**
     * 获取待接单订单
     */
    @GetMapping("/pending")
    public ResponseEntity<List<Order>> getPendingOrders() {
        return ResponseEntity.ok(orderService.findPendingOrders());
    }

    /**
     * 获取进行中的订单
     */
    @GetMapping("/in-progress")
    public ResponseEntity<List<Order>> getInProgressOrders() {
        return ResponseEntity.ok(orderService.findInProgressOrders());
    }

    /**
     * 创建订单
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@RequestBody Order order) {
        Map<String, Object> result = new HashMap<>();
        try {
            Order saved = orderService.create(order);
            result.put("success", true);
            result.put("data", saved);
            result.put("message", "订单创建成功");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(result);
        }
    }

    /**
     * 司机接单
     */
    @PostMapping("/{id}/accept")
    public ResponseEntity<Map<String, Object>> acceptOrder(
            @PathVariable Long id,
            @RequestParam Long driverId) {
        Map<String, Object> result = new HashMap<>();
        try {
            Order updated = orderService.acceptOrder(id, driverId);
            result.put("success", true);
            result.put("data", updated);
            result.put("message", "接单成功");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(result);
        }
    }

    /**
     * 开始行程
     */
    @PostMapping("/{id}/start-trip")
    public ResponseEntity<Map<String, Object>> startTrip(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        try {
            Order updated = orderService.startTrip(id);
            result.put("success", true);
            result.put("data", updated);
            result.put("message", "行程开始");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(result);
        }
    }

    /**
     * 完成订单
     */
    @PostMapping("/{id}/complete")
    public ResponseEntity<Map<String, Object>> completeOrder(
            @PathVariable Long id,
            @RequestParam BigDecimal distance,
            @RequestParam Integer duration,
            @RequestParam(required = false, defaultValue = "0") Integer waitTime) {
        Map<String, Object> result = new HashMap<>();
        try {
            Order updated = orderService.completeOrder(id, distance, duration, waitTime);
            result.put("success", true);
            result.put("data", updated);
            result.put("message", "订单完成");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(result);
        }
    }

    /**
     * 取消订单
     */
    @PostMapping("/{id}/cancel")
    public ResponseEntity<Map<String, Object>> cancelOrder(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        try {
            Order updated = orderService.cancelOrder(id);
            result.put("success", true);
            result.put("data", updated);
            result.put("message", "订单已取消");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(result);
        }
    }

    /**
     * 订单支付
     */
    @PostMapping("/{id}/pay")
    public ResponseEntity<Map<String, Object>> payOrder(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        try {
            Order updated = orderService.payOrder(id);
            result.put("success", true);
            result.put("data", updated);
            result.put("message", "支付成功");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(result);
        }
    }

    /**
     * 获取用户的待评价订单
     */
    @GetMapping("/user/{userId}/pending-evaluation")
    public ResponseEntity<List<Order>> getUserPendingEvaluationOrders(@PathVariable Long userId) {
        return ResponseEntity.ok(orderService.findUserPendingEvaluationOrders(userId));
    }

    /**
     * 获取司机的待评价订单
     */
    @GetMapping("/driver/{driverId}/pending-evaluation")
    public ResponseEntity<List<Order>> getDriverPendingEvaluationOrders(@PathVariable Long driverId) {
        return ResponseEntity.ok(orderService.findDriverPendingEvaluationOrders(driverId));
    }

    /**
     * 删除订单
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        try {
            orderService.delete(id);
            result.put("success", true);
            result.put("message", "订单删除成功");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(result);
        }
    }

    /**
     * 统计各状态的订单数量
     */
    @GetMapping("/count/status/{status}")
    public ResponseEntity<Map<String, Object>> countByStatus(@PathVariable Integer status) {
        Map<String, Object> result = new HashMap<>();
        result.put("status", status);
        result.put("count", orderService.countByStatus(status));
        return ResponseEntity.ok(result);
    }

    /**
     * 获取最新的N个订单
     */
    @GetMapping("/latest")
    public ResponseEntity<List<Order>> getLatestOrders(
            @RequestParam(required = false, defaultValue = "10") Integer limit) {
        return ResponseEntity.ok(orderService.findLatestOrders(limit));
    }
}

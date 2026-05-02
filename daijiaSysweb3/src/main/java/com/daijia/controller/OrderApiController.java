package com.daijia.controller;

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
 * 提供订单相关的RESTful接口
 *
 * @author daijia
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/order")
public class OrderApiController {

    @Autowired
    private OrderService orderService;

    /**
     * 获取所有订单列表
     *
     * @return 订单列表
     */
    @GetMapping("/list")
    public ResponseEntity<List<Order>> list() {
        List<Order> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    /**
     * 根据ID获取订单详情
     *
     * @param id 订单ID
     * @return 订单详情
     */
    @GetMapping("/detail/{id}")
    public ResponseEntity<Order> detail(@PathVariable Long id) {
        Order order = orderService.getOrderById(id);
        if (order == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(order);
    }

    /**
     * 创建新订单
     *
     * @param order 订单信息
     * @return 创建结果
     */
    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> create(@RequestBody Order order) {
        Map<String, Object> result = new HashMap<>();
        try {
            Order savedOrder = orderService.createOrder(order);
            result.put("success", true);
            result.put("message", "创建订单成功");
            result.put("data", savedOrder);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "创建订单失败：" + e.getMessage());
            return ResponseEntity.badRequest().body(result);
        }
    }

    /**
     * 更新订单信息
     *
     * @param order 订单信息
     * @return 更新结果
     */
    @PostMapping("/update")
    public ResponseEntity<Map<String, Object>> update(@RequestBody Order order) {
        Map<String, Object> result = new HashMap<>();
        try {
            Order existingOrder = orderService.getOrderById(order.getId());
            if (existingOrder == null) {
                result.put("success", false);
                result.put("message", "订单不存在");
                return ResponseEntity.badRequest().body(result);
            }
            Order updatedOrder = orderService.updateOrder(order);
            result.put("success", true);
            result.put("message", "更新订单成功");
            result.put("data", updatedOrder);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "更新订单失败：" + e.getMessage());
            return ResponseEntity.badRequest().body(result);
        }
    }

    /**
     * 删除订单
     *
     * @param id 订单ID
     * @return 删除结果
     */
    @PostMapping("/delete/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        boolean deleted = orderService.deleteOrder(id);
        if (deleted) {
            result.put("success", true);
            result.put("message", "删除订单成功");
            return ResponseEntity.ok(result);
        } else {
            result.put("success", false);
            result.put("message", "订单不存在");
            return ResponseEntity.badRequest().body(result);
        }
    }

    /**
     * 接单
     *
     * @param params 包含orderId和driverId
     * @return 接单结果
     */
    @PostMapping("/accept")
    public ResponseEntity<Map<String, Object>> accept(@RequestBody Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        try {
            Long orderId = Long.valueOf(params.get("orderId").toString());
            Long driverId = Long.valueOf(params.get("driverId").toString());
            Order order = orderService.acceptOrder(orderId, driverId);
            if (order != null) {
                result.put("success", true);
                result.put("message", "接单成功");
                result.put("data", order);
                return ResponseEntity.ok(result);
            } else {
                result.put("success", false);
                result.put("message", "接单失败：订单状态不正确");
                return ResponseEntity.badRequest().body(result);
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "接单失败：" + e.getMessage());
            return ResponseEntity.badRequest().body(result);
        }
    }

    /**
     * 开始服务
     *
     * @param params 包含orderId
     * @return 结果
     */
    @PostMapping("/start")
    public ResponseEntity<Map<String, Object>> start(@RequestBody Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        try {
            Long orderId = Long.valueOf(params.get("orderId").toString());
            Order order = orderService.startService(orderId);
            if (order != null) {
                result.put("success", true);
                result.put("message", "开始服务成功");
                result.put("data", order);
                return ResponseEntity.ok(result);
            } else {
                result.put("success", false);
                result.put("message", "开始服务失败：订单状态不正确");
                return ResponseEntity.badRequest().body(result);
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "开始服务失败：" + e.getMessage());
            return ResponseEntity.badRequest().body(result);
        }
    }

    /**
     * 完成订单
     *
     * @param params 包含orderId和actualAmount
     * @return 结果
     */
    @PostMapping("/complete")
    public ResponseEntity<Map<String, Object>> complete(@RequestBody Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        try {
            Long orderId = Long.valueOf(params.get("orderId").toString());
            BigDecimal actualAmount = params.containsKey("actualAmount") 
                    ? new BigDecimal(params.get("actualAmount").toString()) 
                    : null;
            Order order = orderService.completeOrder(orderId, actualAmount);
            if (order != null) {
                result.put("success", true);
                result.put("message", "完成订单成功");
                result.put("data", order);
                return ResponseEntity.ok(result);
            } else {
                result.put("success", false);
                result.put("message", "完成订单失败：订单状态不正确");
                return ResponseEntity.badRequest().body(result);
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "完成订单失败：" + e.getMessage());
            return ResponseEntity.badRequest().body(result);
        }
    }

    /**
     * 取消订单
     *
     * @param params 包含orderId和cancelReason
     * @return 结果
     */
    @PostMapping("/cancel")
    public ResponseEntity<Map<String, Object>> cancel(@RequestBody Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        try {
            Long orderId = Long.valueOf(params.get("orderId").toString());
            String cancelReason = (String) params.get("cancelReason");
            Order order = orderService.cancelOrder(orderId, cancelReason);
            if (order != null) {
                result.put("success", true);
                result.put("message", "取消订单成功");
                result.put("data", order);
                return ResponseEntity.ok(result);
            } else {
                result.put("success", false);
                result.put("message", "取消订单失败：订单状态不正确");
                return ResponseEntity.badRequest().body(result);
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "取消订单失败：" + e.getMessage());
            return ResponseEntity.badRequest().body(result);
        }
    }

    /**
     * 获取订单状态统计
     *
     * @return 统计数据
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> stats() {
        Map<String, Object> stats = orderService.getOrderStatusStats();
        return ResponseEntity.ok(stats);
    }

    /**
     * 获取进行中的订单列表
     *
     * @return 订单列表
     */
    @GetMapping("/active")
    public ResponseEntity<List<Order>> activeOrders() {
        List<Order> orders = orderService.getActiveOrders();
        return ResponseEntity.ok(orders);
    }

    /**
     * 获取今日订单统计
     *
     * @return 统计数据
     */
    @GetMapping("/today")
    public ResponseEntity<Map<String, Object>> todayStats() {
        Map<String, Object> stats = orderService.getTodayOrderStats();
        return ResponseEntity.ok(stats);
    }
}

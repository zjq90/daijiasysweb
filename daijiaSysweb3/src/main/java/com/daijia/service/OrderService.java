package com.daijia.service;

import com.daijia.entity.Order;
import com.daijia.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 订单服务类
 * 提供订单相关的业务逻辑处理，包括增删改查、状态管理、统计分析等
 *
 * @author daijia
 * @version 1.0.0
 */
@Service
@Transactional
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private OrderRepository orderRepository;

    /**
     * 生成唯一订单编号
     * 格式：DJ + 年月日时分秒 + 4位随机数
     *
     * @return 订单编号
     */
    public String generateOrderNo() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String random = String.format("%04d", new Random().nextInt(10000));
        return "DJ" + timestamp + random;
    }

    /**
     * 创建新订单
     *
     * @param order 订单对象
     * @return 创建后的订单对象
     */
    public Order createOrder(Order order) {
        if (order.getOrderNo() == null || order.getOrderNo().isEmpty()) {
            order.setOrderNo(generateOrderNo());
        }
        if (order.getOrderTime() == null) {
            order.setOrderTime(LocalDateTime.now());
        }
        order.setCreateTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());
        Order savedOrder = orderRepository.save(order);
        logger.info("创建订单成功，订单编号：{}", savedOrder.getOrderNo());
        return savedOrder;
    }

    /**
     * 根据ID查询订单
     *
     * @param id 订单ID
     * @return 订单对象
     */
    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    /**
     * 根据订单编号查询订单
     *
     * @param orderNo 订单编号
     * @return 订单对象
     */
    public Order getOrderByNo(String orderNo) {
        return orderRepository.findByOrderNo(orderNo);
    }

    /**
     * 更新订单信息
     *
     * @param order 订单对象
     * @return 更新后的订单对象
     */
    public Order updateOrder(Order order) {
        order.setUpdateTime(LocalDateTime.now());
        Order updatedOrder = orderRepository.save(order);
        logger.info("更新订单成功，订单编号：{}", updatedOrder.getOrderNo());
        return updatedOrder;
    }

    /**
     * 删除订单
     *
     * @param id 订单ID
     * @return 是否删除成功
     */
    public boolean deleteOrder(Long id) {
        Optional<Order> orderOpt = orderRepository.findById(id);
        if (orderOpt.isPresent()) {
            orderRepository.delete(orderOpt.get());
            logger.info("删除订单成功，订单ID：{}", id);
            return true;
        }
        return false;
    }

    /**
     * 查询所有订单
     *
     * @return 订单列表
     */
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    /**
     * 接单
     *
     * @param orderId  订单ID
     * @param driverId 司机ID
     * @return 更新后的订单对象
     */
    public Order acceptOrder(Long orderId, Long driverId) {
        Order order = getOrderById(orderId);
        if (order != null && "PENDING_WAITING".equals(order.getStatus())) {
            order.setStatus("ACCEPTED");
            order.setDriverId(driverId);
            order.setAcceptTime(LocalDateTime.now());
            return updateOrder(order);
        }
        return null;
    }

    /**
     * 开始服务
     *
     * @param orderId 订单ID
     * @return 更新后的订单对象
     */
    public Order startService(Long orderId) {
        Order order = getOrderById(orderId);
        if (order != null && ("ACCEPTED".equals(order.getStatus()) || "PICKING_UP".equals(order.getStatus()))) {
            order.setStatus("IN_SERVICE");
            order.setStartTime(LocalDateTime.now());
            return updateOrder(order);
        }
        return null;
    }

    /**
     * 完成订单
     *
     * @param orderId      订单ID
     * @param actualAmount 实际金额
     * @return 更新后的订单对象
     */
    public Order completeOrder(Long orderId, BigDecimal actualAmount) {
        Order order = getOrderById(orderId);
        if (order != null && "IN_SERVICE".equals(order.getStatus())) {
            order.setStatus("COMPLETED");
            order.setCompleteTime(LocalDateTime.now());
            if (actualAmount != null) {
                order.setActualAmount(actualAmount);
            }
            return updateOrder(order);
        }
        return null;
    }

    /**
     * 取消订单
     *
     * @param orderId      订单ID
     * @param cancelReason 取消原因
     * @return 更新后的订单对象
     */
    public Order cancelOrder(Long orderId, String cancelReason) {
        Order order = getOrderById(orderId);
        if (order != null && !"COMPLETED".equals(order.getStatus()) && !"CANCELLED".equals(order.getStatus())) {
            order.setStatus("CANCELLED");
            order.setCancelTime(LocalDateTime.now());
            order.setCancelReason(cancelReason);
            return updateOrder(order);
        }
        return null;
    }

    /**
     * 获取实时订单状态统计
     *
     * @return 订单状态统计数据
     */
    public Map<String, Object> getOrderStatusStats() {
        Map<String, Object> stats = new HashMap<>();
        List<Object[]> results = orderRepository.countOrdersByStatus();
        
        Map<String, Long> statusMap = new HashMap<>();
        for (Object[] result : results) {
            String status = (String) result[0];
            Long count = (Long) result[1];
            statusMap.put(status, count);
        }
        
        stats.put("total", orderRepository.count());
        stats.put("pendingWaiting", statusMap.getOrDefault("PENDING_WAITING", 0L));
        stats.put("accepted", statusMap.getOrDefault("ACCEPTED", 0L));
        stats.put("pickingUp", statusMap.getOrDefault("PICKING_UP", 0L));
        stats.put("inService", statusMap.getOrDefault("IN_SERVICE", 0L));
        stats.put("completed", statusMap.getOrDefault("COMPLETED", 0L));
        stats.put("cancelled", statusMap.getOrDefault("CANCELLED", 0L));
        
        return stats;
    }

    /**
     * 获取今日订单统计
     *
     * @return 今日订单统计数据
     */
    public Map<String, Object> getTodayOrderStats() {
        LocalDateTime startTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("todayOrders", orderRepository.countByOrderTimeBetween(startTime, endTime));
        
        // 获取今日各时段订单分布 - 在Service层手动计算
        List<Order> orders = orderRepository.findByOrderTimeBetweenOrderByOrderTimeAsc(startTime, endTime);
        List<Map<String, Object>> hourData = new ArrayList<>();
        
        Map<Integer, Integer> hourCount = new HashMap<>();
        for (Order order : orders) {
            if (order.getOrderTime() != null) {
                int hour = order.getOrderTime().getHour();
                hourCount.put(hour, hourCount.getOrDefault(hour, 0) + 1);
            }
        }
        
        for (Map.Entry<Integer, Integer> entry : hourCount.entrySet()) {
            Map<String, Object> hourMap = new HashMap<>();
            hourMap.put("hour", entry.getKey());
            hourMap.put("count", entry.getValue());
            hourData.add(hourMap);
        }
        stats.put("hourDistribution", hourData);
        
        return stats;
    }

    /**
     * 获取进行中的订单列表
     *
     * @return 进行中的订单列表
     */
    public List<Order> getActiveOrders() {
        return orderRepository.findActiveOrders();
    }

    /**
     * 分页查询订单（简化版）
     *
     * @param page     页码（从0开始）
     * @param pageSize 每页数量
     * @return 订单列表
     */
    public List<Order> getOrdersByPage(int page, int pageSize) {
        List<Order> allOrders = orderRepository.findAll();
        int start = page * pageSize;
        int end = Math.min(start + pageSize, allOrders.size());
        if (start > allOrders.size()) {
            return new ArrayList<>();
        }
        return allOrders.subList(start, end);
    }
}

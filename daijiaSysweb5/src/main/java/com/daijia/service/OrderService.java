package com.daijia.service;

import com.daijia.entity.Order;
import com.daijia.entity.User;
import com.daijia.entity.Driver;
import com.daijia.repository.OrderRepository;
import com.daijia.repository.UserRepository;
import com.daijia.repository.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * 订单服务类
 * 
 * 处理订单相关业务逻辑
 * 
 * 订单状态：
 * 0: 待接单
 * 1: 已接单
 * 2: 已接驾
 * 3: 服务中
 * 4: 已完成
 * 5: 已取消
 * 
 * @author daijia
 * @version 1.0.0
 */
@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DriverRepository driverRepository;

    /**
     * 生成订单号
     * 格式：DJ + 年月日时分秒 + 4位随机数
     * 
     * @return 订单号
     */
    public String generateOrderNo() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String timestamp = LocalDateTime.now().format(formatter);
        Random random = new Random();
        int suffix = random.nextInt(10000);
        return "DJ" + timestamp + String.format("%04d", suffix);
    }

    /**
     * 保存订单
     * 
     * @param order 订单对象
     * @return Order
     */
    @Transactional
    public Order save(Order order) {
        if (order.getOrderNo() == null || order.getOrderNo().isEmpty()) {
            order.setOrderNo(generateOrderNo());
        }
        return orderRepository.save(order);
    }

    /**
     * 根据ID获取订单
     * 
     * @param id 订单ID
     * @return Optional<Order>
     */
    public Optional<Order> findById(Long id) {
        Optional<Order> orderOpt = orderRepository.findById(id);
        orderOpt.ifPresent(this::fillOrderDetails);
        return orderOpt;
    }

    /**
     * 根据订单号获取订单
     * 
     * @param orderNo 订单号
     * @return Optional<Order>
     */
    public Optional<Order> findByOrderNo(String orderNo) {
        Optional<Order> orderOpt = orderRepository.findByOrderNo(orderNo);
        orderOpt.ifPresent(this::fillOrderDetails);
        return orderOpt;
    }

    /**
     * 获取所有订单
     * 
     * @return List<Order>
     */
    public List<Order> findAll() {
        List<Order> orders = orderRepository.findAll();
        orders.forEach(this::fillOrderDetails);
        return orders;
    }

    /**
     * 分页获取订单
     * 
     * @param pageable 分页参数
     * @return Page<Order>
     */
    public Page<Order> findAll(Pageable pageable) {
        Page<Order> orders = orderRepository.findAll(pageable);
        orders.forEach(this::fillOrderDetails);
        return orders;
    }

    /**
     * 根据状态获取订单
     * 
     * @param status 状态
     * @return List<Order>
     */
    public List<Order> findByStatus(Integer status) {
        List<Order> orders = orderRepository.findByStatus(status);
        orders.forEach(this::fillOrderDetails);
        return orders;
    }

    /**
     * 获取进行中的订单
     * 
     * @return List<Order>
     */
    public List<Order> findActiveOrders() {
        List<Order> orders = orderRepository.findActiveOrders();
        orders.forEach(this::fillOrderDetails);
        return orders;
    }

    /**
     * 根据ID删除订单
     * 
     * @param id 订单ID
     */
    @Transactional
    public void deleteById(Long id) {
        orderRepository.deleteById(id);
    }

    /**
     * 根据状态统计订单数量
     * 
     * @param status 状态
     * @return 数量
     */
    public long countByStatus(Integer status) {
        return orderRepository.countByStatus(status);
    }

    /**
     * 接单
     * 
     * @param orderId 订单ID
     * @param driverId 司机ID
     * @return Order
     */
    @Transactional
    public Order acceptOrder(Long orderId, Long driverId) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            if (order.getStatus() == 0) {
                order.setStatus(1);
                order.setDriverId(driverId);
                order.setAcceptTime(LocalDateTime.now());
                return orderRepository.save(order);
            }
        }
        return null;
    }

    /**
     * 接驾
     * 
     * @param orderId 订单ID
     * @return Order
     */
    @Transactional
    public Order pickupOrder(Long orderId) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            if (order.getStatus() == 1) {
                order.setStatus(2);
                order.setPickupTime(LocalDateTime.now());
                return orderRepository.save(order);
            }
        }
        return null;
    }

    /**
     * 开始服务
     * 
     * @param orderId 订单ID
     * @return Order
     */
    @Transactional
    public Order startService(Long orderId) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            if (order.getStatus() == 2) {
                order.setStatus(3);
                order.setStartTime(LocalDateTime.now());
                return orderRepository.save(order);
            }
        }
        return null;
    }

    /**
     * 完成订单
     * 
     * @param orderId 订单ID
     * @param actualDistance 实际距离
     * @param actualTime 实际时间
     * @param actualAmount 实际费用
     * @return Order
     */
    @Transactional
    public Order completeOrder(Long orderId, java.math.BigDecimal actualDistance, 
                                Integer actualTime, java.math.BigDecimal actualAmount) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            if (order.getStatus() == 3) {
                order.setStatus(4);
                order.setActualDistance(actualDistance);
                order.setActualTime(actualTime);
                order.setActualAmount(actualAmount);
                order.setCompleteTime(LocalDateTime.now());
                return orderRepository.save(order);
            }
        }
        return null;
    }

    /**
     * 取消订单
     * 
     * @param orderId 订单ID
     * @param reason 取消原因
     * @return Order
     */
    @Transactional
    public Order cancelOrder(Long orderId, String reason) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            if (order.getStatus() <= 2) {
                order.setStatus(5);
                order.setCancelReason(reason);
                order.setCancelTime(LocalDateTime.now());
                return orderRepository.save(order);
            }
        }
        return null;
    }

    /**
     * 填充订单详情（用户和司机信息）
     * 
     * @param order 订单对象
     */
    private void fillOrderDetails(Order order) {
        if (order.getUserId() != null) {
            Optional<User> userOpt = userRepository.findById(order.getUserId());
            userOpt.ifPresent(order::setUser);
        }
        if (order.getDriverId() != null) {
            Optional<Driver> driverOpt = driverRepository.findById(order.getDriverId());
            driverOpt.ifPresent(order::setDriver);
        }
    }

    /**
     * 获取状态文本
     * 
     * @param status 状态码
     * @return 状态文本
     */
    public String getStatusText(Integer status) {
        switch (status) {
            case 0: return "待接单";
            case 1: return "已接单";
            case 2: return "已接驾";
            case 3: return "服务中";
            case 4: return "已完成";
            case 5: return "已取消";
            default: return "未知";
        }
    }
}

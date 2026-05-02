package com.daijia.service;

import com.daijia.entity.Driver;
import com.daijia.entity.Order;
import com.daijia.entity.User;
import com.daijia.repository.DriverRepository;
import com.daijia.repository.OrderRepository;
import com.daijia.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 订单服务类
 * 处理代驾订单的完整业务流程
 * 包括订单创建、接单、完成、取消等状态管理
 */
@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private PricingConfigService pricingConfigService;

    /**
     * 获取所有订单
     * 
     * @return 订单列表
     */
    public List<Order> findAll() {
        List<Order> orders = orderRepository.findAll();
        for (Order order : orders) {
            fillOrderInfo(order);
        }
        return orders;
    }

    /**
     * 根据ID查询订单
     * 
     * @param id 订单ID
     * @return 订单（可选）
     */
    public Optional<Order> findById(Long id) {
        Optional<Order> orderOpt = orderRepository.findById(id);
        orderOpt.ifPresent(this::fillOrderInfo);
        return orderOpt;
    }

    /**
     * 根据订单号查询订单
     * 
     * @param orderNo 订单号
     * @return 订单（可选）
     */
    public Optional<Order> findByOrderNo(String orderNo) {
        Optional<Order> orderOpt = orderRepository.findByOrderNo(orderNo);
        orderOpt.ifPresent(this::fillOrderInfo);
        return orderOpt;
    }

    /**
     * 根据用户ID查询订单
     * 
     * @param userId 用户ID
     * @return 订单列表
     */
    public List<Order> findByUserId(Long userId) {
        List<Order> orders = orderRepository.findByUserIdOrderByCreatedTimeDesc(userId);
        for (Order order : orders) {
            fillOrderInfo(order);
        }
        return orders;
    }

    /**
     * 根据司机ID查询订单
     * 
     * @param driverId 司机ID
     * @return 订单列表
     */
    public List<Order> findByDriverId(Long driverId) {
        List<Order> orders = orderRepository.findByDriverIdOrderByCreatedTimeDesc(driverId);
        for (Order order : orders) {
            fillOrderInfo(order);
        }
        return orders;
    }

    /**
     * 根据状态查询订单
     * 
     * @param status 订单状态
     * @return 订单列表
     */
    public List<Order> findByStatus(Integer status) {
        List<Order> orders = orderRepository.findByStatusOrderByCreatedTimeDesc(status);
        for (Order order : orders) {
            fillOrderInfo(order);
        }
        return orders;
    }

    /**
     * 获取待接单订单
     * 
     * @return 待接单订单列表
     */
    public List<Order> findPendingOrders() {
        List<Order> orders = orderRepository.findPendingOrders();
        for (Order order : orders) {
            fillOrderInfo(order);
        }
        return orders;
    }

    /**
     * 获取进行中的订单
     * 
     * @return 进行中订单列表
     */
    public List<Order> findInProgressOrders() {
        List<Order> orders = orderRepository.findInProgressOrders();
        for (Order order : orders) {
            fillOrderInfo(order);
        }
        return orders;
    }

    /**
     * 创建订单
     * 
     * @param order 订单信息
     * @return 创建后的订单
     */
    @Transactional
    public Order create(Order order) {
        // 验证用户是否存在
        Optional<User> userOpt = userRepository.findById(order.getUserId());
        if (userOpt.isEmpty()) {
            throw new RuntimeException("用户不存在");
        }

        // 生成订单号
        String orderNo = Order.generateOrderNo();
        while (orderRepository.existsByOrderNo(orderNo)) {
            orderNo = Order.generateOrderNo();
        }
        order.setOrderNo(orderNo);

        // 设置默认值
        if (order.getStatus() == null) {
            order.setStatus(0); // 待接单
        }
        if (order.getUserEvaluated() == null) {
            order.setUserEvaluated(false);
        }
        if (order.getDriverEvaluated() == null) {
            order.setDriverEvaluated(false);
        }
        if (order.getWaitTime() == null) {
            order.setWaitTime(0);
        }

        order.setCreatedTime(LocalDateTime.now());
        order.setUpdatedTime(LocalDateTime.now());

        return orderRepository.save(order);
    }

    /**
     * 司机接单
     * 
     * @param orderId 订单ID
     * @param driverId 司机ID
     * @return 更新后的订单
     */
    @Transactional
    public Order acceptOrder(Long orderId, Long driverId) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isEmpty()) {
            throw new RuntimeException("订单不存在");
        }

        Order order = orderOpt.get();
        
        // 验证订单状态
        if (order.getStatus() != 0) {
            throw new RuntimeException("订单状态不正确，无法接单");
        }

        // 验证司机是否存在且可用
        Optional<Driver> driverOpt = driverRepository.findById(driverId);
        if (driverOpt.isEmpty()) {
            throw new RuntimeException("司机不存在");
        }
        
        Driver driver = driverOpt.get();
        if (driver.getStatus() != 1) {
            throw new RuntimeException("司机状态不可用");
        }
        if (driver.getServicePermission() == 3) {
            throw new RuntimeException("司机服务权限已暂停");
        }

        // 更新订单状态
        order.setDriverId(driverId);
        order.setStatus(1); // 已接单
        order.setUpdatedTime(LocalDateTime.now());

        return orderRepository.save(order);
    }

    /**
     * 开始行程
     * 
     * @param orderId 订单ID
     * @return 更新后的订单
     */
    @Transactional
    public Order startTrip(Long orderId) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isEmpty()) {
            throw new RuntimeException("订单不存在");
        }

        Order order = orderOpt.get();
        
        // 验证订单状态
        if (order.getStatus() != 1) {
            throw new RuntimeException("订单状态不正确，无法开始行程");
        }

        // 更新订单状态
        order.setStatus(2); // 进行中
        order.setUpdatedTime(LocalDateTime.now());

        return orderRepository.save(order);
    }

    /**
     * 完成订单
     * 
     * @param orderId 订单ID
     * @param distance 行驶距离
     * @param duration 行驶时长
     * @param waitTime 等待时间
     * @return 更新后的订单
     */
    @Transactional
    public Order completeOrder(Long orderId, java.math.BigDecimal distance, Integer duration, Integer waitTime) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isEmpty()) {
            throw new RuntimeException("订单不存在");
        }

        Order order = orderOpt.get();
        
        // 验证订单状态
        if (order.getStatus() != 2) {
            throw new RuntimeException("订单状态不正确，无法完成");
        }

        // 更新订单信息
        order.setDistance(distance);
        order.setDuration(duration);
        order.setWaitTime(waitTime != null ? waitTime : 0);
        
        // 计算费用
        if (distance != null) {
            java.math.BigDecimal totalAmount = pricingConfigService.calculatePriceWithDefault(
                distance, 
                order.getWaitTime()
            );
            order.setTotalAmount(totalAmount);
        }

        // 更新订单状态
        order.setStatus(3); // 已完成
        order.setUpdatedTime(LocalDateTime.now());

        return orderRepository.save(order);
    }

    /**
     * 取消订单
     * 
     * @param orderId 订单ID
     * @return 更新后的订单
     */
    @Transactional
    public Order cancelOrder(Long orderId) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isEmpty()) {
            throw new RuntimeException("订单不存在");
        }

        Order order = orderOpt.get();
        
        // 验证订单是否可以取消
        if (!order.canCancel()) {
            throw new RuntimeException("订单状态不允许取消");
        }

        // 更新订单状态
        order.setStatus(4); // 已取消
        order.setUpdatedTime(LocalDateTime.now());

        return orderRepository.save(order);
    }

    /**
     * 订单支付
     * 
     * @param orderId 订单ID
     * @return 更新后的订单
     */
    @Transactional
    public Order payOrder(Long orderId) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isEmpty()) {
            throw new RuntimeException("订单不存在");
        }

        Order order = orderOpt.get();
        
        // 验证订单状态
        if (order.getStatus() != 3) {
            throw new RuntimeException("订单状态不正确，无法支付");
        }

        // 更新订单状态
        order.setStatus(5); // 已支付
        order.setUpdatedTime(LocalDateTime.now());

        return orderRepository.save(order);
    }

    /**
     * 获取用户的待评价订单
     * 
     * @param userId 用户ID
     * @return 待评价订单列表
     */
    public List<Order> findUserPendingEvaluationOrders(Long userId) {
        List<Order> orders = orderRepository.findUserPendingEvaluationOrders(userId);
        for (Order order : orders) {
            fillOrderInfo(order);
        }
        return orders;
    }

    /**
     * 获取司机的待评价订单
     * 
     * @param driverId 司机ID
     * @return 待评价订单列表
     */
    public List<Order> findDriverPendingEvaluationOrders(Long driverId) {
        List<Order> orders = orderRepository.findDriverPendingEvaluationOrders(driverId);
        for (Order order : orders) {
            fillOrderInfo(order);
        }
        return orders;
    }

    /**
     * 统计各状态的订单数量
     * 
     * @param status 订单状态
     * @return 订单数量
     */
    public long countByStatus(Integer status) {
        return orderRepository.countByStatus(status);
    }

    /**
     * 获取最新的N个订单
     * 
     * @param limit 返回数量
     * @return 最新订单列表
     */
    public List<Order> findLatestOrders(int limit) {
        List<Order> orders = orderRepository.findLatestOrders(limit);
        for (Order order : orders) {
            fillOrderInfo(order);
        }
        return orders;
    }

    /**
     * 填充订单的关联信息
     * 用于页面展示时显示用户和司机的详细信息
     * 
     * @param order 订单对象
     */
    private void fillOrderInfo(Order order) {
        if (order.getUserId() != null) {
            userRepository.findById(order.getUserId()).ifPresent(order::setUser);
        }
        if (order.getDriverId() != null) {
            driverRepository.findById(order.getDriverId()).ifPresent(order::setDriver);
        }
    }

    /**
     * 删除订单
     * 
     * @param id 订单ID
     */
    @Transactional
    public void delete(Long id) {
        orderRepository.deleteById(id);
    }
}

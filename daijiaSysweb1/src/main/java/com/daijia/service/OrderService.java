
package com.daijia.service;

import com.daijia.entity.Driver;
import com.daijia.entity.Order;
import com.daijia.repository.OrderRepository;
import com.daijia.util.DistanceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private DispatchService dispatchService;
    
    @Autowired
    private DriverService driverService;
    
    public List<Order> findAll() {
        return orderRepository.findAllOrderByCreateTimeDesc();
    }
    
    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }
    
    public Optional<Order> findByOrderNo(String orderNo) {
        return orderRepository.findByOrderNo(orderNo);
    }
    
    public List<Order> findPendingOrders() {
        return orderRepository.findPendingOrders();
    }
    
    @Transactional
    public Order createOrder(Order order) {
        if (order.getOrderNo() == null) {
            order.setOrderNo("ORD" + System.currentTimeMillis());
        }
        order.setStatus("PENDING");
        
        if (order.getStartLat() != null && order.getStartLng() != null 
            && order.getEndLat() != null && order.getEndLng() != null) {
            double distance = DistanceUtil.getDistance(
                order.getStartLat(), order.getStartLng(),
                order.getEndLat(), order.getEndLng()
            );
            order.setDistance(new BigDecimal(distance / 1000));
            
            double amount = calculateAmount(distance);
            order.setAmount(new BigDecimal(amount));
        }
        
        return orderRepository.save(order);
    }
    
    private double calculateAmount(double distance) {
        double basePrice = 15.0;
        double pricePerKm = 3.5;
        double distanceKm = distance / 1000;
        
        if (distanceKm <= 3) {
            return basePrice;
        } else {
            return basePrice + (distanceKm - 3) * pricePerKm;
        }
    }
    
    @Transactional
    public Order autoDispatch(Long orderId) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (!orderOpt.isPresent()) {
            throw new RuntimeException("订单不存在");
        }
        
        Order order = orderOpt.get();
        
        if (!"PENDING".equals(order.getStatus())) {
            throw new RuntimeException("订单状态不允许派单");
        }
        
        Driver bestDriver = dispatchService.findBestDriver(order);
        
        if (bestDriver == null) {
            throw new RuntimeException("暂无可用司机");
        }
        
        order.setDriver(bestDriver);
        order.setStatus("DISPATCHED");
        order.setDispatchType("AUTO");
        order.setAcceptTime(LocalDateTime.now());
        
        return orderRepository.save(order);
    }
    
    @Transactional
    public Order manualDispatch(Long orderId, Long driverId) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (!orderOpt.isPresent()) {
            throw new RuntimeException("订单不存在");
        }
        
        Order order = orderOpt.get();
        
        if (!"PENDING".equals(order.getStatus())) {
            throw new RuntimeException("订单状态不允许派单");
        }
        
        Optional<Driver> driverOpt = driverService.findById(driverId);
        if (!driverOpt.isPresent()) {
            throw new RuntimeException("司机不存在");
        }
        
        order.setDriver(driverOpt.get());
        order.setStatus("DISPATCHED");
        order.setDispatchType("MANUAL");
        order.setAcceptTime(LocalDateTime.now());
        
        return orderRepository.save(order);
    }
    
    @Transactional
    public Order acceptOrder(Long orderId, Long driverId) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (!orderOpt.isPresent()) {
            throw new RuntimeException("订单不存在");
        }
        
        Order order = orderOpt.get();
        
        if (!"PENDING".equals(order.getStatus())) {
            throw new RuntimeException("订单状态不允许抢单");
        }
        
        Optional<Driver> driverOpt = driverService.findById(driverId);
        if (!driverOpt.isPresent()) {
            throw new RuntimeException("司机不存在");
        }
        
        order.setDriver(driverOpt.get());
        order.setStatus("DISPATCHED");
        order.setDispatchType("ROBBERY");
        order.setAcceptTime(LocalDateTime.now());
        
        return orderRepository.save(order);
    }
    
    @Transactional
    public Order startService(Long orderId) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (!orderOpt.isPresent()) {
            throw new RuntimeException("订单不存在");
        }
        
        Order order = orderOpt.get();
        
        if (!"DISPATCHED".equals(order.getStatus())) {
            throw new RuntimeException("订单状态不允许开始服务");
        }
        
        order.setStatus("IN_SERVICE");
        order.setStartServiceTime(LocalDateTime.now());
        
        return orderRepository.save(order);
    }
    
    @Transactional
    public Order endService(Long orderId) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (!orderOpt.isPresent()) {
            throw new RuntimeException("订单不存在");
        }
        
        Order order = orderOpt.get();
        
        if (!"IN_SERVICE".equals(order.getStatus())) {
            throw new RuntimeException("订单状态不允许结束服务");
        }
        
        order.setStatus("COMPLETED");
        order.setEndServiceTime(LocalDateTime.now());
        
        return orderRepository.save(order);
    }
    
    @Transactional
    public Order cancelOrder(Long orderId) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (!orderOpt.isPresent()) {
            throw new RuntimeException("订单不存在");
        }
        
        Order order = orderOpt.get();
        
        if (!"PENDING".equals(order.getStatus()) && !"DISPATCHED".equals(order.getStatus())) {
            throw new RuntimeException("订单状态不允许取消");
        }
        
        order.setStatus("CANCELLED");
        order.setCancelTime(LocalDateTime.now());
        
        return orderRepository.save(order);
    }
    
    @Transactional
    public Order payOrder(Long orderId) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (!orderOpt.isPresent()) {
            throw new RuntimeException("订单不存在");
        }
        
        Order order = orderOpt.get();
        
        if (!"COMPLETED".equals(order.getStatus())) {
            throw new RuntimeException("订单状态不允许支付");
        }
        
        order.setStatus("PAID");
        order.setPayTime(LocalDateTime.now());
        
        return orderRepository.save(order);
    }
    
    @Transactional
    public Order save(Order order) {
        return orderRepository.save(order);
    }
    
    @Transactional
    public void deleteById(Long id) {
        orderRepository.deleteById(id);
    }
}

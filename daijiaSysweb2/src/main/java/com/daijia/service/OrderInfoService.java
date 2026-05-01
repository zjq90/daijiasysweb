package com.daijia.service;

import com.daijia.entity.OrderInfo;
import com.daijia.repository.OrderInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 订单信息服务层
 * 处理订单信息相关的业务逻辑
 */
@Service
@Transactional
public class OrderInfoService {

    @Autowired
    private OrderInfoRepository orderInfoRepository;

    /**
     * 查询所有订单
     * @return 订单列表
     */
    public List<OrderInfo> findAll() {
        return orderInfoRepository.findAll();
    }

    /**
     * 根据ID查询订单
     * @param id 订单ID
     * @return 订单信息
     */
    public Optional<OrderInfo> findById(Long id) {
        return orderInfoRepository.findById(id);
    }

    /**
     * 根据订单号查询
     * @param orderNo 订单号
     * @return 订单信息
     */
    public OrderInfo findByOrderNo(String orderNo) {
        return orderInfoRepository.findByOrderNo(orderNo);
    }

    /**
     * 根据司机ID查询订单
     * @param driverId 司机ID
     * @return 订单列表
     */
    public List<OrderInfo> findByDriverId(Long driverId) {
        return orderInfoRepository.findByDriverIdOrderByCreateTimeDesc(driverId);
    }

    /**
     * 根据状态查询订单
     * @param status 状态
     * @return 订单列表
     */
    public List<OrderInfo> findByStatus(String status) {
        return orderInfoRepository.findByStatusOrderByCreateTimeDesc(status);
    }

    /**
     * 根据司机ID和状态查询订单
     * @param driverId 司机ID
     * @param status 状态
     * @return 订单列表
     */
    public List<OrderInfo> findByDriverIdAndStatus(Long driverId, String status) {
        return orderInfoRepository.findByDriverIdAndStatusOrderByCreateTimeDesc(driverId, status);
    }

    /**
     * 保存订单
     * @param order 订单信息
     * @return 保存后的订单
     */
    public OrderInfo save(OrderInfo order) {
        // 生成订单号
        if (order.getOrderNo() == null || order.getOrderNo().isEmpty()) {
            String orderNo = "OD" + System.currentTimeMillis();
            order.setOrderNo(orderNo);
        }
        return orderInfoRepository.save(order);
    }

    /**
     * 更新订单
     * @param order 订单信息
     * @return 更新后的订单
     */
    public OrderInfo update(OrderInfo order) {
        if (!orderInfoRepository.existsById(order.getId())) {
            throw new RuntimeException("订单不存在");
        }
        return orderInfoRepository.save(order);
    }

    /**
     * 删除订单
     * @param id 订单ID
     */
    public void deleteById(Long id) {
        orderInfoRepository.deleteById(id);
    }
}

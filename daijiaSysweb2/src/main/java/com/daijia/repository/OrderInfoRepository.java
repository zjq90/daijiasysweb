package com.daijia.repository;

import com.daijia.entity.OrderInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 订单信息数据访问层
 * 继承JpaRepository，提供基本的CRUD操作
 */
@Repository
public interface OrderInfoRepository extends JpaRepository<OrderInfo, Long> {

    /**
     * 根据订单号查询
     * @param orderNo 订单号
     * @return 订单信息
     */
    OrderInfo findByOrderNo(String orderNo);

    /**
     * 根据司机ID查询订单列表，按创建时间倒序排列
     * @param driverId 司机ID
     * @return 订单列表
     */
    List<OrderInfo> findByDriverIdOrderByCreateTimeDesc(Long driverId);

    /**
     * 根据状态查询订单列表
     * @param status 状态
     * @return 订单列表
     */
    List<OrderInfo> findByStatusOrderByCreateTimeDesc(String status);

    /**
     * 查询司机已完成的订单
     * @param driverId 司机ID
     * @param status 状态（COMPLETED）
     * @return 订单列表
     */
    List<OrderInfo> findByDriverIdAndStatusOrderByCreateTimeDesc(Long driverId, String status);
}

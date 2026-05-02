package com.daijia.repository;

import com.daijia.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单数据访问层
 * 提供订单数据的增删改查及统计功能
 *
 * @author daijia
 * @version 1.0.0
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * 根据订单编号查询订单
     *
     * @param orderNo 订单编号
     * @return 订单对象
     */
    Order findByOrderNo(String orderNo);

    /**
     * 根据订单状态查询订单列表
     *
     * @param status 订单状态
     * @return 订单列表
     */
    List<Order> findByStatusOrderByOrderTimeDesc(String status);

    /**
     * 根据司机ID和状态查询订单
     *
     * @param driverId 司机ID
     * @param status   订单状态
     * @return 订单列表
     */
    List<Order> findByDriverIdAndStatus(Long driverId, String status);

    /**
     * 根据乘客ID查询订单列表
     *
     * @param passengerId 乘客ID
     * @return 订单列表
     */
    List<Order> findByPassengerIdOrderByOrderTimeDesc(Long passengerId);

    /**
     * 统计指定状态的订单数量
     *
     * @param status 订单状态
     * @return 订单数量
     */
    long countByStatus(String status);

    /**
     * 统计指定时间范围内的订单数量
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 订单数量
     */
    long countByOrderTimeBetween(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 统计指定服务区域的订单数量
     *
     * @param serviceAreaId 服务区域ID
     * @return 订单数量
     */
    long countByServiceAreaId(Long serviceAreaId);

    /**
     * 查询指定时间范围内的订单列表
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 订单列表
     */
    List<Order> findByOrderTimeBetweenOrderByOrderTimeDesc(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 查询进行中的订单（非取消、非完成）
     *
     * @return 订单列表
     */
    @Query("SELECT o FROM Order o WHERE o.status IN ('PENDING_WAITING', 'ACCEPTED', 'PICKING_UP', 'IN_SERVICE') ORDER BY o.orderTime DESC")
    List<Order> findActiveOrders();

    /**
     * 统计各状态的订单数量
     *
     * @return 状态统计结果列表
     */
    @Query("SELECT o.status, COUNT(o) FROM Order o GROUP BY o.status")
    List<Object[]> countOrdersByStatus();

    /**
     * 统计指定服务区域各状态的订单数量
     *
     * @param serviceAreaId 服务区域ID
     * @return 状态统计结果列表
     */
    @Query("SELECT o.status, COUNT(o) FROM Order o WHERE o.serviceAreaId = :serviceAreaId GROUP BY o.status")
    List<Object[]> countOrdersByStatusAndServiceArea(@Param("serviceAreaId") Long serviceAreaId);

    /**
     * 查询指定时间范围的订单列表
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 订单列表
     */
    List<Order> findByOrderTimeBetweenOrderByOrderTimeAsc(LocalDateTime startTime, LocalDateTime endTime);
}

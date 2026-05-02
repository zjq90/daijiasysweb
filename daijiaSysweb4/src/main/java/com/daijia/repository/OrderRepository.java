package com.daijia.repository;

import com.daijia.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 订单数据访问接口
 * 继承JpaRepository，提供基础的增删改查功能
 * 同时定义与订单状态、用户/司机查询相关的自定义查询方法
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * 根据订单号查询订单
     * 
     * @param orderNo 订单号
     * @return 订单（可选）
     */
    Optional<Order> findByOrderNo(@Param("orderNo") String orderNo);

    /**
     * 根据用户ID查询所有订单
     * 
     * @param userId 用户ID
     * @return 订单列表
     */
    List<Order> findByUserIdOrderByCreatedTimeDesc(@Param("userId") Long userId);

    /**
     * 根据司机ID查询所有订单
     * 
     * @param driverId 司机ID
     * @return 订单列表
     */
    List<Order> findByDriverIdOrderByCreatedTimeDesc(@Param("driverId") Long driverId);

    /**
     * 根据订单状态查询订单
     * 
     * @param status 订单状态
     * @return 订单列表
     */
    List<Order> findByStatusOrderByCreatedTimeDesc(@Param("status") Integer status);

    /**
     * 查询待接单的订单
     * 用于司机接单列表
     * 
     * @return 待接单订单列表
     */
    @Query("SELECT o FROM Order o WHERE o.status = 0 ORDER BY o.createdTime DESC")
    List<Order> findPendingOrders();

    /**
     * 查询进行中的订单
     * 
     * @return 进行中订单列表
     */
    @Query("SELECT o FROM Order o WHERE o.status = 2 ORDER BY o.createdTime DESC")
    List<Order> findInProgressOrders();

    /**
     * 查询已完成但未支付的订单
     * 
     * @return 待支付订单列表
     */
    @Query("SELECT o FROM Order o WHERE o.status = 3 ORDER BY o.createdTime DESC")
    List<Order> findCompletedUnpaidOrders();

    /**
     * 查询用户的待评价订单
     * 
     * @param userId 用户ID
     * @return 待评价订单列表
     */
    @Query("SELECT o FROM Order o WHERE o.userId = :userId AND o.status IN (3, 5) AND o.userEvaluated = false ORDER BY o.createdTime DESC")
    List<Order> findUserPendingEvaluationOrders(@Param("userId") Long userId);

    /**
     * 查询司机的待评价订单
     * 
     * @param driverId 司机ID
     * @return 待评价订单列表
     */
    @Query("SELECT o FROM Order o WHERE o.driverId = :driverId AND o.status IN (3, 5) AND o.driverEvaluated = false ORDER BY o.createdTime DESC")
    List<Order> findDriverPendingEvaluationOrders(@Param("driverId") Long driverId);

    /**
     * 根据用户ID和订单状态查询订单
     * 
     * @param userId 用户ID
     * @param status 订单状态
     * @return 订单列表
     */
    List<Order> findByUserIdAndStatusOrderByCreatedTimeDesc(
            @Param("userId") Long userId,
            @Param("status") Integer status);

    /**
     * 根据司机ID和订单状态查询订单
     * 
     * @param driverId 司机ID
     * @param status 订单状态
     * @return 订单列表
     */
    List<Order> findByDriverIdAndStatusOrderByCreatedTimeDesc(
            @Param("driverId") Long driverId,
            @Param("status") Integer status);

    /**
     * 根据订单号或地址模糊查询订单
     * 
     * @param keyword 关键词
     * @return 匹配的订单列表
     */
    @Query("SELECT o FROM Order o WHERE o.orderNo LIKE %:keyword% OR o.startAddress LIKE %:keyword% OR o.endAddress LIKE %:keyword%")
    List<Order> searchByKeyword(@Param("keyword") String keyword);

    /**
     * 统计指定状态的订单数量
     * 
     * @param status 订单状态
     * @return 订单数量
     */
    long countByStatus(@Param("status") Integer status);

    /**
     * 统计用户的订单数量
     * 
     * @param userId 用户ID
     * @return 订单数量
     */
    long countByUserId(@Param("userId") Long userId);

    /**
     * 统计司机的订单数量
     * 
     * @param driverId 司机ID
     * @return 订单数量
     */
    long countByDriverId(@Param("driverId") Long driverId);

    /**
     * 查询最新的N个订单
     * 
     * @param limit 返回数量
     * @return 最新订单列表
     */
    @Query("SELECT o FROM Order o ORDER BY o.createdTime DESC")
    List<Order> findLatestOrders(@Param("limit") int limit);

    /**
     * 检查订单号是否存在
     * 
     * @param orderNo 订单号
     * @return 是否存在
     */
    boolean existsByOrderNo(@Param("orderNo") String orderNo);

    /**
     * 查询用户的历史订单
     * 
     * @param userId 用户ID
     * @return 历史订单列表
     */
    @Query("SELECT o FROM Order o WHERE o.userId = :userId AND o.status >= 3 ORDER BY o.createdTime DESC")
    List<Order> findUserHistoryOrders(@Param("userId") Long userId);

    /**
     * 查询司机的历史订单
     * 
     * @param driverId 司机ID
     * @return 历史订单列表
     */
    @Query("SELECT o FROM Order o WHERE o.driverId = :driverId AND o.status >= 3 ORDER BY o.createdTime DESC")
    List<Order> findDriverHistoryOrders(@Param("driverId") Long driverId);
}

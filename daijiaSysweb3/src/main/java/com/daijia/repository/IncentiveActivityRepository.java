package com.daijia.repository;

import com.daijia.entity.IncentiveActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 激励奖励活动数据访问层
 * 提供激励奖励活动数据的增删改查及统计功能
 *
 * @author daijia
 * @version 1.0.0
 */
@Repository
public interface IncentiveActivityRepository extends JpaRepository<IncentiveActivity, Long> {

    /**
     * 根据活动编号查询活动
     *
     * @param activityNo 活动编号
     * @return 活动对象
     */
    IncentiveActivity findByActivityNo(String activityNo);

    /**
     * 根据状态查询活动列表
     *
     * @param status 活动状态
     * @return 活动列表
     */
    List<IncentiveActivity> findByStatusOrderBySortOrderAsc(String status);

    /**
     * 根据活动类型查询活动列表
     *
     * @param activityType 活动类型
     * @return 活动列表
     */
    List<IncentiveActivity> findByActivityTypeOrderByCreateTimeDesc(String activityType);

    /**
     * 查询进行中的活动
     *
     * @param currentTime 当前时间
     * @return 活动列表
     */
    @Query("SELECT a FROM IncentiveActivity a WHERE a.status = 'ACTIVE' AND a.startTime <= :currentTime AND a.endTime >= :currentTime ORDER BY a.sortOrder ASC")
    List<IncentiveActivity> findActiveActivities(@Param("currentTime") LocalDateTime currentTime);

    /**
     * 查询指定司机适用的进行中活动
     *
     * @param driverId    司机ID
     * @param currentTime 当前时间
     * @return 活动列表
     */
    @Query("SELECT a FROM IncentiveActivity a WHERE a.status = 'ACTIVE' AND a.startTime <= :currentTime AND a.endTime >= :currentTime AND (a.driverIds IS NULL OR a.driverIds LIKE CONCAT('%', :driverId, '%')) ORDER BY a.sortOrder ASC")
    List<IncentiveActivity> findApplicableActivitiesForDriver(
            @Param("driverId") Long driverId,
            @Param("currentTime") LocalDateTime currentTime);

    /**
     * 统计各状态的活动数量
     *
     * @return 状态统计结果列表
     */
    @Query("SELECT a.status, COUNT(a) FROM IncentiveActivity a GROUP BY a.status")
    List<Object[]> countActivitiesByStatus();

    /**
     * 统计各类型的活动数量
     *
     * @return 类型统计结果列表
     */
    @Query("SELECT a.activityType, COUNT(a) FROM IncentiveActivity a GROUP BY a.activityType")
    List<Object[]> countActivitiesByType();

    /**
     * 统计指定时间范围内的活动总预算
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 总预算
     */
    @Query("SELECT SUM(a.budgetAmount) FROM IncentiveActivity a WHERE a.startTime >= :startTime AND a.endTime <= :endTime")
    java.math.BigDecimal sumBudgetByTimeRange(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);

    /**
     * 统计指定时间范围内的活动已使用金额
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 已使用金额
     */
    @Query("SELECT SUM(a.usedAmount) FROM IncentiveActivity a WHERE a.startTime >= :startTime AND a.endTime <= :endTime")
    java.math.BigDecimal sumUsedAmountByTimeRange(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);

    /**
     * 统计活动总受益司机数
     *
     * @return 总受益司机数
     */
    @Query("SELECT SUM(a.benefitedDrivers) FROM IncentiveActivity a WHERE a.status = 'ACTIVE'")
    Integer sumBenefitedDrivers();

    /**
     * 统计活动总受益订单数
     *
     * @return 总受益订单数
     */
    @Query("SELECT SUM(a.benefitedOrders) FROM IncentiveActivity a WHERE a.status = 'ACTIVE'")
    Integer sumBenefitedOrders();
}

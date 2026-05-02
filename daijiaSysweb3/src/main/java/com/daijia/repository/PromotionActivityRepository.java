package com.daijia.repository;

import com.daijia.entity.PromotionActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 优惠活动数据访问层
 * 提供优惠活动数据的增删改查及统计功能
 *
 * @author daijia
 * @version 1.0.0
 */
@Repository
public interface PromotionActivityRepository extends JpaRepository<PromotionActivity, Long> {

    /**
     * 根据活动编号查询活动
     *
     * @param activityNo 活动编号
     * @return 活动对象
     */
    PromotionActivity findByActivityNo(String activityNo);

    /**
     * 根据状态查询活动列表
     *
     * @param status 活动状态
     * @return 活动列表
     */
    List<PromotionActivity> findByStatusOrderBySortOrderAsc(String status);

    /**
     * 根据活动类型查询活动列表
     *
     * @param activityType 活动类型
     * @return 活动列表
     */
    List<PromotionActivity> findByActivityTypeOrderByCreateTimeDesc(String activityType);

    /**
     * 查询进行中的活动
     *
     * @param currentTime 当前时间
     * @return 活动列表
     */
    @Query("SELECT a FROM PromotionActivity a WHERE a.status = 'ACTIVE' AND a.startTime <= :currentTime AND a.endTime >= :currentTime ORDER BY a.sortOrder ASC")
    List<PromotionActivity> findActiveActivities(@Param("currentTime") LocalDateTime currentTime);

    /**
     * 查询新人专享活动
     *
     * @param currentTime 当前时间
     * @return 活动列表
     */
    @Query("SELECT a FROM PromotionActivity a WHERE a.status = 'ACTIVE' AND a.applicableUserType = 'NEW_USER' AND a.startTime <= :currentTime AND a.endTime >= :currentTime ORDER BY a.sortOrder ASC")
    List<PromotionActivity> findNewUserActivities(@Param("currentTime") LocalDateTime currentTime);

    /**
     * 查询指定用户适用的进行中活动
     *
     * @param userId      用户ID
     * @param userType    用户类型
     * @param currentTime 当前时间
     * @return 活动列表
     */
    @Query("SELECT a FROM PromotionActivity a WHERE a.status = 'ACTIVE' AND a.startTime <= :currentTime AND a.endTime >= :currentTime " +
           "AND (a.applicableUserType = 'ALL' OR a.applicableUserType = :userType OR (a.applicableUserType = 'SPECIFIC' AND a.userIds LIKE CONCAT('%', :userId, '%'))) " +
           "ORDER BY a.sortOrder ASC")
    List<PromotionActivity> findApplicableActivitiesForUser(
            @Param("userId") Long userId,
            @Param("userType") String userType,
            @Param("currentTime") LocalDateTime currentTime);

    /**
     * 统计各状态的活动数量
     *
     * @return 状态统计结果列表
     */
    @Query("SELECT a.status, COUNT(a) FROM PromotionActivity a GROUP BY a.status")
    List<Object[]> countActivitiesByStatus();

    /**
     * 统计各类型的活动数量
     *
     * @return 类型统计结果列表
     */
    @Query("SELECT a.activityType, COUNT(a) FROM PromotionActivity a GROUP BY a.activityType")
    List<Object[]> countActivitiesByType();

    /**
     * 统计指定时间范围内的活动总预算
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 总预算
     */
    @Query("SELECT SUM(a.budgetAmount) FROM PromotionActivity a WHERE a.startTime >= :startTime AND a.endTime <= :endTime")
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
    @Query("SELECT SUM(a.usedAmount) FROM PromotionActivity a WHERE a.startTime >= :startTime AND a.endTime <= :endTime")
    java.math.BigDecimal sumUsedAmountByTimeRange(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);

    /**
     * 统计活动总发放数量
     *
     * @return 总发放数量
     */
    @Query("SELECT SUM(a.issuedQuantity) FROM PromotionActivity a WHERE a.status = 'ACTIVE'")
    Integer sumIssuedQuantity();

    /**
     * 统计活动总使用数量
     *
     * @return 总使用数量
     */
    @Query("SELECT SUM(a.usedQuantity) FROM PromotionActivity a WHERE a.status = 'ACTIVE'")
    Integer sumUsedQuantity();

    /**
     * 查询即将过期的活动（7天内过期）
     *
     * @param currentTime 当前时间
     * @param expireTime  过期时间
     * @return 活动列表
     */
    @Query("SELECT a FROM PromotionActivity a WHERE a.status = 'ACTIVE' AND a.endTime BETWEEN :currentTime AND :expireTime ORDER BY a.endTime ASC")
    List<PromotionActivity> findExpiringActivities(
            @Param("currentTime") LocalDateTime currentTime,
            @Param("expireTime") LocalDateTime expireTime);
}

package com.daijia.repository;

import com.daijia.entity.DriverEvaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 司机评价记录数据访问接口
 * 继承JpaRepository，提供基础的增删改查功能
 * 同时定义与评价统计、特殊行为查询相关的自定义查询方法
 */
@Repository
public interface DriverEvaluationRepository extends JpaRepository<DriverEvaluation, Long> {

    /**
     * 根据用户ID查询所有评价记录
     * 用于查看某个用户的所有司机评价
     * 
     * @param userId 用户ID
     * @return 评价记录列表
     */
    List<DriverEvaluation> findByUserIdOrderByCreatedTimeDesc(@Param("userId") Long userId);

    /**
     * 根据司机ID查询所有评价记录
     * 用于查看某个司机的所有评价历史
     * 
     * @param driverId 司机ID
     * @return 评价记录列表
     */
    List<DriverEvaluation> findByDriverIdOrderByCreatedTimeDesc(@Param("driverId") Long driverId);

    /**
     * 根据订单ID查询评价记录
     * 每个订单只能有一个司机评价
     * 
     * @param orderId 订单ID
     * @return 评价记录（可选）
     */
    java.util.Optional<DriverEvaluation> findByOrderId(@Param("orderId") Long orderId);

    /**
     * 查询所有正常状态的评价记录
     * 
     * @param status 评价状态
     * @return 评价记录列表
     */
    List<DriverEvaluation> findByStatusOrderByCreatedTimeDesc(@Param("status") Integer status);

    /**
     * 查询有异常行为的评价记录
     * 用于监控有问题的用户
     * 
     * @return 异常行为评价列表
     */
    @Query("SELECT e FROM DriverEvaluation e WHERE e.status = 1 AND (e.hasAbnormalBehavior = true OR (e.isDrunk = true AND e.overallRating <= 2)) ORDER BY e.createdTime DESC")
    List<DriverEvaluation> findEvaluationsWithSeriousIssues();

    /**
     * 查询有醉酒记录的评价
     * 
     * @return 醉酒记录列表
     */
    List<DriverEvaluation> findByIsDrunkTrueOrderByCreatedTimeDesc();

    /**
     * 查询有更改目的地记录的评价
     * 
     * @return 更改目的地记录列表
     */
    List<DriverEvaluation> findByIsDestinationChangedTrueOrderByCreatedTimeDesc();

    /**
     * 查询有异常行为记录的评价
     * 
     * @return 异常行为记录列表
     */
    List<DriverEvaluation> findByHasAbnormalBehaviorTrueOrderByCreatedTimeDesc();

    /**
     * 统计用户的评价数量
     * 
     * @param userId 用户ID
     * @return 评价数量
     */
    long countByUserId(@Param("userId") Long userId);

    /**
     * 统计司机的评价数量
     * 
     * @param driverId 司机ID
     * @return 评价数量
     */
    long countByDriverId(@Param("driverId") Long driverId);

    /**
     * 计算用户的平均评分
     * 
     * @param userId 用户ID
     * @return 平均评分（可选）
     */
    @Query("SELECT AVG(e.overallRating) FROM DriverEvaluation e WHERE e.userId = :userId AND e.status = 1")
    java.util.Optional<Double> calculateAvgRatingByUserId(@Param("userId") Long userId);

    /**
     * 根据评分范围查询评价
     * 
     * @param minRating 最低评分
     * @param maxRating 最高评分
     * @return 评价记录列表
     */
    List<DriverEvaluation> findByOverallRatingBetweenOrderByCreatedTimeDesc(
            @Param("minRating") Integer minRating,
            @Param("maxRating") Integer maxRating);

    /**
     * 查询最新的N条评价
     * 
     * @param limit 返回数量
     * @return 最新评价列表
     */
    @Query("SELECT e FROM DriverEvaluation e WHERE e.status = 1 ORDER BY e.createdTime DESC")
    List<DriverEvaluation> findLatestEvaluations(@Param("limit") int limit);

    /**
     * 检查订单是否已被司机评价
     * 
     * @param orderId 订单ID
     * @return 是否已评价
     */
    boolean existsByOrderId(@Param("orderId") Long orderId);

    /**
     * 统计用户的异常行为次数
     * 
     * @param userId 用户ID
     * @return 异常行为次数
     */
    @Query("SELECT COUNT(e) FROM DriverEvaluation e WHERE e.userId = :userId AND e.status = 1 AND (e.hasAbnormalBehavior = true OR (e.isDrunk = true AND e.overallRating <= 2))")
    long countSeriousIssuesByUserId(@Param("userId") Long userId);
}

package com.daijia.repository;

import com.daijia.entity.UserEvaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户评价记录数据访问接口
 * 继承JpaRepository，提供基础的增删改查功能
 * 同时定义与评价统计、查询相关的自定义查询方法
 */
@Repository
public interface UserEvaluationRepository extends JpaRepository<UserEvaluation, Long> {

    /**
     * 根据司机ID查询所有评价记录
     * 用于查看某个司机的所有用户评价
     * 
     * @param driverId 司机ID
     * @return 评价记录列表
     */
    List<UserEvaluation> findByDriverIdOrderByCreatedTimeDesc(@Param("driverId") Long driverId);

    /**
     * 根据用户ID查询所有评价记录
     * 用于查看某个用户的所有评价历史
     * 
     * @param userId 用户ID
     * @return 评价记录列表
     */
    List<UserEvaluation> findByUserIdOrderByCreatedTimeDesc(@Param("userId") Long userId);

    /**
     * 根据订单ID查询评价记录
     * 每个订单只能有一个用户评价
     * 
     * @param orderId 订单ID
     * @return 评价记录（可选）
     */
    java.util.Optional<UserEvaluation> findByOrderId(@Param("orderId") Long orderId);

    /**
     * 查询所有正常状态的评价记录
     * 
     * @param status 评价状态
     * @return 评价记录列表
     */
    List<UserEvaluation> findByStatusOrderByCreatedTimeDesc(@Param("status") Integer status);

    /**
     * 查询高评分评价（4-5星）
     * 
     * @return 高评分评价列表
     */
    @Query("SELECT e FROM UserEvaluation e WHERE e.status = 1 AND e.overallRating >= 4 ORDER BY e.createdTime DESC")
    List<UserEvaluation> findHighRatingEvaluations();

    /**
     * 查询低评分评价（1-3星）
     * 用于监控需要关注的评价
     * 
     * @return 低评分评价列表
     */
    @Query("SELECT e FROM UserEvaluation e WHERE e.status = 1 AND e.overallRating <= 3 ORDER BY e.createdTime DESC")
    List<UserEvaluation> findLowRatingEvaluations();

    /**
     * 统计司机的评价数量
     * 
     * @param driverId 司机ID
     * @return 评价数量
     */
    long countByDriverId(@Param("driverId") Long driverId);

    /**
     * 统计用户的评价数量
     * 
     * @param userId 用户ID
     * @return 评价数量
     */
    long countByUserId(@Param("userId") Long userId);

    /**
     * 计算司机的平均评分
     * 
     * @param driverId 司机ID
     * @return 平均评分（可选）
     */
    @Query("SELECT AVG(e.overallRating) FROM UserEvaluation e WHERE e.driverId = :driverId AND e.status = 1")
    java.util.Optional<Double> calculateAvgRatingByDriverId(@Param("driverId") Long driverId);

    /**
     * 根据评分范围查询评价
     * 
     * @param minRating 最低评分
     * @param maxRating 最高评分
     * @return 评价记录列表
     */
    List<UserEvaluation> findByOverallRatingBetweenOrderByCreatedTimeDesc(
            @Param("minRating") Integer minRating,
            @Param("maxRating") Integer maxRating);

    /**
     * 查询最新的N条评价
     * 
     * @param limit 返回数量
     * @return 最新评价列表
     */
    @Query("SELECT e FROM UserEvaluation e WHERE e.status = 1 ORDER BY e.createdTime DESC")
    List<UserEvaluation> findLatestEvaluations(@Param("limit") int limit);

    /**
     * 检查订单是否已被用户评价
     * 
     * @param orderId 订单ID
     * @return 是否已评价
     */
    boolean existsByOrderId(@Param("orderId") Long orderId);
}

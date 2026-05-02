package com.daijia.service;

import com.daijia.entity.Driver;
import com.daijia.entity.Order;
import com.daijia.entity.UserEvaluation;
import com.daijia.repository.DriverRepository;
import com.daijia.repository.OrderRepository;
import com.daijia.repository.UserEvaluationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 用户评价记录服务类
 * 处理用户（乘客）对司机的评价业务逻辑
 * 包括评价的创建、查询、更新、删除，以及评价对司机评分的影响
 */
@Service
public class UserEvaluationService {

    @Autowired
    private UserEvaluationRepository userEvaluationRepository;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private OrderRepository orderRepository;

    /**
     * 获取所有有效评价记录
     * 
     * @return 评价记录列表
     */
    public List<UserEvaluation> findAllValid() {
        List<UserEvaluation> evaluations = userEvaluationRepository.findByStatusOrderByCreatedTimeDesc(1);
        // 填充关联信息
        for (UserEvaluation eval : evaluations) {
            fillEvaluationInfo(eval);
        }
        return evaluations;
    }

    /**
     * 根据ID查询评价
     * 
     * @param id 评价ID
     * @return 评价（可选）
     */
    public Optional<UserEvaluation> findById(Long id) {
        Optional<UserEvaluation> evalOpt = userEvaluationRepository.findById(id);
        evalOpt.ifPresent(this::fillEvaluationInfo);
        return evalOpt;
    }

    /**
     * 根据司机ID查询所有评价
     * 
     * @param driverId 司机ID
     * @return 评价列表
     */
    public List<UserEvaluation> findByDriverId(Long driverId) {
        List<UserEvaluation> evaluations = userEvaluationRepository.findByDriverIdOrderByCreatedTimeDesc(driverId);
        for (UserEvaluation eval : evaluations) {
            fillEvaluationInfo(eval);
        }
        return evaluations;
    }

    /**
     * 根据用户ID查询所有评价
     * 
     * @param userId 用户ID
     * @return 评价列表
     */
    public List<UserEvaluation> findByUserId(Long userId) {
        List<UserEvaluation> evaluations = userEvaluationRepository.findByUserIdOrderByCreatedTimeDesc(userId);
        for (UserEvaluation eval : evaluations) {
            fillEvaluationInfo(eval);
        }
        return evaluations;
    }

    /**
     * 创建用户评价
     * 创建评价后会自动更新司机的平均评分、派单优先级和服务权限
     * 
     * @param evaluation 评价信息
     * @return 创建后的评价
     */
    @Transactional
    public UserEvaluation create(UserEvaluation evaluation) {
        // 验证订单是否存在且未被评价
        Optional<Order> orderOpt = orderRepository.findById(evaluation.getOrderId());
        if (orderOpt.isEmpty()) {
            throw new RuntimeException("订单不存在");
        }
        
        Order order = orderOpt.get();
        if (order.getUserEvaluated() != null && order.getUserEvaluated()) {
            throw new RuntimeException("该订单已被评价");
        }

        // 验证评分范围
        validateRating(evaluation.getOverallRating());

        // 设置默认值
        if (evaluation.getStatus() == null) {
            evaluation.setStatus(1);
        }
        evaluation.setCreatedTime(LocalDateTime.now());
        evaluation.setUpdatedTime(LocalDateTime.now());

        // 保存评价
        UserEvaluation savedEval = userEvaluationRepository.save(evaluation);

        // 更新司机评分
        updateDriverRating(evaluation.getDriverId(), evaluation.getOverallRating());

        // 更新订单评价状态
        order.setUserEvaluated(true);
        order.setUpdatedTime(LocalDateTime.now());
        orderRepository.save(order);

        return savedEval;
    }

    /**
     * 更新评价信息
     * 注意：更新评价不会重新计算司机评分，因为评分应该是不可更改的
     * 
     * @param evaluation 评价信息
     * @return 更新后的评价
     */
    @Transactional
    public UserEvaluation update(UserEvaluation evaluation) {
        Optional<UserEvaluation> existingOpt = userEvaluationRepository.findById(evaluation.getId());
        if (existingOpt.isEmpty()) {
            throw new RuntimeException("评价记录不存在");
        }

        UserEvaluation existing = existingOpt.get();
        
        // 允许更新的字段
        if (evaluation.getContent() != null) {
            existing.setContent(evaluation.getContent());
        }
        if (evaluation.getDrivingSkill() != null) {
            existing.setDrivingSkill(evaluation.getDrivingSkill());
        }
        if (evaluation.getServiceAttitude() != null) {
            existing.setServiceAttitude(evaluation.getServiceAttitude());
        }
        if (evaluation.getCarCleanliness() != null) {
            existing.setCarCleanliness(evaluation.getCarCleanliness());
        }
        
        existing.setUpdatedTime(LocalDateTime.now());
        
        return userEvaluationRepository.save(existing);
    }

    /**
     * 删除评价（逻辑删除）
     * 将状态设置为0，而非物理删除
     * 
     * @param id 评价ID
     */
    @Transactional
    public void delete(Long id) {
        Optional<UserEvaluation> evalOpt = userEvaluationRepository.findById(id);
        if (evalOpt.isPresent()) {
            UserEvaluation eval = evalOpt.get();
            eval.setStatus(0);
            eval.setUpdatedTime(LocalDateTime.now());
            userEvaluationRepository.save(eval);
        }
    }

    /**
     * 获取高评分评价（4-5星）
     * 
     * @return 高评分评价列表
     */
    public List<UserEvaluation> findHighRatingEvaluations() {
        List<UserEvaluation> evaluations = userEvaluationRepository.findHighRatingEvaluations();
        for (UserEvaluation eval : evaluations) {
            fillEvaluationInfo(eval);
        }
        return evaluations;
    }

    /**
     * 获取低评分评价（1-3星）
     * 用于监控需要关注的评价
     * 
     * @return 低评分评价列表
     */
    public List<UserEvaluation> findLowRatingEvaluations() {
        List<UserEvaluation> evaluations = userEvaluationRepository.findLowRatingEvaluations();
        for (UserEvaluation eval : evaluations) {
            fillEvaluationInfo(eval);
        }
        return evaluations;
    }

    /**
     * 获取最新的N条评价
     * 
     * @param limit 返回数量
     * @return 最新评价列表
     */
    public List<UserEvaluation> findLatestEvaluations(int limit) {
        List<UserEvaluation> evaluations = userEvaluationRepository.findLatestEvaluations(limit);
        for (UserEvaluation eval : evaluations) {
            fillEvaluationInfo(eval);
        }
        return evaluations;
    }

    /**
     * 验证评分范围
     * 评分必须在1-5之间
     * 
     * @param rating 评分值
     */
    private void validateRating(Integer rating) {
        if (rating == null || rating < 1 || rating > 5) {
            throw new RuntimeException("评分必须在1-5之间");
        }
    }

    /**
     * 更新司机评分
     * 当有新的评价时，重新计算司机的平均评分
     * 并根据新的评分自动调整派单优先级和服务权限
     * 
     * @param driverId 司机ID
     * @param newRating 新的评分
     */
    private void updateDriverRating(Long driverId, Integer newRating) {
        Optional<Driver> driverOpt = driverRepository.findById(driverId);
        if (driverOpt.isPresent()) {
            Driver driver = driverOpt.get();
            driver.updateRating(newRating);
            driverRepository.save(driver);
        }
    }

    /**
     * 填充评价的关联信息
     * 用于页面展示时显示司机和用户的详细信息
     * 
     * @param eval 评价对象
     */
    private void fillEvaluationInfo(UserEvaluation eval) {
        if (eval.getDriverId() != null) {
            driverRepository.findById(eval.getDriverId()).ifPresent(eval::setDriver);
        }
    }

    /**
     * 统计司机的评价数量
     * 
     * @param driverId 司机ID
     * @return 评价数量
     */
    public long countByDriverId(Long driverId) {
        return userEvaluationRepository.countByDriverId(driverId);
    }

    /**
     * 检查订单是否已被评价
     * 
     * @param orderId 订单ID
     * @return 是否已评价
     */
    public boolean existsByOrderId(Long orderId) {
        return userEvaluationRepository.existsByOrderId(orderId);
    }
}

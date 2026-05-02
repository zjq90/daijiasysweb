package com.daijia.service;

import com.daijia.entity.DriverEvaluation;
import com.daijia.entity.Order;
import com.daijia.entity.User;
import com.daijia.repository.DriverEvaluationRepository;
import com.daijia.repository.OrderRepository;
import com.daijia.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 司机评价记录服务类
 * 处理司机对用户（乘客）的评价业务逻辑
 * 包括评价的创建、查询、更新、删除，以及评价对用户评分的影响
 * 特别关注用户的特殊行为标记（醉酒、更改目的地、异常行为等）
 */
@Service
public class DriverEvaluationService {

    @Autowired
    private DriverEvaluationRepository driverEvaluationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    /**
     * 获取所有有效评价记录
     * 
     * @return 评价记录列表
     */
    public List<DriverEvaluation> findAllValid() {
        List<DriverEvaluation> evaluations = driverEvaluationRepository.findByStatusOrderByCreatedTimeDesc(1);
        // 填充关联信息
        for (DriverEvaluation eval : evaluations) {
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
    public Optional<DriverEvaluation> findById(Long id) {
        Optional<DriverEvaluation> evalOpt = driverEvaluationRepository.findById(id);
        evalOpt.ifPresent(this::fillEvaluationInfo);
        return evalOpt;
    }

    /**
     * 根据用户ID查询所有评价
     * 
     * @param userId 用户ID
     * @return 评价列表
     */
    public List<DriverEvaluation> findByUserId(Long userId) {
        List<DriverEvaluation> evaluations = driverEvaluationRepository.findByUserIdOrderByCreatedTimeDesc(userId);
        for (DriverEvaluation eval : evaluations) {
            fillEvaluationInfo(eval);
        }
        return evaluations;
    }

    /**
     * 根据司机ID查询所有评价
     * 
     * @param driverId 司机ID
     * @return 评价列表
     */
    public List<DriverEvaluation> findByDriverId(Long driverId) {
        List<DriverEvaluation> evaluations = driverEvaluationRepository.findByDriverIdOrderByCreatedTimeDesc(driverId);
        for (DriverEvaluation eval : evaluations) {
            fillEvaluationInfo(eval);
        }
        return evaluations;
    }

    /**
     * 创建司机评价
     * 创建评价后会自动更新用户的平均评分
     * 如果有严重异常行为，可能会影响用户的服务权限
     * 
     * @param evaluation 评价信息
     * @return 创建后的评价
     */
    @Transactional
    public DriverEvaluation create(DriverEvaluation evaluation) {
        // 验证订单是否存在且未被评价
        Optional<Order> orderOpt = orderRepository.findById(evaluation.getOrderId());
        if (orderOpt.isEmpty()) {
            throw new RuntimeException("订单不存在");
        }
        
        Order order = orderOpt.get();
        if (order.getDriverEvaluated() != null && order.getDriverEvaluated()) {
            throw new RuntimeException("该订单已被评价");
        }

        // 验证评分范围
        validateRating(evaluation.getOverallRating());

        // 设置默认值
        if (evaluation.getStatus() == null) {
            evaluation.setStatus(1);
        }
        if (evaluation.getIsDrunk() == null) {
            evaluation.setIsDrunk(false);
        }
        if (evaluation.getIsDestinationChanged() == null) {
            evaluation.setIsDestinationChanged(false);
        }
        if (evaluation.getHasAbnormalBehavior() == null) {
            evaluation.setHasAbnormalBehavior(false);
        }
        
        evaluation.setCreatedTime(LocalDateTime.now());
        evaluation.setUpdatedTime(LocalDateTime.now());

        // 保存评价
        DriverEvaluation savedEval = driverEvaluationRepository.save(evaluation);

        // 更新用户评分
        updateUserRating(evaluation.getUserId(), evaluation.getOverallRating());

        // 更新订单评价状态
        order.setDriverEvaluated(true);
        order.setUpdatedTime(LocalDateTime.now());
        orderRepository.save(order);

        return savedEval;
    }

    /**
     * 更新评价信息
     * 注意：更新评价不会重新计算用户评分
     * 
     * @param evaluation 评价信息
     * @return 更新后的评价
     */
    @Transactional
    public DriverEvaluation update(DriverEvaluation evaluation) {
        Optional<DriverEvaluation> existingOpt = driverEvaluationRepository.findById(evaluation.getId());
        if (existingOpt.isEmpty()) {
            throw new RuntimeException("评价记录不存在");
        }

        DriverEvaluation existing = existingOpt.get();
        
        // 允许更新的字段
        if (evaluation.getContent() != null) {
            existing.setContent(evaluation.getContent());
        }
        if (evaluation.getIsDrunk() != null) {
            existing.setIsDrunk(evaluation.getIsDrunk());
        }
        if (evaluation.getIsDestinationChanged() != null) {
            existing.setIsDestinationChanged(evaluation.getIsDestinationChanged());
        }
        if (evaluation.getHasAbnormalBehavior() != null) {
            existing.setHasAbnormalBehavior(evaluation.getHasAbnormalBehavior());
        }
        
        existing.setUpdatedTime(LocalDateTime.now());
        
        return driverEvaluationRepository.save(existing);
    }

    /**
     * 删除评价（逻辑删除）
     * 将状态设置为0，而非物理删除
     * 
     * @param id 评价ID
     */
    @Transactional
    public void delete(Long id) {
        Optional<DriverEvaluation> evalOpt = driverEvaluationRepository.findById(id);
        if (evalOpt.isPresent()) {
            DriverEvaluation eval = evalOpt.get();
            eval.setStatus(0);
            eval.setUpdatedTime(LocalDateTime.now());
            driverEvaluationRepository.save(eval);
        }
    }

    /**
     * 获取有严重问题的评价记录
     * 用于监控有异常行为的用户
     * 
     * @return 问题评价列表
     */
    public List<DriverEvaluation> findEvaluationsWithSeriousIssues() {
        List<DriverEvaluation> evaluations = driverEvaluationRepository.findEvaluationsWithSeriousIssues();
        for (DriverEvaluation eval : evaluations) {
            fillEvaluationInfo(eval);
        }
        return evaluations;
    }

    /**
     * 获取有醉酒记录的评价
     * 
     * @return 醉酒记录列表
     */
    public List<DriverEvaluation> findDrunkEvaluations() {
        List<DriverEvaluation> evaluations = driverEvaluationRepository.findByIsDrunkTrueOrderByCreatedTimeDesc();
        for (DriverEvaluation eval : evaluations) {
            fillEvaluationInfo(eval);
        }
        return evaluations;
    }

    /**
     * 获取有更改目的地记录的评价
     * 
     * @return 更改目的地记录列表
     */
    public List<DriverEvaluation> findDestinationChangedEvaluations() {
        List<DriverEvaluation> evaluations = driverEvaluationRepository.findByIsDestinationChangedTrueOrderByCreatedTimeDesc();
        for (DriverEvaluation eval : evaluations) {
            fillEvaluationInfo(eval);
        }
        return evaluations;
    }

    /**
     * 获取有异常行为记录的评价
     * 
     * @return 异常行为记录列表
     */
    public List<DriverEvaluation> findAbnormalBehaviorEvaluations() {
        List<DriverEvaluation> evaluations = driverEvaluationRepository.findByHasAbnormalBehaviorTrueOrderByCreatedTimeDesc();
        for (DriverEvaluation eval : evaluations) {
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
    public List<DriverEvaluation> findLatestEvaluations(int limit) {
        List<DriverEvaluation> evaluations = driverEvaluationRepository.findLatestEvaluations(limit);
        for (DriverEvaluation eval : evaluations) {
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
     * 更新用户评分
     * 当有新的评价时，重新计算用户的平均评分
     * 
     * @param userId 用户ID
     * @param newRating 新的评分
     */
    private void updateUserRating(Long userId, Integer newRating) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.updateRating(newRating);
            userRepository.save(user);
        }
    }

    /**
     * 填充评价的关联信息
     * 用于页面展示时显示用户的详细信息
     * 
     * @param eval 评价对象
     */
    private void fillEvaluationInfo(DriverEvaluation eval) {
        if (eval.getUserId() != null) {
            userRepository.findById(eval.getUserId()).ifPresent(eval::setUser);
        }
    }

    /**
     * 统计用户的评价数量
     * 
     * @param userId 用户ID
     * @return 评价数量
     */
    public long countByUserId(Long userId) {
        return driverEvaluationRepository.countByUserId(userId);
    }

    /**
     * 统计用户的严重异常行为次数
     * 
     * @param userId 用户ID
     * @return 异常行为次数
     */
    public long countSeriousIssuesByUserId(Long userId) {
        return driverEvaluationRepository.countSeriousIssuesByUserId(userId);
    }

    /**
     * 检查订单是否已被评价
     * 
     * @param orderId 订单ID
     * @return 是否已评价
     */
    public boolean existsByOrderId(Long orderId) {
        return driverEvaluationRepository.existsByOrderId(orderId);
    }
}

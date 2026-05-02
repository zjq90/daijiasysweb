package com.daijia.service;

import com.daijia.common.Constants;
import com.daijia.entity.Evaluation;
import com.daijia.exception.BusinessException;
import com.daijia.repository.EvaluationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class EvaluationService {
    
    private final EvaluationRepository evaluationRepository;
    
    @Transactional
    public Evaluation createEvaluation(Evaluation evaluation) {
        Optional<Evaluation> existOpt = evaluationRepository.findByOrderId(evaluation.getOrderId());
        if (existOpt.isPresent()) {
            throw new BusinessException("该订单已评价");
        }
        
        evaluation.setStatus("ACTIVE");
        evaluation = evaluationRepository.save(evaluation);
        
        log.info("评价创建成功，订单ID：{}", evaluation.getOrderId());
        return evaluation;
    }
    
    public Optional<Evaluation> getByOrderId(Long orderId) {
        return evaluationRepository.findByOrderId(orderId);
    }
    
    public List<Evaluation> getByToUserId(Long toUserId) {
        return evaluationRepository.findByToUserIdOrderByCreateTimeDesc(toUserId);
    }
    
    public List<Evaluation> getByFromUserId(Long fromUserId) {
        return evaluationRepository.findByFromUserIdOrderByCreateTimeDesc(fromUserId);
    }
    
    public BigDecimal getAverageRating(Long userId, String userType) {
        BigDecimal avg = evaluationRepository.calculateAverageRating(userId, userType);
        return avg != null ? avg : new BigDecimal("5.0");
    }
    
    public long getEvaluationCount(Long userId, String userType) {
        return evaluationRepository.countByToUserIdAndUserType(userId, userType);
    }
}

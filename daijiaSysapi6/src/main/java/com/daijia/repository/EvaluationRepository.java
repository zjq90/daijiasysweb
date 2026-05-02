package com.daijia.repository;

import com.daijia.entity.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {
    
    Optional<Evaluation> findByOrderId(Long orderId);
    
    List<Evaluation> findByToUserIdOrderByCreateTimeDesc(Long toUserId);
    
    List<Evaluation> findByFromUserIdOrderByCreateTimeDesc(Long fromUserId);
    
    @Query("SELECT AVG(e.rating) FROM Evaluation e WHERE e.toUserId = :userId AND e.userType = :userType")
    BigDecimal calculateAverageRating(@Param("userId") Long userId, @Param("userType") String userType);
    
    @Query("SELECT COUNT(e) FROM Evaluation e WHERE e.toUserId = :userId AND e.userType = :userType")
    long countByToUserIdAndUserType(@Param("userId") Long userId, @Param("userType") String userType);
}

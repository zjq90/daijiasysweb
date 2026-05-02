package com.daijia.repository;

import com.daijia.entity.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Long> {
    
    @Query("SELECT p FROM Promotion p WHERE p.status = :status AND p.startTime <= :now AND (p.endTime IS NULL OR p.endTime >= :now) AND p.targetUser IN :targetUsers")
    List<Promotion> findActivePromotions(@Param("status") String status, @Param("now") LocalDateTime now, @Param("targetUsers") List<String> targetUsers);
    
    List<Promotion> findByStatusOrderByCreateTimeDesc(String status);
    
    List<Promotion> findByTypeAndStatus(String type, String status);
}

package com.daijia.repository;

import com.daijia.entity.EmergencyHelp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmergencyHelpRepository extends JpaRepository<EmergencyHelp, Long> {
    
    List<EmergencyHelp> findByUserIdOrderByCreateTimeDesc(Long userId);
    
    List<EmergencyHelp> findByOrderId(Long orderId);
    
    List<EmergencyHelp> findByStatusOrderByCreateTimeDesc(String status);
}

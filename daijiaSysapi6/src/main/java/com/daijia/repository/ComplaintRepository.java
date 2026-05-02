package com.daijia.repository;

import com.daijia.entity.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
    
    Optional<Complaint> findByOrderId(Long orderId);
    
    List<Complaint> findByComplainantIdOrderByCreateTimeDesc(Long complainantId);
    
    List<Complaint> findByRespondentIdOrderByCreateTimeDesc(Long respondentId);
    
    List<Complaint> findByStatusOrderByCreateTimeDesc(String status);
}

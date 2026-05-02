package com.daijia.repository;

import com.daijia.entity.EmergencyHelp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 安全求助数据访问接口
 * 
 * 用于处理紧急求助功能的数据操作
 * 
 * @author daijia
 * @version 1.0.0
 */
@Repository
public interface EmergencyHelpRepository extends JpaRepository<EmergencyHelp, Long> {

    Optional<EmergencyHelp> findByHelpNo(String helpNo);

    List<EmergencyHelp> findByStatus(Integer status);

    List<EmergencyHelp> findByOrderId(Long orderId);

    List<EmergencyHelp> findByUserId(Long userId);

    List<EmergencyHelp> findByDriverId(Long driverId);

    List<EmergencyHelp> findByStatusOrderByCreateTimeDesc(Integer status);

    Page<EmergencyHelp> findAll(Pageable pageable);

    Page<EmergencyHelp> findByStatus(Integer status, Pageable pageable);

    @Query("SELECT eh FROM EmergencyHelp eh WHERE eh.helpNo LIKE %?1% OR eh.address LIKE %?1% OR eh.content LIKE %?1%")
    List<EmergencyHelp> search(String keyword);

    @Query("SELECT eh FROM EmergencyHelp eh WHERE eh.createTime BETWEEN ?1 AND ?2")
    List<EmergencyHelp> findByCreateTimeBetween(LocalDateTime startTime, LocalDateTime endTime);

    @Query("SELECT COUNT(eh) FROM EmergencyHelp eh WHERE eh.status = ?1")
    long countByStatus(Integer status);

    @Query("SELECT eh FROM EmergencyHelp eh WHERE eh.status IN (0, 1) ORDER BY eh.createTime DESC")
    List<EmergencyHelp> findPendingAndProcessing();
}

package com.daijia.repository;

import com.daijia.entity.AudioRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 录音记录数据访问接口
 * 
 * 用于处理安全求助过程中的录音记录数据操作
 * 
 * @author daijia
 * @version 1.0.0
 */
@Repository
public interface AudioRecordRepository extends JpaRepository<AudioRecord, Long> {

    Optional<AudioRecord> findByRecordNo(String recordNo);

    List<AudioRecord> findByOrderId(Long orderId);

    List<AudioRecord> findByOrderIdOrderByCreateTimeDesc(Long orderId);

    List<AudioRecord> findByEmergencyHelpId(Long emergencyHelpId);

    List<AudioRecord> findByEmergencyHelpIdOrderByCreateTimeDesc(Long emergencyHelpId);

    List<AudioRecord> findByStatus(Integer status);

    Page<AudioRecord> findAll(Pageable pageable);

    Page<AudioRecord> findByStatus(Integer status, Pageable pageable);

    @Query("SELECT ar FROM AudioRecord ar WHERE ar.orderId = ?1 AND ar.status = 1 ORDER BY ar.createTime DESC")
    List<AudioRecord> findValidByOrderId(Long orderId);

    @Query("SELECT ar FROM AudioRecord ar WHERE ar.emergencyHelpId = ?1 AND ar.status = 1 ORDER BY ar.createTime DESC")
    List<AudioRecord> findValidByEmergencyHelpId(Long emergencyHelpId);

    @Query("SELECT ar FROM AudioRecord ar WHERE ar.recordStartTime BETWEEN ?1 AND ?2")
    List<AudioRecord> findByRecordStartTimeBetween(LocalDateTime startTime, LocalDateTime endTime);

    @Query("SELECT COUNT(ar) FROM AudioRecord ar WHERE ar.status = ?1")
    long countByStatus(Integer status);
}

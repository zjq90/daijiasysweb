package com.daijia.repository;

import com.daijia.entity.SystemNotice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 系统通知数据访问接口
 * 
 * @author daijia
 * @version 1.0.0
 */
@Repository
public interface SystemNoticeRepository extends JpaRepository<SystemNotice, Long> {

    List<SystemNotice> findByTargetType(Integer targetType);

    List<SystemNotice> findByTargetTypeAndTargetId(Integer targetType, Long targetId);

    List<SystemNotice> findByTargetTypeAndIsRead(Integer targetType, Integer isRead);

    List<SystemNotice> findByTargetTypeAndTargetIdAndIsRead(Integer targetType, Long targetId, Integer isRead);

    Page<SystemNotice> findByTargetType(Integer targetType, Pageable pageable);

    Page<SystemNotice> findByTargetTypeAndTargetId(Integer targetType, Long targetId, Pageable pageable);

    @Query("SELECT COUNT(sn) FROM SystemNotice sn WHERE sn.targetType = ?1 AND sn.isRead = 0")
    long countUnreadByTargetType(Integer targetType);

    @Query("SELECT COUNT(sn) FROM SystemNotice sn WHERE sn.targetType = ?1 AND sn.targetId = ?2 AND sn.isRead = 0")
    long countUnreadByTargetTypeAndTargetId(Integer targetType, Long targetId);

    @Query("SELECT sn FROM SystemNotice sn WHERE sn.createTime BETWEEN ?1 AND ?2")
    List<SystemNotice> findByCreateTimeBetween(LocalDateTime startTime, LocalDateTime endTime);

    @Query("SELECT sn FROM SystemNotice sn WHERE sn.targetType IN (?1, 0) ORDER BY sn.createTime DESC")
    List<SystemNotice> findByTargetTypeOrAll(Integer targetType);
}

package com.daijia.repository;

import com.daijia.entity.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 投诉数据访问层
 * 提供投诉数据的增删改查及统计功能
 *
 * @author daijia
 * @version 1.0.0
 */
@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {

    /**
     * 根据投诉编号查询投诉
     *
     * @param complaintNo 投诉编号
     * @return 投诉对象
     */
    Complaint findByComplaintNo(String complaintNo);

    /**
     * 根据订单ID查询投诉
     *
     * @param orderId 订单ID
     * @return 投诉列表
     */
    List<Complaint> findByOrderId(Long orderId);

    /**
     * 根据司机ID查询投诉
     *
     * @param driverId 司机ID
     * @return 投诉列表
     */
    List<Complaint> findByDriverIdOrderByComplaintTimeDesc(Long driverId);

    /**
     * 根据状态查询投诉列表
     *
     * @param status 投诉状态
     * @return 投诉列表
     */
    List<Complaint> findByStatusOrderByComplaintTimeDesc(String status);

    /**
     * 统计指定状态的投诉数量
     *
     * @param status 投诉状态
     * @return 投诉数量
     */
    long countByStatus(String status);

    /**
     * 统计指定时间范围的投诉数量
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 投诉数量
     */
    long countByComplaintTimeBetween(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 统计各类型的投诉数量
     *
     * @return 类型统计结果列表
     */
    @Query("SELECT c.complaintType, COUNT(c) FROM Complaint c GROUP BY c.complaintType")
    List<Object[]> countComplaintsByType();

    /**
     * 统计各状态的投诉数量
     *
     * @return 状态统计结果列表
     */
    @Query("SELECT c.status, COUNT(c) FROM Complaint c GROUP BY c.status")
    List<Object[]> countComplaintsByStatus();

    /**
     * 统计指定司机的有效投诉数量
     *
     * @param driverId 司机ID
     * @return 有效投诉数量
     */
    @Query("SELECT COUNT(c) FROM Complaint c WHERE c.driverId = :driverId AND c.valid = true")
    long countValidComplaintsByDriver(@Param("driverId") Long driverId);

    /**
     * 统计指定服务区域的投诉数量
     *
     * @param serviceAreaId 服务区域ID
     * @return 投诉数量
     */
    long countByServiceAreaId(Long serviceAreaId);

    /**
     * 查询待处理的投诉列表
     *
     * @return 投诉列表
     */
    @Query("SELECT c FROM Complaint c WHERE c.status IN ('PENDING', 'PROCESSING') ORDER BY c.complaintTime ASC")
    List<Complaint> findPendingComplaints();

    /**
     * 查询指定时间范围的投诉列表
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 投诉列表
     */
    List<Complaint> findByComplaintTimeBetweenOrderByComplaintTimeAsc(LocalDateTime startTime, LocalDateTime endTime);
}

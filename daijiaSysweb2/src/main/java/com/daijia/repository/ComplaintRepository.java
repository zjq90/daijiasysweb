package com.daijia.repository;

import com.daijia.entity.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 投诉单数据访问层
 * 继承JpaRepository，提供基本的CRUD操作
 */
@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {

    /**
     * 根据状态查询投诉单列表，按创建时间倒序排列
     * @param status 状态
     * @return 投诉单列表
     */
    List<Complaint> findByStatusOrderByCreateTimeDesc(String status);

    /**
     * 根据司机ID查询投诉单列表
     * @param driverId 司机ID
     * @return 投诉单列表
     */
    List<Complaint> findByDriverIdOrderByCreateTimeDesc(Long driverId);

    /**
     * 根据投诉单号查询
     * @param complaintNo 投诉单号
     * @return 投诉单
     */
    Complaint findByComplaintNo(String complaintNo);

    /**
     * 根据订单号查询投诉单
     * @param orderNo 订单号
     * @return 投诉单列表
     */
    List<Complaint> findByOrderNo(String orderNo);
}

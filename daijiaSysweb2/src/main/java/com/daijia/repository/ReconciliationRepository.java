package com.daijia.repository;

import com.daijia.entity.Reconciliation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 对账单数据访问层
 * 继承JpaRepository，提供基本的CRUD操作
 */
@Repository
public interface ReconciliationRepository extends JpaRepository<Reconciliation, Long> {

    /**
     * 根据司机ID查询对账单列表，按创建时间倒序排列
     * @param driverId 司机ID
     * @return 对账单列表
     */
    List<Reconciliation> findByDriverIdOrderByCreateTimeDesc(Long driverId);

    /**
     * 根据状态查询对账单列表
     * @param status 状态
     * @return 对账单列表
     */
    List<Reconciliation> findByStatusOrderByCreateTimeDesc(String status);

    /**
     * 根据对账单号查询
     * @param reconNo 对账单号
     * @return 对账单
     */
    Reconciliation findByReconNo(String reconNo);
}

package com.daijia.repository;

import com.daijia.entity.CommissionOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 分成单数据访问层
 * 继承JpaRepository，提供基本的CRUD操作
 */
@Repository
public interface CommissionOrderRepository extends JpaRepository<CommissionOrder, Long> {

    /**
     * 根据订单号查询分成单
     * @param orderNo 订单号
     * @return 分成单
     */
    CommissionOrder findByOrderNo(String orderNo);

    /**
     * 根据司机ID查询分成单列表，按创建时间倒序排列
     * @param driverId 司机ID
     * @return 分成单列表
     */
    List<CommissionOrder> findByDriverIdOrderByCreateTimeDesc(Long driverId);

    /**
     * 根据状态查询分成单列表
     * @param status 状态
     * @return 分成单列表
     */
    List<CommissionOrder> findByStatusOrderByCreateTimeDesc(String status);

    /**
     * 根据分成单号查询
     * @param commissionNo 分成单号
     * @return 分成单
     */
    CommissionOrder findByCommissionNo(String commissionNo);
}

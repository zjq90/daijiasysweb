package com.daijia.repository;

import com.daijia.entity.CommissionConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * 订单分成配置数据访问层
 * 继承JpaRepository，提供基本的CRUD操作
 */
@Repository
public interface CommissionConfigRepository extends JpaRepository<CommissionConfig, Long> {

    /**
     * 根据状态查询配置列表
     * @param status 状态
     * @return 配置列表
     */
    List<CommissionConfig> findByStatusOrderByMinAmountAsc(String status);

    /**
     * 根据订单金额查找匹配的配置
     * @param amount 订单金额
     * @param status 状态
     * @return 配置列表
     */
    List<CommissionConfig> findByMinAmountLessThanEqualAndMaxAmountGreaterThanEqualAndStatusOrderByMinAmountAsc(
            BigDecimal amount, BigDecimal amount2, String status);
}

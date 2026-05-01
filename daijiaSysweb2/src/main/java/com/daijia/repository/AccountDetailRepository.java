package com.daijia.repository;

import com.daijia.entity.AccountDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 账户明细数据访问层
 * 继承JpaRepository，提供基本的CRUD操作
 */
@Repository
public interface AccountDetailRepository extends JpaRepository<AccountDetail, Long> {

    /**
     * 根据账户ID查询明细列表，按创建时间倒序排列
     * @param accountId 账户ID
     * @return 明细列表
     */
    List<AccountDetail> findByAccountIdOrderByCreateTimeDesc(Long accountId);

    /**
     * 根据账户ID和类型查询明细列表
     * @param accountId 账户ID
     * @param type 类型
     * @return 明细列表
     */
    List<AccountDetail> findByAccountIdAndTypeOrderByCreateTimeDesc(Long accountId, String type);

    /**
     * 根据订单号查询明细
     * @param orderNo 订单号
     * @return 明细列表
     */
    List<AccountDetail> findByOrderNo(String orderNo);
}

package com.daijia.service;

import com.daijia.entity.AccountDetail;
import com.daijia.repository.AccountDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 账户明细服务层
 * 处理账户明细相关的业务逻辑
 */
@Service
@Transactional
public class AccountDetailService {

    @Autowired
    private AccountDetailRepository accountDetailRepository;

    /**
     * 查询所有明细
     * @return 明细列表
     */
    public List<AccountDetail> findAll() {
        return accountDetailRepository.findAll();
    }

    /**
     * 根据ID查询明细
     * @param id 明细ID
     * @return 明细信息
     */
    public Optional<AccountDetail> findById(Long id) {
        return accountDetailRepository.findById(id);
    }

    /**
     * 根据账户ID查询明细列表
     * @param accountId 账户ID
     * @return 明细列表
     */
    public List<AccountDetail> findByAccountId(Long accountId) {
        return accountDetailRepository.findByAccountIdOrderByCreateTimeDesc(accountId);
    }

    /**
     * 根据账户ID和类型查询明细
     * @param accountId 账户ID
     * @param type 类型
     * @return 明细列表
     */
    public List<AccountDetail> findByAccountIdAndType(Long accountId, String type) {
        return accountDetailRepository.findByAccountIdAndTypeOrderByCreateTimeDesc(accountId, type);
    }

    /**
     * 根据订单号查询明细
     * @param orderNo 订单号
     * @return 明细列表
     */
    public List<AccountDetail> findByOrderNo(String orderNo) {
        return accountDetailRepository.findByOrderNo(orderNo);
    }

    /**
     * 保存明细
     * @param detail 明细信息
     * @return 保存后的明细
     */
    public AccountDetail save(AccountDetail detail) {
        return accountDetailRepository.save(detail);
    }

    /**
     * 删除明细
     * @param id 明细ID
     */
    public void deleteById(Long id) {
        accountDetailRepository.deleteById(id);
    }
}

package com.daijia.service;

import com.daijia.entity.Reconciliation;
import com.daijia.repository.ReconciliationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 对账单服务层
 * 处理对账单相关的业务逻辑
 */
@Service
@Transactional
public class ReconciliationService {

    @Autowired
    private ReconciliationRepository reconciliationRepository;

    /**
     * 查询所有对账单
     * @return 对账单列表
     */
    public List<Reconciliation> findAll() {
        return reconciliationRepository.findAll();
    }

    /**
     * 根据ID查询对账单
     * @param id 对账单ID
     * @return 对账单信息
     */
    public Optional<Reconciliation> findById(Long id) {
        return reconciliationRepository.findById(id);
    }

    /**
     * 根据司机ID查询对账单
     * @param driverId 司机ID
     * @return 对账单列表
     */
    public List<Reconciliation> findByDriverId(Long driverId) {
        return reconciliationRepository.findByDriverIdOrderByCreateTimeDesc(driverId);
    }

    /**
     * 根据状态查询对账单
     * @param status 状态
     * @return 对账单列表
     */
    public List<Reconciliation> findByStatus(String status) {
        return reconciliationRepository.findByStatusOrderByCreateTimeDesc(status);
    }

    /**
     * 根据对账单号查询
     * @param reconNo 对账单号
     * @return 对账单
     */
    public Reconciliation findByReconNo(String reconNo) {
        return reconciliationRepository.findByReconNo(reconNo);
    }

    /**
     * 保存对账单
     * @param reconciliation 对账单
     * @return 保存后的对账单
     */
    public Reconciliation save(Reconciliation reconciliation) {
        // 生成对账单号
        if (reconciliation.getReconNo() == null || reconciliation.getReconNo().isEmpty()) {
            String reconNo = "RC" + System.currentTimeMillis();
            reconciliation.setReconNo(reconNo);
        }
        return reconciliationRepository.save(reconciliation);
    }

    /**
     * 确认对账单
     * @param id 对账单ID
     * @return 更新后的对账单
     */
    public Reconciliation confirm(Long id) {
        Optional<Reconciliation> reconOpt = reconciliationRepository.findById(id);
        if (reconOpt.isPresent()) {
            Reconciliation recon = reconOpt.get();
            recon.setStatus("CONFIRMED");
            recon.setConfirmTime(LocalDateTime.now());
            return reconciliationRepository.save(recon);
        }
        throw new RuntimeException("对账单不存在");
    }

    /**
     * 标记对账单有异议
     * @param id 对账单ID
     * @param remark 异议备注
     * @return 更新后的对账单
     */
    public Reconciliation dispute(Long id, String remark) {
        Optional<Reconciliation> reconOpt = reconciliationRepository.findById(id);
        if (reconOpt.isPresent()) {
            Reconciliation recon = reconOpt.get();
            recon.setStatus("DISPUTE");
            recon.setRemark(remark);
            return reconciliationRepository.save(recon);
        }
        throw new RuntimeException("对账单不存在");
    }

    /**
     * 删除对账单
     * @param id 对账单ID
     */
    public void deleteById(Long id) {
        reconciliationRepository.deleteById(id);
    }
}

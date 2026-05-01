package com.daijia.service;

import com.daijia.entity.CommissionOrder;
import com.daijia.repository.CommissionOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 分成单服务层
 * 处理分成单相关的业务逻辑
 */
@Service
@Transactional
public class CommissionOrderService {

    @Autowired
    private CommissionOrderRepository commissionOrderRepository;

    /**
     * 查询所有分成单
     * @return 分成单列表
     */
    public List<CommissionOrder> findAll() {
        return commissionOrderRepository.findAll();
    }

    /**
     * 根据ID查询分成单
     * @param id 分成单ID
     * @return 分成单信息
     */
    public Optional<CommissionOrder> findById(Long id) {
        return commissionOrderRepository.findById(id);
    }

    /**
     * 根据订单号查询分成单
     * @param orderNo 订单号
     * @return 分成单
     */
    public CommissionOrder findByOrderNo(String orderNo) {
        return commissionOrderRepository.findByOrderNo(orderNo);
    }

    /**
     * 根据司机ID查询分成单
     * @param driverId 司机ID
     * @return 分成单列表
     */
    public List<CommissionOrder> findByDriverId(Long driverId) {
        return commissionOrderRepository.findByDriverIdOrderByCreateTimeDesc(driverId);
    }

    /**
     * 根据状态查询分成单
     * @param status 状态
     * @return 分成单列表
     */
    public List<CommissionOrder> findByStatus(String status) {
        return commissionOrderRepository.findByStatusOrderByCreateTimeDesc(status);
    }

    /**
     * 保存分成单
     * @param commissionOrder 分成单
     * @return 保存后的分成单
     */
    public CommissionOrder save(CommissionOrder commissionOrder) {
        // 生成分成单号
        if (commissionOrder.getCommissionNo() == null || commissionOrder.getCommissionNo().isEmpty()) {
            String commissionNo = "CM" + System.currentTimeMillis();
            commissionOrder.setCommissionNo(commissionNo);
        }
        return commissionOrderRepository.save(commissionOrder);
    }

    /**
     * 结算分成单
     * @param id 分成单ID
     * @return 更新后的分成单
     */
    public CommissionOrder settle(Long id) {
        Optional<CommissionOrder> orderOpt = commissionOrderRepository.findById(id);
        if (orderOpt.isPresent()) {
            CommissionOrder order = orderOpt.get();
            order.setStatus("SETTLED");
            order.setSettleTime(LocalDateTime.now());
            return commissionOrderRepository.save(order);
        }
        throw new RuntimeException("分成单不存在");
    }

    /**
     * 取消分成单
     * @param id 分成单ID
     * @return 更新后的分成单
     */
    public CommissionOrder cancel(Long id) {
        Optional<CommissionOrder> orderOpt = commissionOrderRepository.findById(id);
        if (orderOpt.isPresent()) {
            CommissionOrder order = orderOpt.get();
            order.setStatus("CANCELLED");
            return commissionOrderRepository.save(order);
        }
        throw new RuntimeException("分成单不存在");
    }

    /**
     * 删除分成单
     * @param id 分成单ID
     */
    public void deleteById(Long id) {
        commissionOrderRepository.deleteById(id);
    }
}

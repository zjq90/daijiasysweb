package com.daijia.service;

import com.daijia.entity.CommissionConfig;
import com.daijia.repository.CommissionConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * 订单分成配置服务层
 * 处理订单分成配置相关的业务逻辑
 */
@Service
@Transactional
public class CommissionConfigService {

    @Autowired
    private CommissionConfigRepository commissionConfigRepository;

    /**
     * 查询所有配置
     * @return 配置列表
     */
    public List<CommissionConfig> findAll() {
        return commissionConfigRepository.findAll();
    }

    /**
     * 根据ID查询配置
     * @param id 配置ID
     * @return 配置信息
     */
    public Optional<CommissionConfig> findById(Long id) {
        return commissionConfigRepository.findById(id);
    }

    /**
     * 查询启用的配置
     * @return 配置列表
     */
    public List<CommissionConfig> findActiveConfigs() {
        return commissionConfigRepository.findByStatusOrderByMinAmountAsc("ACTIVE");
    }

    /**
     * 根据订单金额查找匹配的配置
     * @param amount 订单金额
     * @return 匹配的配置（返回第一个匹配的）
     */
    public CommissionConfig findByAmount(BigDecimal amount) {
        List<CommissionConfig> configs = commissionConfigRepository
                .findByMinAmountLessThanEqualAndMaxAmountGreaterThanEqualAndStatusOrderByMinAmountAsc(
                        amount, amount, "ACTIVE");
        if (configs != null && !configs.isEmpty()) {
            return configs.get(0);
        }
        return null;
    }

    /**
     * 保存配置
     * @param config 配置信息
     * @return 保存后的配置
     */
    public CommissionConfig save(CommissionConfig config) {
        // 验证分成比例之和应为100%
        if (config.getPlatformRate() != null && config.getDriverRate() != null) {
            if (config.getPlatformRate().add(config.getDriverRate()).compareTo(new BigDecimal("100")) != 0) {
                throw new RuntimeException("平台抽成比例与司机分成比例之和必须为100%");
            }
        }
        return commissionConfigRepository.save(config);
    }

    /**
     * 更新配置
     * @param config 配置信息
     * @return 更新后的配置
     */
    public CommissionConfig update(CommissionConfig config) {
        if (!commissionConfigRepository.existsById(config.getId())) {
            throw new RuntimeException("配置不存在");
        }
        return save(config);
    }

    /**
     * 删除配置
     * @param id 配置ID
     */
    public void deleteById(Long id) {
        if (!commissionConfigRepository.existsById(id)) {
            throw new RuntimeException("配置不存在");
        }
        commissionConfigRepository.deleteById(id);
    }
}

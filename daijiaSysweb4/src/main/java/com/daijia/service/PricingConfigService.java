package com.daijia.service;

import com.daijia.entity.PricingConfig;
import com.daijia.repository.PricingConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 * 计费配置服务类
 * 处理系统计费规则的配置和管理
 * 包括起步价、里程费、等待费、夜间加价等配置
 */
@Service
public class PricingConfigService {

    @Autowired
    private PricingConfigRepository pricingConfigRepository;

    /**
     * 获取所有配置
     * 
     * @return 配置列表
     */
    public List<PricingConfig> findAll() {
        return pricingConfigRepository.findAll();
    }

    /**
     * 获取所有启用状态的配置
     * 
     * @return 启用的配置列表
     */
    public List<PricingConfig> findAllEnabled() {
        return pricingConfigRepository.findByStatusOrderByCreatedTimeDesc(1);
    }

    /**
     * 根据ID查询配置
     * 
     * @param id 配置ID
     * @return 配置（可选）
     */
    public Optional<PricingConfig> findById(Long id) {
        return pricingConfigRepository.findById(id);
    }

    /**
     * 获取默认配置
     * 系统只能有一个默认配置
     * 
     * @return 默认配置（可选）
     */
    public Optional<PricingConfig> findDefaultConfig() {
        return pricingConfigRepository.findByIsDefaultTrue();
    }

    /**
     * 创建计费配置
     * 
     * @param config 配置信息
     * @return 创建后的配置
     */
    @Transactional
    public PricingConfig create(PricingConfig config) {
        // 验证配置名称是否已存在
        if (pricingConfigRepository.existsByConfigName(config.getConfigName())) {
            throw new RuntimeException("配置名称已存在");
        }

        // 验证必填字段
        validateConfig(config);

        // 设置默认值
        if (config.getStatus() == null) {
            config.setStatus(1);
        }
        if (config.getIsDefault() == null) {
            config.setIsDefault(false);
        }
        if (config.getStartPrice() == null) {
            config.setStartPrice(BigDecimal.ZERO);
        }
        if (config.getStartDistance() == null) {
            config.setStartDistance(new BigDecimal("3.00"));
        }
        if (config.getPerKmPrice() == null) {
            config.setPerKmPrice(BigDecimal.ZERO);
        }
        if (config.getFreeWaitTime() == null) {
            config.setFreeWaitTime(10);
        }
        if (config.getPerMinuteWaitPrice() == null) {
            config.setPerMinuteWaitPrice(BigDecimal.ZERO);
        }
        if (config.getNightSurchargeRate() == null) {
            config.setNightSurchargeRate(new BigDecimal("1.00"));
        }

        // 如果设置为默认配置，需要先清除其他配置的默认状态
        if (Boolean.TRUE.equals(config.getIsDefault())) {
            clearDefaultConfig();
        }

        return pricingConfigRepository.save(config);
    }

    /**
     * 更新计费配置
     * 
     * @param config 配置信息
     * @return 更新后的配置
     */
    @Transactional
    public PricingConfig update(PricingConfig config) {
        Optional<PricingConfig> existingOpt = pricingConfigRepository.findById(config.getId());
        if (existingOpt.isEmpty()) {
            throw new RuntimeException("配置不存在");
        }

        PricingConfig existing = existingOpt.get();

        // 验证配置名称是否重复（排除自身）
        if (config.getConfigName() != null && 
            !config.getConfigName().equals(existing.getConfigName()) &&
            pricingConfigRepository.existsByConfigNameExcludingId(config.getConfigName(), config.getId())) {
            throw new RuntimeException("配置名称已存在");
        }

        // 更新字段
        if (config.getConfigName() != null) {
            existing.setConfigName(config.getConfigName());
        }
        if (config.getStartPrice() != null) {
            existing.setStartPrice(config.getStartPrice());
        }
        if (config.getStartDistance() != null) {
            existing.setStartDistance(config.getStartDistance());
        }
        if (config.getPerKmPrice() != null) {
            existing.setPerKmPrice(config.getPerKmPrice());
        }
        if (config.getFreeWaitTime() != null) {
            existing.setFreeWaitTime(config.getFreeWaitTime());
        }
        if (config.getPerMinuteWaitPrice() != null) {
            existing.setPerMinuteWaitPrice(config.getPerMinuteWaitPrice());
        }
        if (config.getNightSurchargeStart() != null) {
            existing.setNightSurchargeStart(config.getNightSurchargeStart());
        }
        if (config.getNightSurchargeEnd() != null) {
            existing.setNightSurchargeEnd(config.getNightSurchargeEnd());
        }
        if (config.getNightSurchargeRate() != null) {
            existing.setNightSurchargeRate(config.getNightSurchargeRate());
        }
        if (config.getStatus() != null) {
            existing.setStatus(config.getStatus());
        }

        // 处理默认配置变更
        if (config.getIsDefault() != null && !config.getIsDefault().equals(existing.getIsDefault())) {
            if (Boolean.TRUE.equals(config.getIsDefault())) {
                clearDefaultConfig();
            }
            existing.setIsDefault(config.getIsDefault());
        }

        return pricingConfigRepository.save(existing);
    }

    /**
     * 删除配置
     * 
     * @param id 配置ID
     */
    @Transactional
    public void delete(Long id) {
        pricingConfigRepository.deleteById(id);
    }

    /**
     * 设置默认配置
     * 
     * @param id 配置ID
     * @return 更新后的配置
     */
    @Transactional
    public PricingConfig setAsDefault(Long id) {
        Optional<PricingConfig> configOpt = pricingConfigRepository.findById(id);
        if (configOpt.isEmpty()) {
            throw new RuntimeException("配置不存在");
        }

        // 清除其他配置的默认状态
        clearDefaultConfig();

        // 设置当前配置为默认
        PricingConfig config = configOpt.get();
        config.setIsDefault(true);
        return pricingConfigRepository.save(config);
    }

    /**
     * 启用/禁用配置
     * 
     * @param id 配置ID
     * @param enabled 是否启用
     * @return 更新后的配置
     */
    @Transactional
    public PricingConfig toggleStatus(Long id, boolean enabled) {
        Optional<PricingConfig> configOpt = pricingConfigRepository.findById(id);
        if (configOpt.isEmpty()) {
            throw new RuntimeException("配置不存在");
        }

        PricingConfig config = configOpt.get();
        config.setStatus(enabled ? 1 : 0);
        return pricingConfigRepository.save(config);
    }

    /**
     * 使用默认配置计算费用
     * 
     * @param distance 行驶距离（公里）
     * @param waitTime 等待时间（分钟）
     * @return 计算出的费用
     */
    public BigDecimal calculatePriceWithDefault(BigDecimal distance, Integer waitTime) {
        return calculatePriceWithDefault(distance, waitTime, LocalTime.now());
    }

    /**
     * 使用默认配置计算费用（指定时间）
     * 
     * @param distance 行驶距离（公里）
     * @param waitTime 等待时间（分钟）
     * @param currentTime 当前时间
     * @return 计算出的费用
     */
    public BigDecimal calculatePriceWithDefault(BigDecimal distance, Integer waitTime, LocalTime currentTime) {
        Optional<PricingConfig> defaultConfig = findDefaultConfig();
        if (defaultConfig.isEmpty()) {
            throw new RuntimeException("未找到默认计费配置");
        }
        return defaultConfig.get().calculatePrice(distance, waitTime, currentTime);
    }

    /**
     * 使用指定配置计算费用
     * 
     * @param configId 配置ID
     * @param distance 行驶距离（公里）
     * @param waitTime 等待时间（分钟）
     * @return 计算出的费用
     */
    public BigDecimal calculatePrice(Long configId, BigDecimal distance, Integer waitTime) {
        return calculatePrice(configId, distance, waitTime, LocalTime.now());
    }

    /**
     * 使用指定配置计算费用（指定时间）
     * 
     * @param configId 配置ID
     * @param distance 行驶距离（公里）
     * @param waitTime 等待时间（分钟）
     * @param currentTime 当前时间
     * @return 计算出的费用
     */
    public BigDecimal calculatePrice(Long configId, BigDecimal distance, Integer waitTime, LocalTime currentTime) {
        Optional<PricingConfig> configOpt = pricingConfigRepository.findById(configId);
        if (configOpt.isEmpty()) {
            throw new RuntimeException("计费配置不存在");
        }
        return configOpt.get().calculatePrice(distance, waitTime, currentTime);
    }

    /**
     * 验证配置参数
     * 
     * @param config 配置信息
     */
    private void validateConfig(PricingConfig config) {
        if (config.getConfigName() == null || config.getConfigName().trim().isEmpty()) {
            throw new RuntimeException("配置名称不能为空");
        }
        if (config.getStartPrice() != null && config.getStartPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("起步价不能为负数");
        }
        if (config.getStartDistance() != null && config.getStartDistance().compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("起步距离不能为负数");
        }
        if (config.getPerKmPrice() != null && config.getPerKmPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("里程费不能为负数");
        }
        if (config.getFreeWaitTime() != null && config.getFreeWaitTime() < 0) {
            throw new RuntimeException("免费等待时间不能为负数");
        }
        if (config.getPerMinuteWaitPrice() != null && config.getPerMinuteWaitPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("等待费不能为负数");
        }
    }

    /**
     * 清除所有配置的默认状态
     */
    private void clearDefaultConfig() {
        List<PricingConfig> defaultConfigs = pricingConfigRepository.findAllByIsDefaultTrue();
        for (PricingConfig config : defaultConfigs) {
            config.setIsDefault(false);
            pricingConfigRepository.save(config);
        }
    }

    /**
     * 统计启用的配置数量
     * 
     * @return 启用的配置数量
     */
    public long countEnabled() {
        return pricingConfigRepository.countByStatus(1);
    }
}

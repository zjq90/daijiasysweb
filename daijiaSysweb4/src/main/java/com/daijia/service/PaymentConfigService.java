package com.daijia.service;

import com.daijia.entity.PaymentConfig;
import com.daijia.repository.PaymentConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 支付方式配置服务类
 * 处理系统支付方式的配置和管理
 * 包括微信支付、支付宝、银联支付等多种支付方式
 * 支持预付与后付模式
 */
@Service
public class PaymentConfigService {

    @Autowired
    private PaymentConfigRepository paymentConfigRepository;

    /**
     * 获取所有配置
     * 
     * @return 配置列表
     */
    public List<PaymentConfig> findAll() {
        return paymentConfigRepository.findAll();
    }

    /**
     * 获取所有启用状态的配置
     * 
     * @return 启用的配置列表
     */
    public List<PaymentConfig> findAllEnabled() {
        return paymentConfigRepository.findByStatusOrderByCreatedTimeDesc(1);
    }

    /**
     * 根据ID查询配置
     * 
     * @param id 配置ID
     * @return 配置（可选）
     */
    public Optional<PaymentConfig> findById(Long id) {
        return paymentConfigRepository.findById(id);
    }

    /**
     * 根据支付类型查询配置
     * 
     * @param paymentType 支付类型
     * @return 配置（可选）
     */
    public Optional<PaymentConfig> findByPaymentType(String paymentType) {
        return paymentConfigRepository.findByPaymentType(paymentType);
    }

    /**
     * 获取支持预付模式的支付配置
     * 
     * @return 支持预付的配置列表
     */
    public List<PaymentConfig> findPrepayEnabledConfigs() {
        return paymentConfigRepository.findPrepayEnabledConfigs(1);
    }

    /**
     * 获取支持后付模式的支付配置
     * 
     * @return 支持后付的配置列表
     */
    public List<PaymentConfig> findPostpayEnabledConfigs() {
        return paymentConfigRepository.findPostpayEnabledConfigs(1);
    }

    /**
     * 创建支付配置
     * 
     * @param config 配置信息
     * @return 创建后的配置
     */
    @Transactional
    public PaymentConfig create(PaymentConfig config) {
        // 验证支付类型是否已存在
        if (paymentConfigRepository.existsByPaymentType(config.getPaymentType())) {
            throw new RuntimeException("该支付类型已存在配置");
        }

        // 验证支付名称是否已存在
        if (paymentConfigRepository.existsByPaymentName(config.getPaymentName())) {
            throw new RuntimeException("该支付名称已存在");
        }

        // 验证必填字段
        validateConfig(config);

        // 设置默认值
        if (config.getStatus() == null) {
            config.setStatus(1);
        }

        return paymentConfigRepository.save(config);
    }

    /**
     * 更新支付配置
     * 
     * @param config 配置信息
     * @return 更新后的配置
     */
    @Transactional
    public PaymentConfig update(PaymentConfig config) {
        Optional<PaymentConfig> existingOpt = paymentConfigRepository.findById(config.getId());
        if (existingOpt.isEmpty()) {
            throw new RuntimeException("配置不存在");
        }

        PaymentConfig existing = existingOpt.get();

        // 验证支付类型是否重复（排除自身）
        if (config.getPaymentType() != null && 
            !config.getPaymentType().equals(existing.getPaymentType()) &&
            paymentConfigRepository.existsByPaymentTypeExcludingId(config.getPaymentType(), config.getId())) {
            throw new RuntimeException("该支付类型已存在配置");
        }

        // 验证支付名称是否重复（排除自身）
        if (config.getPaymentName() != null && 
            !config.getPaymentName().equals(existing.getPaymentName()) &&
            paymentConfigRepository.existsByPaymentNameExcludingId(config.getPaymentName(), config.getId())) {
            throw new RuntimeException("该支付名称已存在");
        }

        // 更新字段
        if (config.getPaymentName() != null) {
            existing.setPaymentName(config.getPaymentName());
        }
        if (config.getPaymentType() != null) {
            existing.setPaymentType(config.getPaymentType());
        }
        if (config.getPayMode() != null) {
            existing.setPayMode(config.getPayMode());
        }
        if (config.getAppId() != null) {
            existing.setAppId(config.getAppId());
        }
        if (config.getAppSecret() != null) {
            existing.setAppSecret(config.getAppSecret());
        }
        if (config.getMerchantId() != null) {
            existing.setMerchantId(config.getMerchantId());
        }
        if (config.getApiKey() != null) {
            existing.setApiKey(config.getApiKey());
        }
        if (config.getNotifyUrl() != null) {
            existing.setNotifyUrl(config.getNotifyUrl());
        }
        if (config.getStatus() != null) {
            existing.setStatus(config.getStatus());
        }

        return paymentConfigRepository.save(existing);
    }

    /**
     * 删除配置
     * 
     * @param id 配置ID
     */
    @Transactional
    public void delete(Long id) {
        paymentConfigRepository.deleteById(id);
    }

    /**
     * 启用/禁用配置
     * 
     * @param id 配置ID
     * @param enabled 是否启用
     * @return 更新后的配置
     */
    @Transactional
    public PaymentConfig toggleStatus(Long id, boolean enabled) {
        Optional<PaymentConfig> configOpt = paymentConfigRepository.findById(id);
        if (configOpt.isEmpty()) {
            throw new RuntimeException("配置不存在");
        }

        PaymentConfig config = configOpt.get();
        config.setStatus(enabled ? 1 : 0);
        return paymentConfigRepository.save(config);
    }

    /**
     * 检查支付类型是否支持预付模式
     * 
     * @param paymentType 支付类型
     * @return 是否支持预付
     */
    public boolean supportsPrepay(String paymentType) {
        Optional<PaymentConfig> configOpt = paymentConfigRepository.findByPaymentType(paymentType);
        return configOpt.map(PaymentConfig::supportsPrepay).orElse(false);
    }

    /**
     * 检查支付类型是否支持后付模式
     * 
     * @param paymentType 支付类型
     * @return 是否支持后付
     */
    public boolean supportsPostpay(String paymentType) {
        Optional<PaymentConfig> configOpt = paymentConfigRepository.findByPaymentType(paymentType);
        return configOpt.map(PaymentConfig::supportsPostpay).orElse(false);
    }

    /**
     * 验证配置参数
     * 
     * @param config 配置信息
     */
    private void validateConfig(PaymentConfig config) {
        if (config.getPaymentName() == null || config.getPaymentName().trim().isEmpty()) {
            throw new RuntimeException("支付名称不能为空");
        }
        if (config.getPaymentType() == null || config.getPaymentType().trim().isEmpty()) {
            throw new RuntimeException("支付类型不能为空");
        }
        if (config.getPayMode() == null || config.getPayMode().trim().isEmpty()) {
            throw new RuntimeException("支付模式不能为空");
        }
        
        // 验证支付类型是否合法
        if (!"WECHAT".equals(config.getPaymentType()) && 
            !"ALIPAY".equals(config.getPaymentType()) && 
            !"UNIONPAY".equals(config.getPaymentType())) {
            throw new RuntimeException("支付类型必须是 WECHAT、ALIPAY 或 UNIONPAY");
        }
        
        // 验证支付模式是否合法
        if (!"PREPAY".equals(config.getPayMode()) && 
            !"POSTPAY".equals(config.getPayMode()) && 
            !"BOTH".equals(config.getPayMode())) {
            throw new RuntimeException("支付模式必须是 PREPAY、POSTPAY 或 BOTH");
        }
    }

    /**
     * 统计启用的配置数量
     * 
     * @return 启用的配置数量
     */
    public long countEnabled() {
        return paymentConfigRepository.countByStatus(1);
    }

    /**
     * 根据支付类型和状态查询配置
     * 
     * @param paymentType 支付类型
     * @param status 状态
     * @return 配置列表
     */
    public List<PaymentConfig> findByPaymentTypeAndStatus(String paymentType, Integer status) {
        return paymentConfigRepository.findByPaymentTypeAndStatusOrderByCreatedTimeDesc(paymentType, status);
    }
}

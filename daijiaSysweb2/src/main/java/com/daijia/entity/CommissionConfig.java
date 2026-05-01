package com.daijia.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单分成配置实体类
 * 对应数据库表：commission_config
 */
@Entity
@Table(name = "commission_config")
public class CommissionConfig {

    /**
     * 配置ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 配置名称
     */
    @Column(name = "config_name", nullable = false, length = 100)
    private String configName;

    /**
     * 平台抽成比例（%）
     */
    @Column(name = "platform_rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal platformRate;

    /**
     * 司机分成比例（%）
     */
    @Column(name = "driver_rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal driverRate;

    /**
     * 最低订单金额
     */
    @Column(name = "min_amount", precision = 10, scale = 2)
    private BigDecimal minAmount = BigDecimal.ZERO;

    /**
     * 最高订单金额
     */
    @Column(name = "max_amount", precision = 10, scale = 2)
    private BigDecimal maxAmount = new BigDecimal("999999.99");

    /**
     * 是否自动清账：0-否，1-是
     */
    @Column(name = "auto_settle")
    private Boolean autoSettle = false;

    /**
     * 自动清账延迟时间（小时）
     */
    @Column(name = "settle_delay_hours")
    private Integer settleDelayHours = 24;

    /**
     * 状态：ACTIVE-启用，INACTIVE-禁用
     */
    @Column(name = "status", length = 20)
    private String status = "ACTIVE";

    /**
     * 配置描述
     */
    @Column(name = "description", length = 200)
    private String description;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private LocalDateTime updateTime;

    /**
     * 持久化前自动设置创建时间和更新时间
     */
    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
    }

    /**
     * 更新时自动设置更新时间
     */
    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }

    // Getter and Setter methods

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public BigDecimal getPlatformRate() {
        return platformRate;
    }

    public void setPlatformRate(BigDecimal platformRate) {
        this.platformRate = platformRate;
    }

    public BigDecimal getDriverRate() {
        return driverRate;
    }

    public void setDriverRate(BigDecimal driverRate) {
        this.driverRate = driverRate;
    }

    public BigDecimal getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(BigDecimal minAmount) {
        this.minAmount = minAmount;
    }

    public BigDecimal getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(BigDecimal maxAmount) {
        this.maxAmount = maxAmount;
    }

    public Boolean getAutoSettle() {
        return autoSettle;
    }

    public void setAutoSettle(Boolean autoSettle) {
        this.autoSettle = autoSettle;
    }

    public Integer getSettleDelayHours() {
        return settleDelayHours;
    }

    public void setSettleDelayHours(Integer settleDelayHours) {
        this.settleDelayHours = settleDelayHours;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
}

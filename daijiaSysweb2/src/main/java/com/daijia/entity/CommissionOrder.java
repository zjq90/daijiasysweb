package com.daijia.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 分成单实体类
 * 对应数据库表：commission_order
 */
@Entity
@Table(name = "commission_order")
public class CommissionOrder {

    /**
     * 分成单ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 分成单号
     */
    @Column(name = "commission_no", nullable = false, length = 50)
    private String commissionNo;

    /**
     * 订单号
     */
    @Column(name = "order_no", nullable = false, length = 50)
    private String orderNo;

    /**
     * 司机ID
     */
    @Column(name = "driver_id", nullable = false)
    private Long driverId;

    /**
     * 司机姓名
     */
    @Column(name = "driver_name", length = 100)
    private String driverName;

    /**
     * 分成配置ID
     */
    @Column(name = "config_id")
    private Long configId;

    /**
     * 订单金额
     */
    @Column(name = "order_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal orderAmount;

    /**
     * 平台抽成金额
     */
    @Column(name = "platform_commission", nullable = false, precision = 10, scale = 2)
    private BigDecimal platformCommission;

    /**
     * 司机分成金额
     */
    @Column(name = "driver_commission", nullable = false, precision = 10, scale = 2)
    private BigDecimal driverCommission;

    /**
     * 状态：PENDING-待结算，SETTLED-已结算，CANCELLED-已取消
     */
    @Column(name = "status", length = 20)
    private String status = "PENDING";

    /**
     * 结算时间
     */
    @Column(name = "settle_time")
    private LocalDateTime settleTime;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private LocalDateTime createTime;

    /**
     * 平台抽成比例（用于页面显示）
     */
    @Transient
    private BigDecimal platformRate;

    /**
     * 司机分成比例（用于页面显示）
     */
    @Transient
    private BigDecimal driverRate;

    /**
     * 持久化前自动设置创建时间
     */
    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
    }

    // Getter and Setter methods

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCommissionNo() {
        return commissionNo;
    }

    public void setCommissionNo(String commissionNo) {
        this.commissionNo = commissionNo;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public Long getConfigId() {
        return configId;
    }

    public void setConfigId(Long configId) {
        this.configId = configId;
    }

    public BigDecimal getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }

    public BigDecimal getPlatformCommission() {
        return platformCommission;
    }

    public void setPlatformCommission(BigDecimal platformCommission) {
        this.platformCommission = platformCommission;
    }

    public BigDecimal getDriverCommission() {
        return driverCommission;
    }

    public void setDriverCommission(BigDecimal driverCommission) {
        this.driverCommission = driverCommission;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getSettleTime() {
        return settleTime;
    }

    public void setSettleTime(LocalDateTime settleTime) {
        this.settleTime = settleTime;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    /**
     * 兼容页面使用的platformAmount字段
     */
    public BigDecimal getPlatformAmount() {
        return platformCommission;
    }

    /**
     * 兼容页面使用的driverAmount字段
     */
    public BigDecimal getDriverAmount() {
        return driverCommission;
    }

    /**
     * 兼容页面使用的platformRate字段
     */
    public BigDecimal getPlatformRate() {
        if (platformRate != null) {
            return platformRate;
        }
        if (orderAmount != null && orderAmount.compareTo(BigDecimal.ZERO) > 0) {
            return platformCommission.multiply(new BigDecimal("100")).divide(orderAmount, 2, java.math.RoundingMode.HALF_UP);
        }
        return new BigDecimal("15");
    }

    public void setPlatformRate(BigDecimal platformRate) {
        this.platformRate = platformRate;
    }

    /**
     * 兼容页面使用的driverRate字段
     */
    public BigDecimal getDriverRate() {
        if (driverRate != null) {
            return driverRate;
        }
        if (orderAmount != null && orderAmount.compareTo(BigDecimal.ZERO) > 0) {
            return driverCommission.multiply(new BigDecimal("100")).divide(orderAmount, 2, java.math.RoundingMode.HALF_UP);
        }
        return new BigDecimal("85");
    }

    public void setDriverRate(BigDecimal driverRate) {
        this.driverRate = driverRate;
    }
}

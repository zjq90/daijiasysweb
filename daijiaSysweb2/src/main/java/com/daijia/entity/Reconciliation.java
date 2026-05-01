package com.daijia.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 对账单实体类
 * 对应数据库表：reconciliation
 */
@Entity
@Table(name = "reconciliation")
public class Reconciliation {

    /**
     * 对账单ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 对账单号
     */
    @Column(name = "recon_no", nullable = false, length = 50)
    private String reconNo;

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
     * 开始日期
     */
    @Column(name = "start_date")
    private LocalDate startDate;

    /**
     * 结束日期
     */
    @Column(name = "end_date")
    private LocalDate endDate;

    /**
     * 对账周期开始日期（兼容页面）
     */
    @Transient
    private LocalDate periodStart;

    /**
     * 对账周期结束日期（兼容页面）
     */
    @Transient
    private LocalDate periodEnd;

    /**
     * 总订单数
     */
    @Column(name = "total_orders")
    private Integer totalOrders = 0;

    /**
     * 总订单金额
     */
    @Column(name = "total_amount", precision = 10, scale = 2)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    /**
     * 总分成金额
     */
    @Column(name = "total_commission", precision = 10, scale = 2)
    private BigDecimal totalCommission = BigDecimal.ZERO;

    /**
     * 状态：PENDING-待确认，CONFIRMED-已确认，DISPUTE-有异议
     */
    @Column(name = "status", length = 20)
    private String status = "PENDING";

    /**
     * 备注
     */
    @Column(name = "remark", length = 200)
    private String remark;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private LocalDateTime createTime;

    /**
     * 确认时间
     */
    @Column(name = "confirm_time")
    private LocalDateTime confirmTime;

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

    public String getReconNo() {
        return reconNo;
    }

    public void setReconNo(String reconNo) {
        this.reconNo = reconNo;
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

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    /**
     * 兼容页面使用的periodStart字段
     */
    public LocalDate getPeriodStart() {
        return periodStart != null ? periodStart : startDate;
    }

    public void setPeriodStart(LocalDate periodStart) {
        this.periodStart = periodStart;
        if (this.startDate == null) {
            this.startDate = periodStart;
        }
    }

    /**
     * 兼容页面使用的periodEnd字段
     */
    public LocalDate getPeriodEnd() {
        return periodEnd != null ? periodEnd : endDate;
    }

    public void setPeriodEnd(LocalDate periodEnd) {
        this.periodEnd = periodEnd;
        if (this.endDate == null) {
            this.endDate = periodEnd;
        }
    }

    public Integer getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(Integer totalOrders) {
        this.totalOrders = totalOrders;
    }

    /**
     * 兼容页面使用的orderCount字段
     */
    public Integer getOrderCount() {
        return totalOrders;
    }

    public void setOrderCount(Integer orderCount) {
        this.totalOrders = orderCount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getTotalCommission() {
        return totalCommission;
    }

    public void setTotalCommission(BigDecimal totalCommission) {
        this.totalCommission = totalCommission;
    }

    /**
     * 兼容页面使用的platformAmount字段（平台抽成金额）
     */
    public BigDecimal getPlatformAmount() {
        if (totalAmount != null && totalCommission != null) {
            return totalAmount.subtract(totalCommission);
        }
        return BigDecimal.ZERO;
    }

    /**
     * 兼容页面使用的driverAmount字段（司机分成金额）
     */
    public BigDecimal getDriverAmount() {
        return totalCommission;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(LocalDateTime confirmTime) {
        this.confirmTime = confirmTime;
    }
}

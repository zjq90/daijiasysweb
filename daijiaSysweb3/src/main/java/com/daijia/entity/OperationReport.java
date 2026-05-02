package com.daijia.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 运营报表实体类
 * 用于存储日/周/月运营统计数据，支持报表分析
 *
 * @author daijia
 * @version 1.0.0
 */
@Entity
@Table(name = "operation_report")
public class OperationReport {

    /**
     * 报表主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 报表日期
     */
    private LocalDate reportDate;

    /**
     * 报表类型
     * DAILY - 日报
     * WEEKLY - 周报
     * MONTHLY - 月报
     */
    @Column(length = 32)
    private String reportType;

    /**
     * 服务区域ID（可空，为空表示全平台统计）
     */
    private Long serviceAreaId;

    /**
     * 订单数据
     * 当日订单总数
     */
    private Integer totalOrders;

    /**
     * 当日完成订单数
     */
    private Integer completedOrders;

    /**
     * 当日取消订单数
     */
    private Integer cancelledOrders;

    /**
     * 成交率 = 完成订单数 / 总订单数
     */
    private BigDecimal completionRate;

    /**
     * 取消率 = 取消订单数 / 总订单数
     */
    private BigDecimal cancellationRate;

    /**
     * 收入数据
     * 当日总营收
     */
    private BigDecimal totalRevenue;

    /**
     * 当日平均订单金额
     */
    private BigDecimal avgOrderAmount;

    /**
     * 司机数据
     * 当日在线司机数
     */
    private Integer onlineDrivers;

    /**
     * 当日活跃司机数（有接单的司机）
     */
    private Integer activeDrivers;

    /**
     * 司机平均收入
     */
    private BigDecimal avgDriverIncome;

    /**
     * 投诉数据
     * 当日投诉数
     */
    private Integer complaintCount;

    /**
     * 投诉率 = 投诉数 / 完成订单数
     */
    private BigDecimal complaintRate;

    /**
     * 评价数据
     * 平均评分
     */
    private BigDecimal avgRating;

    /**
     * 5星好评数
     */
    private Integer fiveStarCount;

    /**
     * 营销数据
     * 使用优惠订单数
     */
    private Integer promotionOrders;

    /**
     * 优惠总金额
     */
    private BigDecimal totalDiscount;

    /**
     * 激励奖励发放金额
     */
    private BigDecimal totalIncentive;

    /**
     * 状态
     * GENERATING - 生成中
     * COMPLETED - 已完成
     * ERROR - 生成失败
     */
    @Column(length = 32)
    private String status;

    /**
     * 备注
     */
    @Column(length = 500)
    private String remark;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 构造方法 - 设置默认值
     */
    public OperationReport() {
        this.totalOrders = 0;
        this.completedOrders = 0;
        this.cancelledOrders = 0;
        this.completionRate = new BigDecimal("0.00");
        this.cancellationRate = new BigDecimal("0.00");
        this.totalRevenue = new BigDecimal("0.00");
        this.avgOrderAmount = new BigDecimal("0.00");
        this.onlineDrivers = 0;
        this.activeDrivers = 0;
        this.avgDriverIncome = new BigDecimal("0.00");
        this.complaintCount = 0;
        this.complaintRate = new BigDecimal("0.00");
        this.avgRating = new BigDecimal("5.00");
        this.fiveStarCount = 0;
        this.promotionOrders = 0;
        this.totalDiscount = new BigDecimal("0.00");
        this.totalIncentive = new BigDecimal("0.00");
        this.status = "GENERATING";
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getReportDate() {
        return reportDate;
    }

    public void setReportDate(LocalDate reportDate) {
        this.reportDate = reportDate;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public Long getServiceAreaId() {
        return serviceAreaId;
    }

    public void setServiceAreaId(Long serviceAreaId) {
        this.serviceAreaId = serviceAreaId;
    }

    public Integer getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(Integer totalOrders) {
        this.totalOrders = totalOrders;
    }

    public Integer getCompletedOrders() {
        return completedOrders;
    }

    public void setCompletedOrders(Integer completedOrders) {
        this.completedOrders = completedOrders;
    }

    public Integer getCancelledOrders() {
        return cancelledOrders;
    }

    public void setCancelledOrders(Integer cancelledOrders) {
        this.cancelledOrders = cancelledOrders;
    }

    public BigDecimal getCompletionRate() {
        return completionRate;
    }

    public void setCompletionRate(BigDecimal completionRate) {
        this.completionRate = completionRate;
    }

    public BigDecimal getCancellationRate() {
        return cancellationRate;
    }

    public void setCancellationRate(BigDecimal cancellationRate) {
        this.cancellationRate = cancellationRate;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public BigDecimal getAvgOrderAmount() {
        return avgOrderAmount;
    }

    public void setAvgOrderAmount(BigDecimal avgOrderAmount) {
        this.avgOrderAmount = avgOrderAmount;
    }

    public Integer getOnlineDrivers() {
        return onlineDrivers;
    }

    public void setOnlineDrivers(Integer onlineDrivers) {
        this.onlineDrivers = onlineDrivers;
    }

    public Integer getActiveDrivers() {
        return activeDrivers;
    }

    public void setActiveDrivers(Integer activeDrivers) {
        this.activeDrivers = activeDrivers;
    }

    public BigDecimal getAvgDriverIncome() {
        return avgDriverIncome;
    }

    public void setAvgDriverIncome(BigDecimal avgDriverIncome) {
        this.avgDriverIncome = avgDriverIncome;
    }

    public Integer getComplaintCount() {
        return complaintCount;
    }

    public void setComplaintCount(Integer complaintCount) {
        this.complaintCount = complaintCount;
    }

    public BigDecimal getComplaintRate() {
        return complaintRate;
    }

    public void setComplaintRate(BigDecimal complaintRate) {
        this.complaintRate = complaintRate;
    }

    public BigDecimal getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(BigDecimal avgRating) {
        this.avgRating = avgRating;
    }

    public Integer getFiveStarCount() {
        return fiveStarCount;
    }

    public void setFiveStarCount(Integer fiveStarCount) {
        this.fiveStarCount = fiveStarCount;
    }

    public Integer getPromotionOrders() {
        return promotionOrders;
    }

    public void setPromotionOrders(Integer promotionOrders) {
        this.promotionOrders = promotionOrders;
    }

    public BigDecimal getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(BigDecimal totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public BigDecimal getTotalIncentive() {
        return totalIncentive;
    }

    public void setTotalIncentive(BigDecimal totalIncentive) {
        this.totalIncentive = totalIncentive;
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

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
}

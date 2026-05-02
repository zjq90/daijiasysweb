package com.daijia.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 激励奖励活动实体类
 * 用于管理司机激励奖励政策，如限时冲单奖、高峰补贴等
 *
 * @author daijia
 * @version 1.0.0
 */
@Entity
@Table(name = "incentive_activity")
public class IncentiveActivity {

    /**
     * 活动主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 活动编号（唯一）
     */
    @Column(unique = true, nullable = false, length = 32)
    private String activityNo;

    /**
     * 活动名称
     */
    @Column(length = 128)
    private String activityName;

    /**
     * 活动类型
     * ORDER_BONUS - 订单奖励（冲单奖）
     * RUSH_HOUR_BONUS - 高峰补贴
     * NEW_DRIVER_BONUS - 新司机入职奖励
     * ATTENDANCE_BONUS - 出勤奖励
     * AREA_BONUS - 区域补贴
     * OTHER - 其他奖励
     */
    @Column(length = 64)
    private String activityType;

    /**
     * 活动描述
     */
    @Column(length = 1000)
    private String description;

    /**
     * 活动规则（JSON格式存储复杂规则）
     */
    @Column(columnDefinition = "TEXT")
    private String rules;

    /**
     * 奖励类型
     * FIXED_AMOUNT - 固定金额
     * PER_ORDER - 每单金额
     * PERCENTAGE - 按订单金额比例
     * TIERED - 阶梯奖励
     */
    @Column(length = 64)
    private String rewardType;

    /**
     * 固定奖励金额
     */
    private BigDecimal fixedAmount;

    /**
     * 每单奖励金额
     */
    private BigDecimal perOrderAmount;

    /**
     * 订单金额比例（百分比，如10表示10%）
     */
    private BigDecimal percentage;

    /**
     * 最低订单数要求
     */
    private Integer minOrders;

    /**
     * 最高奖励金额（封顶）
     */
    private BigDecimal maxAmount;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 适用时间规则（JSON格式，如工作日/周末/特定时间段）
     */
    @Column(columnDefinition = "TEXT")
    private String timeRules;

    /**
     * 适用服务区域ID（多个用逗号分隔，为空表示全部区域）
     */
    @Column(length = 500)
    private String serviceAreaIds;

    /**
     * 适用司机ID列表（多个用逗号分隔，为空表示全部司机）
     */
    @Column(length = 2000)
    private String driverIds;

    /**
     * 活动状态
     * DRAFT - 草稿
     * PENDING - 待发布
     * ACTIVE - 进行中
     * PAUSED - 暂停
     * EXPIRED - 已过期
     * CANCELLED - 已取消
     */
    @Column(length = 32)
    private String status;

    /**
     * 预算金额
     */
    private BigDecimal budgetAmount;

    /**
     * 已使用金额
     */
    private BigDecimal usedAmount;

    /**
     * 受益司机数
     */
    private Integer benefitedDrivers;

    /**
     * 受益订单数
     */
    private Integer benefitedOrders;

    /**
     * 排序
     */
    private Integer sortOrder;

    /**
     * 备注
     */
    @Column(length = 500)
    private String remark;

    /**
     * 创建人ID
     */
    private Long creatorId;

    /**
     * 发布时间
     */
    private LocalDateTime publishTime;

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
    public IncentiveActivity() {
        this.status = "DRAFT";
        this.budgetAmount = new BigDecimal("0.00");
        this.usedAmount = new BigDecimal("0.00");
        this.benefitedDrivers = 0;
        this.benefitedOrders = 0;
        this.sortOrder = 0;
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

    public String getActivityNo() {
        return activityNo;
    }

    public void setActivityNo(String activityNo) {
        this.activityNo = activityNo;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }

    public String getRewardType() {
        return rewardType;
    }

    public void setRewardType(String rewardType) {
        this.rewardType = rewardType;
    }

    public BigDecimal getFixedAmount() {
        return fixedAmount;
    }

    public void setFixedAmount(BigDecimal fixedAmount) {
        this.fixedAmount = fixedAmount;
    }

    public BigDecimal getPerOrderAmount() {
        return perOrderAmount;
    }

    public void setPerOrderAmount(BigDecimal perOrderAmount) {
        this.perOrderAmount = perOrderAmount;
    }

    public BigDecimal getPercentage() {
        return percentage;
    }

    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
    }

    public Integer getMinOrders() {
        return minOrders;
    }

    public void setMinOrders(Integer minOrders) {
        this.minOrders = minOrders;
    }

    public BigDecimal getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(BigDecimal maxAmount) {
        this.maxAmount = maxAmount;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getTimeRules() {
        return timeRules;
    }

    public void setTimeRules(String timeRules) {
        this.timeRules = timeRules;
    }

    public String getServiceAreaIds() {
        return serviceAreaIds;
    }

    public void setServiceAreaIds(String serviceAreaIds) {
        this.serviceAreaIds = serviceAreaIds;
    }

    public String getDriverIds() {
        return driverIds;
    }

    public void setDriverIds(String driverIds) {
        this.driverIds = driverIds;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getBudgetAmount() {
        return budgetAmount;
    }

    public void setBudgetAmount(BigDecimal budgetAmount) {
        this.budgetAmount = budgetAmount;
    }

    public BigDecimal getUsedAmount() {
        return usedAmount;
    }

    public void setUsedAmount(BigDecimal usedAmount) {
        this.usedAmount = usedAmount;
    }

    public Integer getBenefitedDrivers() {
        return benefitedDrivers;
    }

    public void setBenefitedDrivers(Integer benefitedDrivers) {
        this.benefitedDrivers = benefitedDrivers;
    }

    public Integer getBenefitedOrders() {
        return benefitedOrders;
    }

    public void setBenefitedOrders(Integer benefitedOrders) {
        this.benefitedOrders = benefitedOrders;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public LocalDateTime getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(LocalDateTime publishTime) {
        this.publishTime = publishTime;
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

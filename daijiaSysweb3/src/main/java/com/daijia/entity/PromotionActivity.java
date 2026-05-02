package com.daijia.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 优惠活动实体类
 * 用于管理乘客优惠活动，如满减、折扣、新人优惠等
 *
 * @author daijia
 * @version 1.0.0
 */
@Entity
@Table(name = "promotion_activity")
public class PromotionActivity {

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
     * NEW_USER - 新人专享
     * FULL_REDUCTION - 满减优惠
     * DISCOUNT - 折扣优惠
     * COUPON - 优惠券
     * TIME_LIMITED - 限时优惠
     * LUCKY_DRAW - 抽奖活动
     * OTHER - 其他优惠
     */
    @Column(length = 64)
    private String activityType;

    /**
     * 优惠类型
     * FIXED_AMOUNT - 固定金额减免
     * PERCENTAGE - 百分比折扣
     * FULL_REDUCTION - 满减（满X减Y）
     */
    @Column(length = 64)
    private String discountType;

    /**
     * 满减门槛金额（满X元可用）
     */
    private BigDecimal thresholdAmount;

    /**
     * 减免金额
     */
    private BigDecimal discountAmount;

    /**
     * 折扣比例（如0.8表示8折）
     */
    private BigDecimal discountRate;

    /**
     * 最大减免金额（封顶）
     */
    private BigDecimal maxDiscountAmount;

    /**
     * 活动描述
     */
    @Column(length = 1000)
    private String description;

    /**
     * 使用规则（JSON格式）
     */
    @Column(columnDefinition = "TEXT")
    private String useRules;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 适用时间规则（JSON格式）
     */
    @Column(columnDefinition = "TEXT")
    private String timeRules;

    /**
     * 适用服务区域ID（多个用逗号分隔，为空表示全部区域）
     */
    @Column(length = 500)
    private String serviceAreaIds;

    /**
     * 适用用户类型
     * ALL - 全部用户
     * NEW_USER - 仅新用户
     * VIP_USER - 仅VIP用户
     * SPECIFIC - 指定用户
     */
    @Column(length = 32)
    private String applicableUserType;

    /**
     * 适用用户ID列表（多个用逗号分隔）
     */
    @Column(length = 2000)
    private String userIds;

    /**
     * 发放总量（优惠券数量限制）
     */
    private Integer totalQuantity;

    /**
     * 已发放数量
     */
    private Integer issuedQuantity;

    /**
     * 已使用数量
     */
    private Integer usedQuantity;

    /**
     * 每人限领数量
     */
    private Integer perUserLimit;

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
     * 是否自动发放
     */
    private Boolean autoIssue;

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
    public PromotionActivity() {
        this.status = "DRAFT";
        this.applicableUserType = "ALL";
        this.totalQuantity = 0;
        this.issuedQuantity = 0;
        this.usedQuantity = 0;
        this.perUserLimit = 1;
        this.budgetAmount = new BigDecimal("0.00");
        this.usedAmount = new BigDecimal("0.00");
        this.autoIssue = false;
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

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public BigDecimal getThresholdAmount() {
        return thresholdAmount;
    }

    public void setThresholdAmount(BigDecimal thresholdAmount) {
        this.thresholdAmount = thresholdAmount;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(BigDecimal discountRate) {
        this.discountRate = discountRate;
    }

    public BigDecimal getMaxDiscountAmount() {
        return maxDiscountAmount;
    }

    public void setMaxDiscountAmount(BigDecimal maxDiscountAmount) {
        this.maxDiscountAmount = maxDiscountAmount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUseRules() {
        return useRules;
    }

    public void setUseRules(String useRules) {
        this.useRules = useRules;
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

    public String getApplicableUserType() {
        return applicableUserType;
    }

    public void setApplicableUserType(String applicableUserType) {
        this.applicableUserType = applicableUserType;
    }

    public String getUserIds() {
        return userIds;
    }

    public void setUserIds(String userIds) {
        this.userIds = userIds;
    }

    public Integer getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(Integer totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public Integer getIssuedQuantity() {
        return issuedQuantity;
    }

    public void setIssuedQuantity(Integer issuedQuantity) {
        this.issuedQuantity = issuedQuantity;
    }

    public Integer getUsedQuantity() {
        return usedQuantity;
    }

    public void setUsedQuantity(Integer usedQuantity) {
        this.usedQuantity = usedQuantity;
    }

    public Integer getPerUserLimit() {
        return perUserLimit;
    }

    public void setPerUserLimit(Integer perUserLimit) {
        this.perUserLimit = perUserLimit;
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

    public Boolean getAutoIssue() {
        return autoIssue;
    }

    public void setAutoIssue(Boolean autoIssue) {
        this.autoIssue = autoIssue;
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

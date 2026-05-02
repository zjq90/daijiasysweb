package com.daijia.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单实体类
 * 
 * 对应数据库表：t_order
 * 
 * 订单状态说明：
 * 0: 待接单（用户已下单，等待司机接单）
 * 1: 已接单（司机已接单）
 * 2: 已接驾（司机已到达接驾地点）
 * 3: 服务中（行程进行中）
 * 4: 已完成（行程结束）
 * 5: 已取消（订单被取消）
 * 
 * @author daijia
 * @version 1.0.0
 */
@Entity
@Table(name = "t_order")
public class Order {

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 订单号
     */
    @Column(name = "order_no", unique = true, nullable = false, length = 32)
    private String orderNo;

    /**
     * 用户ID
     */
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /**
     * 司机ID
     */
    @Column(name = "driver_id")
    private Long driverId;

    /**
     * 出发地地址
     */
    @Column(name = "start_address", nullable = false, length = 200)
    private String startAddress;

    /**
     * 出发地纬度
     */
    @Column(name = "start_latitude", nullable = false, precision = 10, scale = 7)
    private BigDecimal startLatitude;

    /**
     * 出发地经度
     */
    @Column(name = "start_longitude", nullable = false, precision = 10, scale = 7)
    private BigDecimal startLongitude;

    /**
     * 目的地地址
     */
    @Column(name = "end_address", nullable = false, length = 200)
    private String endAddress;

    /**
     * 目的地纬度
     */
    @Column(name = "end_latitude", nullable = false, precision = 10, scale = 7)
    private BigDecimal endLatitude;

    /**
     * 目的地经度
     */
    @Column(name = "end_longitude", nullable = false, precision = 10, scale = 7)
    private BigDecimal endLongitude;

    /**
     * 预估距离（公里）
     */
    @Column(name = "estimate_distance", precision = 10, scale = 2)
    private BigDecimal estimateDistance;

    /**
     * 预估时间（分钟）
     */
    @Column(name = "estimate_time")
    private Integer estimateTime;

    /**
     * 预估费用
     */
    @Column(name = "estimate_amount", precision = 10, scale = 2)
    private BigDecimal estimateAmount;

    /**
     * 实际距离（公里）
     */
    @Column(name = "actual_distance", precision = 10, scale = 2)
    private BigDecimal actualDistance;

    /**
     * 实际时间（分钟）
     */
    @Column(name = "actual_time")
    private Integer actualTime;

    /**
     * 实际费用
     */
    @Column(name = "actual_amount", precision = 10, scale = 2)
    private BigDecimal actualAmount;

    /**
     * 订单状态
     * 0: 待接单, 1: 已接单, 2: 已接驾, 3: 服务中, 4: 已完成, 5: 已取消
     */
    @Column(name = "status")
    private Integer status;

    /**
     * 下单时间
     */
    @Column(name = "order_time")
    private LocalDateTime orderTime;

    /**
     * 接单时间
     */
    @Column(name = "accept_time")
    private LocalDateTime acceptTime;

    /**
     * 接驾时间
     */
    @Column(name = "pickup_time")
    private LocalDateTime pickupTime;

    /**
     * 行程开始时间
     */
    @Column(name = "start_time")
    private LocalDateTime startTime;

    /**
     * 完成时间
     */
    @Column(name = "complete_time")
    private LocalDateTime completeTime;

    /**
     * 取消时间
     */
    @Column(name = "cancel_time")
    private LocalDateTime cancelTime;

    /**
     * 取消原因
     */
    @Column(name = "cancel_reason", length = 200)
    private String cancelReason;

    /**
     * 评分（1-5星）
     */
    @Column(name = "rating")
    private Integer rating;

    /**
     * 评价内容
     */
    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;

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

    @Transient
    private User user;

    @Transient
    private Driver driver;

    public Order() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public String getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    public BigDecimal getStartLatitude() {
        return startLatitude;
    }

    public void setStartLatitude(BigDecimal startLatitude) {
        this.startLatitude = startLatitude;
    }

    public BigDecimal getStartLongitude() {
        return startLongitude;
    }

    public void setStartLongitude(BigDecimal startLongitude) {
        this.startLongitude = startLongitude;
    }

    public String getEndAddress() {
        return endAddress;
    }

    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
    }

    public BigDecimal getEndLatitude() {
        return endLatitude;
    }

    public void setEndLatitude(BigDecimal endLatitude) {
        this.endLatitude = endLatitude;
    }

    public BigDecimal getEndLongitude() {
        return endLongitude;
    }

    public void setEndLongitude(BigDecimal endLongitude) {
        this.endLongitude = endLongitude;
    }

    public BigDecimal getEstimateDistance() {
        return estimateDistance;
    }

    public void setEstimateDistance(BigDecimal estimateDistance) {
        this.estimateDistance = estimateDistance;
    }

    public Integer getEstimateTime() {
        return estimateTime;
    }

    public void setEstimateTime(Integer estimateTime) {
        this.estimateTime = estimateTime;
    }

    public BigDecimal getEstimateAmount() {
        return estimateAmount;
    }

    public void setEstimateAmount(BigDecimal estimateAmount) {
        this.estimateAmount = estimateAmount;
    }

    public BigDecimal getActualDistance() {
        return actualDistance;
    }

    public void setActualDistance(BigDecimal actualDistance) {
        this.actualDistance = actualDistance;
    }

    public Integer getActualTime() {
        return actualTime;
    }

    public void setActualTime(Integer actualTime) {
        this.actualTime = actualTime;
    }

    public BigDecimal getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(BigDecimal actualAmount) {
        this.actualAmount = actualAmount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(LocalDateTime orderTime) {
        this.orderTime = orderTime;
    }

    public LocalDateTime getAcceptTime() {
        return acceptTime;
    }

    public void setAcceptTime(LocalDateTime acceptTime) {
        this.acceptTime = acceptTime;
    }

    public LocalDateTime getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(LocalDateTime pickupTime) {
        this.pickupTime = pickupTime;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(LocalDateTime completeTime) {
        this.completeTime = completeTime;
    }

    public LocalDateTime getCancelTime() {
        return cancelTime;
    }

    public void setCancelTime(LocalDateTime cancelTime) {
        this.cancelTime = cancelTime;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    @PrePersist
    public void prePersist() {
        if (this.createTime == null) {
            this.createTime = LocalDateTime.now();
        }
        if (this.updateTime == null) {
            this.updateTime = LocalDateTime.now();
        }
        if (this.status == null) {
            this.status = 0;
        }
        if (this.orderTime == null) {
            this.orderTime = LocalDateTime.now();
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updateTime = LocalDateTime.now();
    }
}

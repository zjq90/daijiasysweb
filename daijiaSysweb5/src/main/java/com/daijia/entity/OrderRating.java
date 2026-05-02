package com.daijia.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 订单评价实体类
 * 
 * 对应数据库表：t_order_rating
 * 
 * 用于存储用户对订单的评价信息，包括评分、评论、标签等
 * 
 * 评分等级：
 * 1: 很差
 * 2: 较差
 * 3: 一般
 * 4: 较好
 * 5: 很好
 * 
 * @author daijia
 * @version 1.0.0
 */
@Entity
@Table(name = "t_order_rating")
public class OrderRating {

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 关联订单ID
     */
    @Column(name = "order_id", unique = true, nullable = false)
    private Long orderId;

    /**
     * 用户ID（乘客）
     */
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /**
     * 司机ID
     */
    @Column(name = "driver_id", nullable = false)
    private Long driverId;

    /**
     * 评分（1-5星）
     * 1: 很差, 2: 较差, 3: 一般, 4: 较好, 5: 很好
     */
    @Column(name = "rating", nullable = false)
    private Integer rating;

    /**
     * 评价内容
     */
    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;

    /**
     * 评价标签（逗号分隔，如："服务好,驾驶平稳,准时"）
     */
    @Column(name = "tags", length = 200)
    private String tags;

    /**
     * 是否匿名评价
     * 0: 实名, 1: 匿名
     */
    @Column(name = "anonymous")
    private Integer anonymous;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private LocalDateTime createTime;

    @Transient
    private Order order;

    @Transient
    private User user;

    @Transient
    private Driver driver;

    public OrderRating() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
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

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Integer getAnonymous() {
        return anonymous;
    }

    public void setAnonymous(Integer anonymous) {
        this.anonymous = anonymous;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
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
        if (this.anonymous == null) {
            this.anonymous = 0;
        }
    }
}

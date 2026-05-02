package com.daijia.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 司机评价记录实体类
 * 存储司机对用户（乘客）的评价信息
 * 包括总体评分、是否醉酒、是否更改目的地、是否有异常行为等特殊标记
 * 该评价会影响用户的平均评分和后续服务
 */
@Data
@Entity
@Table(name = "driver_evaluations")
public class DriverEvaluation {

    /**
     * 评价记录唯一标识ID，主键，自增
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 关联的订单ID
     */
    @Column(name = "order_id", nullable = false)
    private Long orderId;

    /**
     * 评价司机ID
     */
    @Column(name = "driver_id", nullable = false)
    private Long driverId;

    /**
     * 被评价用户（乘客）ID
     */
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /**
     * 总体评分：1-5星
     * 这是司机对用户的整体评价
     * 该评分直接影响用户的平均评分
     */
    @Column(name = "overall_rating", nullable = false)
    private Integer overallRating;

    /**
     * 是否醉酒
     * 司机记录用户是否处于醉酒状态
     * 醉酒状态会影响用户的信用评分和服务权限
     */
    @Column(name = "is_drunk")
    private Boolean isDrunk = false;

    /**
     * 是否更改目的地
     * 司机记录用户是否在行程中更改目的地
     * 更改目的地会影响订单计费和司机体验
     */
    @Column(name = "is_destination_changed")
    private Boolean isDestinationChanged = false;

    /**
     * 是否有异常行为
     * 司机记录用户是否有异常行为（如暴力倾向、骚扰司机等）
     * 异常行为会严重影响用户的信用评分
     */
    @Column(name = "has_abnormal_behavior")
    private Boolean hasAbnormalBehavior = false;

    /**
     * 评价内容
     * 司机可以填写详细的评价文字
     */
    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    /**
     * 评价状态：1-有效，0-已删除
     */
    @Column(name = "status", nullable = false)
    private Integer status = 1;

    /**
     * 创建时间
     */
    @Column(name = "created_time", updatable = false)
    private LocalDateTime createdTime = LocalDateTime.now();

    /**
     * 更新时间
     */
    @Column(name = "updated_time")
    private LocalDateTime updatedTime = LocalDateTime.now();

    /**
     * 评价司机信息 - 非持久化字段，用于页面展示
     */
    @Transient
    private Driver driver;

    /**
     * 被评价用户（乘客）信息 - 非持久化字段，用于页面展示
     */
    @Transient
    private User user;

    /**
     * 获取评分星星显示HTML
     * 用于前端页面显示星级评分
     * @return 星星HTML字符串
     */
    public String getRatingStars() {
        if (overallRating == null) {
            return "";
        }
        StringBuilder stars = new StringBuilder();
        for (int i = 1; i <= 5; i++) {
            if (i <= overallRating) {
                stars.append("<span class=\"text-warning\">★</span>");
            } else {
                stars.append("<span class=\"text-muted\">☆</span>");
            }
        }
        return stars.toString();
    }

    /**
     * 获取特殊标记说明
     * 用于展示用户的特殊行为标记
     * @return 特殊标记描述字符串
     */
    public String getSpecialFlags() {
        StringBuilder flags = new StringBuilder();
        if (isDrunk != null && isDrunk) {
            flags.append("<span class=\"badge bg-warning text-dark me-1\">醉酒</span>");
        }
        if (isDestinationChanged != null && isDestinationChanged) {
            flags.append("<span class=\"badge bg-info me-1\">更改目的地</span>");
        }
        if (hasAbnormalBehavior != null && hasAbnormalBehavior) {
            flags.append("<span class=\"badge bg-danger me-1\">异常行为</span>");
        }
        return flags.toString();
    }

    /**
     * 判断是否有严重不良行为
     * 用于判断用户是否存在需要特殊处理的不良行为
     * @return 是否有严重不良行为
     */
    public Boolean hasSeriousIssues() {
        return (hasAbnormalBehavior != null && hasAbnormalBehavior) ||
               (isDrunk != null && isDrunk && overallRating != null && overallRating <= 2);
    }
}

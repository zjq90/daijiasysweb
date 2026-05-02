package com.daijia.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 用户评价记录实体类
 * 存储用户（乘客）对司机的评价信息
 * 包括总体评分、驾驶技术、服务态度、车辆整洁等分项评分
 * 该评价会影响司机的平均评分、派单优先级和服务权限
 */
@Data
@Entity
@Table(name = "user_evaluations")
public class UserEvaluation {

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
     * 评价用户（乘客）ID
     */
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /**
     * 被评价司机ID
     */
    @Column(name = "driver_id", nullable = false)
    private Long driverId;

    /**
     * 总体评分：1-5星
     * 这是用户对本次服务的整体评价
     * 该评分直接影响司机的平均评分
     */
    @Column(name = "overall_rating", nullable = false)
    private Integer overallRating;

    /**
     * 驾驶技术评分：1-5星
     * 用户对司机驾驶技术的评价
     */
    @Column(name = "driving_skill")
    private Integer drivingSkill;

    /**
     * 服务态度评分：1-5星
     * 用户对司机服务态度的评价
     */
    @Column(name = "service_attitude")
    private Integer serviceAttitude;

    /**
     * 车辆整洁评分：1-5星
     * 用户对车辆整洁程度的评价
     */
    @Column(name = "car_cleanliness")
    private Integer carCleanliness;

    /**
     * 评价内容
     * 用户可以填写详细的评价文字
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
     * 评价用户（乘客）信息 - 非持久化字段，用于页面展示
     */
    @Transient
    private User user;

    /**
     * 被评价司机信息 - 非持久化字段，用于页面展示
     */
    @Transient
    private Driver driver;

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
     * 获取分项评分平均值
     * 如果有分项评分，计算平均分值
     * @return 分项评分平均值
     */
    public Double getAvgItemRating() {
        int count = 0;
        int total = 0;
        
        if (drivingSkill != null) {
            count++;
            total += drivingSkill;
        }
        if (serviceAttitude != null) {
            count++;
            total += serviceAttitude;
        }
        if (carCleanliness != null) {
            count++;
            total += carCleanliness;
        }
        
        if (count == 0) {
            return null;
        }
        return (double) total / count;
    }
}

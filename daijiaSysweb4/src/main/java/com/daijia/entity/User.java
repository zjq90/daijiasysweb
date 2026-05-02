package com.daijia.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

/**
 * 用户实体类
 * 存储乘客用户的基本信息，包括用户名、密码、真实姓名、联系方式等
 * 用户状态和评分信息也存储在此实体中
 */
@Data
@Entity
@Table(name = "users")
public class User {

    /**
     * 用户唯一标识ID，主键，自增
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户名，登录账号，唯一
     */
    @Column(name = "username", unique = true, nullable = false, length = 50)
    private String username;

    /**
     * 密码，加密存储
     */
    @Column(name = "password", nullable = false, length = 255)
    private String password;

    /**
     * 真实姓名
     */
    @Column(name = "real_name", nullable = false, length = 50)
    private String realName;

    /**
     * 手机号码，唯一
     */
    @Column(name = "phone", unique = true, nullable = false, length = 20)
    private String phone;

    /**
     * 电子邮箱，可选
     */
    @Column(name = "email", length = 100)
    private String email;

    /**
     * 用户状态：1-正常，0-禁用
     */
    @Column(name = "status", nullable = false)
    private Integer status = 1;

    /**
     * 用户平均评分，范围0.00-5.00
     * 该评分影响用户的派单优先级和服务权限
     */
    @Column(name = "avg_rating", precision = 3, scale = 2)
    private BigDecimal avgRating = new BigDecimal("5.00");

    /**
     * 用户被评价的总次数
     */
    @Column(name = "total_ratings")
    private Integer totalRatings = 0;

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
     * 更新用户评分
     * 当有新的评价时，重新计算平均评分
     * @param newRating 新的评分值（1-5）
     */
    public void updateRating(Integer newRating) {
        if (newRating == null || newRating < 1 || newRating > 5) {
            return;
        }
        
        // 计算新的平均分：(旧平均分 * 旧次数 + 新评分) / (旧次数 + 1)
        BigDecimal oldTotal = this.avgRating.multiply(new BigDecimal(this.totalRatings));
        this.totalRatings++;
        this.avgRating = oldTotal.add(new BigDecimal(newRating))
                                 .divide(new BigDecimal(this.totalRatings), 2, RoundingMode.HALF_UP);
        this.updatedTime = LocalDateTime.now();
    }
}

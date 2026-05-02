package com.daijia.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

/**
 * 司机实体类
 * 存储司机的基本信息，包括驾驶证信息、车辆信息、评分信息等
 * 司机的派单优先级和服务权限直接受评分影响
 */
@Data
@Entity
@Table(name = "drivers")
public class Driver {

    /**
     * 司机唯一标识ID，主键，自增
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
     * 身份证号，唯一
     */
    @Column(name = "id_card", unique = true, nullable = false, length = 18)
    private String idCard;

    /**
     * 驾驶证号
     */
    @Column(name = "license_number", nullable = false, length = 20)
    private String licenseNumber;

    /**
     * 车型
     */
    @Column(name = "car_model", length = 50)
    private String carModel;

    /**
     * 车牌号
     */
    @Column(name = "car_plate", length = 20)
    private String carPlate;

    /**
     * 司机状态：1-正常，0-禁用，2-休息
     */
    @Column(name = "status", nullable = false)
    private Integer status = 1;

    /**
     * 司机平均评分，范围0.00-5.00
     * 该评分直接影响派单优先级和服务权限
     */
    @Column(name = "avg_rating", precision = 3, scale = 2)
    private BigDecimal avgRating = new BigDecimal("5.00");

    /**
     * 司机被评价的总次数
     */
    @Column(name = "total_ratings")
    private Integer totalRatings = 0;

    /**
     * 派单优先级：1-10，越高越优先
     * 该值根据司机评分动态调整：
     * - 评分 >= 4.8: 优先级 8-10
     * - 评分 4.5-4.7: 优先级 6-7
     * - 评分 4.0-4.4: 优先级 4-5
     * - 评分 < 4.0: 优先级 1-3
     */
    @Column(name = "dispatch_priority")
    private Integer dispatchPriority = 5;

    /**
     * 服务权限：1-全部服务，2-限制服务，3-暂停服务
     * 该值根据司机评分动态调整：
     * - 评分 >= 4.5: 全部服务
     * - 评分 4.0-4.4: 限制服务（不能接长途单、高端单）
     * - 评分 < 4.0: 暂停服务
     */
    @Column(name = "service_permission")
    private Integer servicePermission = 1;

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
     * 更新司机评分并自动调整派单优先级和服务权限
     * 当有新的评价时，重新计算平均评分
     * 并根据新的评分调整派单优先级和服务权限
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
        
        // 根据新的评分调整派单优先级和服务权限
        adjustPriorityAndPermission();
        
        this.updatedTime = LocalDateTime.now();
    }

    /**
     * 根据当前评分调整派单优先级和服务权限
     * 评分越高，优先级越高，权限越大
     */
    public void adjustPriorityAndPermission() {
        if (this.avgRating == null) {
            return;
        }
        
        // 调整派单优先级
        if (this.avgRating.compareTo(new BigDecimal("4.8")) >= 0) {
            this.dispatchPriority = 10;
        } else if (this.avgRating.compareTo(new BigDecimal("4.5")) >= 0) {
            this.dispatchPriority = 7;
        } else if (this.avgRating.compareTo(new BigDecimal("4.0")) >= 0) {
            this.dispatchPriority = 5;
        } else {
            this.dispatchPriority = 2;
        }
        
        // 调整服务权限
        if (this.avgRating.compareTo(new BigDecimal("4.5")) >= 0) {
            this.servicePermission = 1; // 全部服务
        } else if (this.avgRating.compareTo(new BigDecimal("4.0")) >= 0) {
            this.servicePermission = 2; // 限制服务
        } else {
            this.servicePermission = 3; // 暂停服务
        }
    }
}

package com.daijia.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单实体类
 * 存储代驾订单的完整信息，包括订单状态、行程信息、支付信息等
 * 订单是连接用户、司机、评价的核心实体
 */
@Data
@Entity
@Table(name = "orders")
public class Order {

    /**
     * 订单唯一标识ID，主键，自增
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 订单号，业务唯一标识
     * 格式：DJ + 日期时间 + 序号
     */
    @Column(name = "order_no", unique = true, nullable = false, length = 50)
    private String orderNo;

    /**
     * 用户（乘客）ID
     */
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /**
     * 司机ID
     * 订单创建时可能为空，司机接单后赋值
     */
    @Column(name = "driver_id")
    private Long driverId;

    /**
     * 起点地址
     */
    @Column(name = "start_address", nullable = false, length = 255)
    private String startAddress;

    /**
     * 终点地址
     */
    @Column(name = "end_address", nullable = false, length = 255)
    private String endAddress;

    /**
     * 行驶距离（公里）
     * 行程结束后记录实际行驶距离
     */
    @Column(name = "distance", precision = 10, scale = 2)
    private BigDecimal distance;

    /**
     * 行驶时长（分钟）
     * 行程结束后记录实际行驶时长
     */
    @Column(name = "duration")
    private Integer duration;

    /**
     * 等待时间（分钟）
     * 司机到达后等待用户的时间
     */
    @Column(name = "wait_time")
    private Integer waitTime = 0;

    /**
     * 订单总金额
     * 根据计费规则计算出的总费用
     */
    @Column(name = "total_amount", precision = 10, scale = 2)
    private BigDecimal totalAmount;

    /**
     * 支付方式
     * WECHAT - 微信支付
     * ALIPAY - 支付宝
     * UNIONPAY - 银联支付
     */
    @Column(name = "payment_type", length = 20)
    private String paymentType;

    /**
     * 支付模式
     * PREPAY - 预付模式
     * POSTPAY - 后付模式
     */
    @Column(name = "pay_mode", length = 20)
    private String payMode;

    /**
     * 订单状态
     * 0 - 待接单
     * 1 - 已接单
     * 2 - 进行中
     * 3 - 已完成
     * 4 - 已取消
     * 5 - 已支付
     */
    @Column(name = "status", nullable = false)
    private Integer status = 0;

    /**
     * 用户是否已评价
     */
    @Column(name = "user_evaluated")
    private Boolean userEvaluated = false;

    /**
     * 司机是否已评价
     */
    @Column(name = "driver_evaluated")
    private Boolean driverEvaluated = false;

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
     * 用户信息 - 非持久化字段，用于页面展示
     */
    @Transient
    private User user;

    /**
     * 司机信息 - 非持久化字段，用于页面展示
     */
    @Transient
    private Driver driver;

    /**
     * 获取订单状态描述
     * 
     * @return 订单状态中文描述
     */
    public String getStatusDesc() {
        if (status == null) {
            return "未知";
        }
        switch (status) {
            case 0:
                return "待接单";
            case 1:
                return "已接单";
            case 2:
                return "进行中";
            case 3:
                return "已完成";
            case 4:
                return "已取消";
            case 5:
                return "已支付";
            default:
                return "未知";
        }
    }

    /**
     * 获取订单状态徽章样式类
     * 用于前端页面显示状态标签的颜色
     * 
     * @return CSS样式类名
     */
    public String getStatusBadgeClass() {
        if (status == null) {
            return "bg-secondary";
        }
        switch (status) {
            case 0:
                return "bg-warning"; // 待接单 - 黄色
            case 1:
                return "bg-info";    // 已接单 - 蓝色
            case 2:
                return "bg-primary"; // 进行中 - 深蓝色
            case 3:
                return "bg-success"; // 已完成 - 绿色
            case 4:
                return "bg-danger";  // 已取消 - 红色
            case 5:
                return "bg-success"; // 已支付 - 绿色
            default:
                return "bg-secondary";
        }
    }

    /**
     * 获取支付方式描述
     * 
     * @return 支付方式中文描述
     */
    public String getPaymentTypeDesc() {
        if (paymentType == null) {
            return "未选择";
        }
        switch (paymentType) {
            case "WECHAT":
                return "微信支付";
            case "ALIPAY":
                return "支付宝";
            case "UNIONPAY":
                return "银联支付";
            default:
                return paymentType;
        }
    }

    /**
     * 获取支付模式描述
     * 
     * @return 支付模式中文描述
     */
    public String getPayModeDesc() {
        if (payMode == null) {
            return "未选择";
        }
        switch (payMode) {
            case "PREPAY":
                return "预付模式";
            case "POSTPAY":
                return "后付模式";
            default:
                return payMode;
        }
    }

    /**
     * 判断订单是否可以取消
     * 
     * @return 是否可以取消
     */
    public Boolean canCancel() {
        return status != null && (status == 0 || status == 1);
    }

    /**
     * 判断订单是否可以评价
     * 
     * @return 是否可以评价
     */
    public Boolean canEvaluate() {
        return status != null && (status == 3 || status == 5);
    }

    /**
     * 生成订单号
     * 格式：DJ + yyyyMMddHHmmss + 3位随机数
     * 
     * @return 生成的订单号
     */
    public static String generateOrderNo() {
        java.time.format.DateTimeFormatter formatter = 
            java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String timestamp = LocalDateTime.now().format(formatter);
        int random = (int) (Math.random() * 1000);
        return "DJ" + timestamp + String.format("%03d", random);
    }
}

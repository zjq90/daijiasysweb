package com.daijia.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 支付方式配置实体类
 * 存储系统的支付方式配置信息，包括微信、支付宝、银联等
 * 支持预付与后付模式，后台配置好参数即可使用
 */
@Data
@Entity
@Table(name = "payment_configs")
public class PaymentConfig {

    /**
     * 配置唯一标识ID，主键，自增
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 支付方式名称
     * 如：微信支付、支付宝、银联支付等
     */
    @Column(name = "payment_name", nullable = false, length = 50)
    private String paymentName;

    /**
     * 支付类型
     * WECHAT - 微信支付
     * ALIPAY - 支付宝
     * UNIONPAY - 银联支付
     */
    @Column(name = "payment_type", nullable = false, length = 20)
    private String paymentType;

    /**
     * 支付模式
     * PREPAY - 预付模式（下单时支付）
     * POSTPAY - 后付模式（行程结束后支付）
     * BOTH - 支持两种模式
     */
    @Column(name = "pay_mode", nullable = false, length = 20)
    private String payMode;

    /**
     * 应用ID
     * 微信：AppID
     * 支付宝：AppID
     * 银联：应用ID
     */
    @Column(name = "app_id", length = 100)
    private String appId;

    /**
     * 应用密钥
     * 用于签名验证等安全操作
     */
    @Column(name = "app_secret", length = 255)
    private String appSecret;

    /**
     * 商户号
     * 各支付平台的商户编号
     */
    @Column(name = "merchant_id", length = 100)
    private String merchantId;

    /**
     * API密钥
     * 用于API接口调用验证
     */
    @Column(name = "api_key", length = 255)
    private String apiKey;

    /**
     * 回调通知地址
     * 支付平台异步通知的接收地址
     */
    @Column(name = "notify_url", length = 255)
    private String notifyUrl;

    /**
     * 配置状态：1-启用，0-禁用
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
     * 获取支付类型显示名称
     * 
     * @return 支付类型中文名称
     */
    public String getPaymentTypeDesc() {
        if (paymentType == null) {
            return "";
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
     * 获取支付模式显示名称
     * 
     * @return 支付模式中文名称
     */
    public String getPayModeDesc() {
        if (payMode == null) {
            return "";
        }
        switch (payMode) {
            case "PREPAY":
                return "预付模式";
            case "POSTPAY":
                return "后付模式";
            case "BOTH":
                return "预付/后付";
            default:
                return payMode;
        }
    }

    /**
     * 判断是否支持预付模式
     * 
     * @return 是否支持预付
     */
    public Boolean supportsPrepay() {
        return "PREPAY".equals(payMode) || "BOTH".equals(payMode);
    }

    /**
     * 判断是否支持后付模式
     * 
     * @return 是否支持后付
     */
    public Boolean supportsPostpay() {
        return "POSTPAY".equals(payMode) || "BOTH".equals(payMode);
    }

    /**
     * 获取支付图标样式类
     * 用于前端页面显示对应支付方式的图标
     * 
     * @return CSS样式类名
     */
    public String getIconClass() {
        if (paymentType == null) {
            return "bi-credit-card";
        }
        switch (paymentType) {
            case "WECHAT":
                return "bi-wechat";
            case "ALIPAY":
                return "bi-alipay";
            case "UNIONPAY":
                return "bi-credit-card-2-front";
            default:
                return "bi-credit-card";
        }
    }

    /**
     * 获取状态徽章样式类
     * 用于前端页面显示状态标签的颜色
     * 
     * @return CSS样式类名
     */
    public String getStatusBadgeClass() {
        if (status == null || status == 1) {
            return "bg-success";
        } else {
            return "bg-secondary";
        }
    }

    /**
     * 获取状态文本
     * 
     * @return 状态中文描述
     */
    public String getStatusText() {
        if (status == null || status == 1) {
            return "启用";
        } else {
            return "禁用";
        }
    }
}

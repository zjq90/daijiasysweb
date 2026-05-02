package com.daijia.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 司机实体类
 * 用于存储司机信息，支持实时司机分布监控
 *
 * @author daijia
 * @version 1.0.0
 */
@Entity
@Table(name = "driver")
public class Driver {

    /**
     * 司机主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 司机编号（唯一）
     */
    @Column(unique = true, nullable = false, length = 32)
    private String driverNo;

    /**
     * 司机姓名
     */
    @Column(length = 64)
    private String name;

    /**
     * 手机号码
     */
    @Column(length = 20)
    private String phone;

    /**
     * 身份证号
     */
    @Column(length = 32)
    private String idCard;

    /**
     * 驾驶证号
     */
    @Column(length = 32)
    private String licenseNo;

    /**
     * 驾驶证有效期开始
     */
    private LocalDateTime licenseStartDate;

    /**
     * 驾驶证有效期结束
     */
    private LocalDateTime licenseEndDate;

    /**
     * 当前状态
     * OFFLINE - 离线
     * ONLINE - 在线（空闲）
     * IN_SERVICE - 服务中
     * PAUSED - 暂停接单
     */
    @Column(length = 32)
    private String status;

    /**
     * 当前纬度（实时位置）
     */
    private BigDecimal currentLatitude;

    /**
     * 当前经度（实时位置）
     */
    private BigDecimal currentLongitude;

    /**
     * 当前所在服务区域ID
     */
    private Long serviceAreaId;

    /**
     * 评分（平均星级，1-5）
     */
    private BigDecimal rating;

    /**
     * 完成订单数
     */
    private Integer totalOrders;

    /**
     * 总收入
     */
    private BigDecimal totalIncome;

    /**
     * 今日收入
     */
    private BigDecimal todayIncome;

    /**
     * 今日订单数
     */
    private Integer todayOrders;

    /**
     * 上线时间
     */
    private LocalDateTime onlineTime;

    /**
     * 注册时间
     */
    private LocalDateTime registerTime;

    /**
     * 状态更新时间
     */
    private LocalDateTime statusUpdateTime;

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
    public Driver() {
        this.status = "OFFLINE";
        this.rating = new BigDecimal("5.0");
        this.totalOrders = 0;
        this.totalIncome = new BigDecimal("0.00");
        this.todayIncome = new BigDecimal("0.00");
        this.todayOrders = 0;
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

    public String getDriverNo() {
        return driverNo;
    }

    public void setDriverNo(String driverNo) {
        this.driverNo = driverNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getLicenseNo() {
        return licenseNo;
    }

    public void setLicenseNo(String licenseNo) {
        this.licenseNo = licenseNo;
    }

    public LocalDateTime getLicenseStartDate() {
        return licenseStartDate;
    }

    public void setLicenseStartDate(LocalDateTime licenseStartDate) {
        this.licenseStartDate = licenseStartDate;
    }

    public LocalDateTime getLicenseEndDate() {
        return licenseEndDate;
    }

    public void setLicenseEndDate(LocalDateTime licenseEndDate) {
        this.licenseEndDate = licenseEndDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getCurrentLatitude() {
        return currentLatitude;
    }

    public void setCurrentLatitude(BigDecimal currentLatitude) {
        this.currentLatitude = currentLatitude;
    }

    public BigDecimal getCurrentLongitude() {
        return currentLongitude;
    }

    public void setCurrentLongitude(BigDecimal currentLongitude) {
        this.currentLongitude = currentLongitude;
    }

    public Long getServiceAreaId() {
        return serviceAreaId;
    }

    public void setServiceAreaId(Long serviceAreaId) {
        this.serviceAreaId = serviceAreaId;
    }

    public BigDecimal getRating() {
        return rating;
    }

    public void setRating(BigDecimal rating) {
        this.rating = rating;
    }

    public Integer getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(Integer totalOrders) {
        this.totalOrders = totalOrders;
    }

    public BigDecimal getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(BigDecimal totalIncome) {
        this.totalIncome = totalIncome;
    }

    public BigDecimal getTodayIncome() {
        return todayIncome;
    }

    public void setTodayIncome(BigDecimal todayIncome) {
        this.todayIncome = todayIncome;
    }

    public Integer getTodayOrders() {
        return todayOrders;
    }

    public void setTodayOrders(Integer todayOrders) {
        this.todayOrders = todayOrders;
    }

    public LocalDateTime getOnlineTime() {
        return onlineTime;
    }

    public void setOnlineTime(LocalDateTime onlineTime) {
        this.onlineTime = onlineTime;
    }

    public LocalDateTime getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(LocalDateTime registerTime) {
        this.registerTime = registerTime;
    }

    public LocalDateTime getStatusUpdateTime() {
        return statusUpdateTime;
    }

    public void setStatusUpdateTime(LocalDateTime statusUpdateTime) {
        this.statusUpdateTime = statusUpdateTime;
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

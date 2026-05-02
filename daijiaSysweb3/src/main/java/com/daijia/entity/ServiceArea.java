package com.daijia.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 服务区域实体类
 * 用于管理代驾服务覆盖区域，支持热力图展示
 *
 * @author daijia
 * @version 1.0.0
 */
@Entity
@Table(name = "service_area")
public class ServiceArea {

    /**
     * 服务区域主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 区域名称
     */
    @Column(length = 128)
    private String areaName;

    /**
     * 区域编码
     */
    @Column(length = 32)
    private String areaCode;

    /**
     * 省份
     */
    @Column(length = 64)
    private String province;

    /**
     * 城市
     */
    @Column(length = 64)
    private String city;

    /**
     * 区县
     */
    @Column(length = 64)
    private String district;

    /**
     * 区域中心点纬度
     */
    private BigDecimal centerLatitude;

    /**
     * 区域中心点经度
     */
    private BigDecimal centerLongitude;

    /**
     * 区域范围（多边形坐标，JSON格式）
     * 格式: [[lat1,lng1],[lat2,lng2],...]
     */
    @Column(columnDefinition = "TEXT")
    private String boundary;

    /**
     * 区域半径（公里）
     */
    private BigDecimal radius;

    /**
     * 服务状态
     * ACTIVE - 正常服务
     * INACTIVE - 暂停服务
     */
    @Column(length = 32)
    private String status;

    /**
     * 当前在线司机数
     */
    private Integer onlineDrivers;

    /**
     * 当前订单数
     */
    private Integer currentOrders;

    /**
     * 今日订单数
     */
    private Integer todayOrders;

    /**
     * 今日订单金额
     */
    private BigDecimal todayAmount;

    /**
     * 热门程度（用于热力图，1-100）
     */
    private Integer heatLevel;

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
    public ServiceArea() {
        this.status = "ACTIVE";
        this.onlineDrivers = 0;
        this.currentOrders = 0;
        this.todayOrders = 0;
        this.todayAmount = new BigDecimal("0.00");
        this.heatLevel = 0;
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

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public BigDecimal getCenterLatitude() {
        return centerLatitude;
    }

    public void setCenterLatitude(BigDecimal centerLatitude) {
        this.centerLatitude = centerLatitude;
    }

    public BigDecimal getCenterLongitude() {
        return centerLongitude;
    }

    public void setCenterLongitude(BigDecimal centerLongitude) {
        this.centerLongitude = centerLongitude;
    }

    public String getBoundary() {
        return boundary;
    }

    public void setBoundary(String boundary) {
        this.boundary = boundary;
    }

    public BigDecimal getRadius() {
        return radius;
    }

    public void setRadius(BigDecimal radius) {
        this.radius = radius;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getOnlineDrivers() {
        return onlineDrivers;
    }

    public void setOnlineDrivers(Integer onlineDrivers) {
        this.onlineDrivers = onlineDrivers;
    }

    public Integer getCurrentOrders() {
        return currentOrders;
    }

    public void setCurrentOrders(Integer currentOrders) {
        this.currentOrders = currentOrders;
    }

    public Integer getTodayOrders() {
        return todayOrders;
    }

    public void setTodayOrders(Integer todayOrders) {
        this.todayOrders = todayOrders;
    }

    public BigDecimal getTodayAmount() {
        return todayAmount;
    }

    public void setTodayAmount(BigDecimal todayAmount) {
        this.todayAmount = todayAmount;
    }

    public Integer getHeatLevel() {
        return heatLevel;
    }

    public void setHeatLevel(Integer heatLevel) {
        this.heatLevel = heatLevel;
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

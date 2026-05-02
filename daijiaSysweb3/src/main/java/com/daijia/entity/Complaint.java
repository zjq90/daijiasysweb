package com.daijia.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 投诉实体类
 * 用于存储乘客投诉信息，支持投诉率统计
 *
 * @author daijia
 * @version 1.0.0
 */
@Entity
@Table(name = "complaint")
public class Complaint {

    /**
     * 投诉主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 投诉编号（唯一）
     */
    @Column(unique = true, nullable = false, length = 32)
    private String complaintNo;

    /**
     * 订单ID
     */
    @Column(nullable = false)
    private Long orderId;

    /**
     * 乘客ID
     */
    @Column(nullable = false)
    private Long passengerId;

    /**
     * 司机ID
     */
    private Long driverId;

    /**
     * 投诉类型
     * DRIVER_ATTITUDE - 司机态度问题
     * ROUTE_PROBLEM - 路线问题
     * SAFETY_ISSUE - 安全问题
     * PRICE_PROBLEM - 价格问题
     * SERVICE_QUALITY - 服务质量
     * OTHER - 其他
     */
    @Column(length = 64)
    private String complaintType;

    /**
     * 投诉内容
     */
    @Column(columnDefinition = "TEXT")
    private String content;

    /**
     * 投诉状态
     * PENDING - 待处理
     * PROCESSING - 处理中
     * RESOLVED - 已解决
     * REJECTED - 已驳回
     */
    @Column(length = 32)
    private String status;

    /**
     * 处理人ID
     */
    private Long handlerId;

    /**
     * 处理结果
     */
    @Column(columnDefinition = "TEXT")
    private String handleResult;

    /**
     * 处理时间
     */
    private LocalDateTime handleTime;

    /**
     * 投诉时间
     */
    private LocalDateTime complaintTime;

    /**
     * 服务区域ID
     */
    private Long serviceAreaId;

    /**
     * 是否有效投诉
     */
    private Boolean valid;

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
    public Complaint() {
        this.status = "PENDING";
        this.valid = true;
        this.complaintTime = LocalDateTime.now();
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

    public String getComplaintNo() {
        return complaintNo;
    }

    public void setComplaintNo(String complaintNo) {
        this.complaintNo = complaintNo;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(Long passengerId) {
        this.passengerId = passengerId;
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public String getComplaintType() {
        return complaintType;
    }

    public void setComplaintType(String complaintType) {
        this.complaintType = complaintType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getHandlerId() {
        return handlerId;
    }

    public void setHandlerId(Long handlerId) {
        this.handlerId = handlerId;
    }

    public String getHandleResult() {
        return handleResult;
    }

    public void setHandleResult(String handleResult) {
        this.handleResult = handleResult;
    }

    public LocalDateTime getHandleTime() {
        return handleTime;
    }

    public void setHandleTime(LocalDateTime handleTime) {
        this.handleTime = handleTime;
    }

    public LocalDateTime getComplaintTime() {
        return complaintTime;
    }

    public void setComplaintTime(LocalDateTime complaintTime) {
        this.complaintTime = complaintTime;
    }

    public Long getServiceAreaId() {
        return serviceAreaId;
    }

    public void setServiceAreaId(Long serviceAreaId) {
        this.serviceAreaId = serviceAreaId;
    }

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
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

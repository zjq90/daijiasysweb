package com.daijia.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 投诉单实体类
 * 对应数据库表：complaint
 */
@Entity
@Table(name = "complaint")
public class Complaint {

    /**
     * 投诉单ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 投诉单号
     */
    @Column(name = "complaint_no", nullable = false, length = 50)
    private String complaintNo;

    /**
     * 关联订单号
     */
    @Column(name = "order_no", length = 50)
    private String orderNo;

    /**
     * 被投诉司机ID
     */
    @Column(name = "driver_id")
    private Long driverId;

    /**
     * 被投诉司机姓名
     */
    @Column(name = "driver_name", length = 100)
    private String driverName;

    /**
     * 投诉人姓名
     */
    @Column(name = "complainant_name", length = 100)
    private String complainantName;

    /**
     * 投诉人电话
     */
    @Column(name = "complainant_phone", length = 20)
    private String complainantPhone;

    /**
     * 投诉类型：服务态度、迟到、拒单、绕路、其他
     */
    @Column(name = "type", length = 50)
    private String type;

    /**
     * 投诉内容
     */
    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    /**
     * 状态：PENDING-待处理，PROCESSING-处理中，RESOLVED-已解决，REJECTED-已驳回
     */
    @Column(name = "status", length = 20)
    private String status = "PENDING";

    /**
     * 处理备注
     */
    @Column(name = "handle_remark", columnDefinition = "TEXT")
    private String handleRemark;

    /**
     * 信用扣分
     */
    @Column(name = "credit_deduct")
    private Integer creditDeduct = 0;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private LocalDateTime createTime;

    /**
     * 处理时间
     */
    @Column(name = "handle_time")
    private LocalDateTime handleTime;

    /**
     * 持久化前自动设置创建时间
     */
    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
    }

    // Getter and Setter methods

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

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getComplainantName() {
        return complainantName;
    }

    public void setComplainantName(String complainantName) {
        this.complainantName = complainantName;
    }

    public String getComplainantPhone() {
        return complainantPhone;
    }

    public void setComplainantPhone(String complainantPhone) {
        this.complainantPhone = complainantPhone;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getHandleRemark() {
        return handleRemark;
    }

    public void setHandleRemark(String handleRemark) {
        this.handleRemark = handleRemark;
    }

    public Integer getCreditDeduct() {
        return creditDeduct;
    }

    public void setCreditDeduct(Integer creditDeduct) {
        this.creditDeduct = creditDeduct;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getHandleTime() {
        return handleTime;
    }

    public void setHandleTime(LocalDateTime handleTime) {
        this.handleTime = handleTime;
    }
}

package com.daijia.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 安全求助记录实体类
 * 
 * 对应数据库表：t_emergency_help
 * 
 * 求助类型：
 * 0: 一般求助
 * 1: 用户/乘客求助
 * 2: 司机求助
 * 3: 系统自动检测异常
 * 
 * 求助状态：
 * 0: 待处理
 * 1: 处理中
 * 2: 已处理
 * 3: 已关闭
 * 
 * @author daijia
 * @version 1.0.0
 */
@Entity
@Table(name = "t_emergency_help")
public class EmergencyHelp {

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 求助编号
     */
    @Column(name = "help_no", unique = true, nullable = false, length = 32)
    private String helpNo;

    /**
     * 关联订单ID（可为空，非订单场景也可求助）
     */
    @Column(name = "order_id")
    private Long orderId;

    /**
     * 用户ID（乘客）
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * 司机ID
     */
    @Column(name = "driver_id")
    private Long driverId;

    /**
     * 求助类型
     * 0: 一般求助, 1: 用户求助, 2: 司机求助, 3: 系统自动检测
     */
    @Column(name = "help_type")
    private Integer helpType;

    /**
     * 求助时的纬度
     */
    @Column(name = "latitude", precision = 10, scale = 7)
    private BigDecimal latitude;

    /**
     * 求助时的经度
     */
    @Column(name = "longitude", precision = 10, scale = 7)
    private BigDecimal longitude;

    /**
     * 求助时的地址描述
     */
    @Column(name = "address", length = 200)
    private String address;

    /**
     * 求助内容/描述
     */
    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    /**
     * 录音文件路径
     */
    @Column(name = "audio_path", length = 255)
    private String audioPath;

    /**
     * 紧急联系人信息（JSON格式存储）
     * 格式示例：[{"name":"张三","phone":"13800138000","relation":"配偶"},...]
     */
    @Column(name = "emergency_contacts", columnDefinition = "TEXT")
    private String emergencyContacts;

    /**
     * 求助状态
     * 0: 待处理, 1: 处理中, 2: 已处理, 3: 已关闭
     */
    @Column(name = "status")
    private Integer status;

    /**
     * 处理的管理员ID
     */
    @Column(name = "admin_id")
    private Long adminId;

    /**
     * 处理时间
     */
    @Column(name = "handle_time")
    private LocalDateTime handleTime;

    /**
     * 处理结果
     */
    @Column(name = "handle_result", columnDefinition = "TEXT")
    private String handleResult;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @Transient
    private User user;

    @Transient
    private Driver driver;

    @Transient
    private Order order;

    @Transient
    private Admin admin;

    public EmergencyHelp() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHelpNo() {
        return helpNo;
    }

    public void setHelpNo(String helpNo) {
        this.helpNo = helpNo;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public Integer getHelpType() {
        return helpType;
    }

    public void setHelpType(Integer helpType) {
        this.helpType = helpType;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAudioPath() {
        return audioPath;
    }

    public void setAudioPath(String audioPath) {
        this.audioPath = audioPath;
    }

    public String getEmergencyContacts() {
        return emergencyContacts;
    }

    public void setEmergencyContacts(String emergencyContacts) {
        this.emergencyContacts = emergencyContacts;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public LocalDateTime getHandleTime() {
        return handleTime;
    }

    public void setHandleTime(LocalDateTime handleTime) {
        this.handleTime = handleTime;
    }

    public String getHandleResult() {
        return handleResult;
    }

    public void setHandleResult(String handleResult) {
        this.handleResult = handleResult;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    @PrePersist
    public void prePersist() {
        if (this.createTime == null) {
            this.createTime = LocalDateTime.now();
        }
        if (this.updateTime == null) {
            this.updateTime = LocalDateTime.now();
        }
        if (this.status == null) {
            this.status = 0;
        }
        if (this.helpType == null) {
            this.helpType = 0;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updateTime = LocalDateTime.now();
    }
}

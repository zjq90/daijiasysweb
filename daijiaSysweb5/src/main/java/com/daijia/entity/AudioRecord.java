package com.daijia.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 录音记录实体类
 * 
 * 对应数据库表：t_audio_record
 * 
 * 用于存储安全求助过程中的录音文件信息，
 * 支持全程录音功能，便于后续调查取证
 * 
 * 状态说明：
 * 0: 已删除
 * 1: 正常
 * 
 * @author daijia
 * @version 1.0.0
 */
@Entity
@Table(name = "t_audio_record")
public class AudioRecord {

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 录音记录编号
     */
    @Column(name = "record_no", unique = true, nullable = false, length = 32)
    private String recordNo;

    /**
     * 关联订单ID（可为空）
     */
    @Column(name = "order_id")
    private Long orderId;

    /**
     * 关联安全求助记录ID（可为空）
     */
    @Column(name = "emergency_help_id")
    private Long emergencyHelpId;

    /**
     * 录音文件存储路径
     */
    @Column(name = "file_path", nullable = false, length = 255)
    private String filePath;

    /**
     * 文件大小（字节）
     */
    @Column(name = "file_size")
    private Long fileSize;

    /**
     * 录音时长（秒）
     */
    @Column(name = "duration")
    private Integer duration;

    /**
     * 录音开始时间
     */
    @Column(name = "record_start_time")
    private LocalDateTime recordStartTime;

    /**
     * 录音结束时间
     */
    @Column(name = "record_end_time")
    private LocalDateTime recordEndTime;

    /**
     * 状态
     * 0: 已删除, 1: 正常
     */
    @Column(name = "status")
    private Integer status;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private LocalDateTime createTime;

    @Transient
    private Order order;

    @Transient
    private EmergencyHelp emergencyHelp;

    public AudioRecord() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRecordNo() {
        return recordNo;
    }

    public void setRecordNo(String recordNo) {
        this.recordNo = recordNo;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getEmergencyHelpId() {
        return emergencyHelpId;
    }

    public void setEmergencyHelpId(Long emergencyHelpId) {
        this.emergencyHelpId = emergencyHelpId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public LocalDateTime getRecordStartTime() {
        return recordStartTime;
    }

    public void setRecordStartTime(LocalDateTime recordStartTime) {
        this.recordStartTime = recordStartTime;
    }

    public LocalDateTime getRecordEndTime() {
        return recordEndTime;
    }

    public void setRecordEndTime(LocalDateTime recordEndTime) {
        this.recordEndTime = recordEndTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public EmergencyHelp getEmergencyHelp() {
        return emergencyHelp;
    }

    public void setEmergencyHelp(EmergencyHelp emergencyHelp) {
        this.emergencyHelp = emergencyHelp;
    }

    @PrePersist
    public void prePersist() {
        if (this.createTime == null) {
            this.createTime = LocalDateTime.now();
        }
        if (this.status == null) {
            this.status = 1;
        }
        if (this.recordStartTime == null) {
            this.recordStartTime = LocalDateTime.now();
        }
    }
}

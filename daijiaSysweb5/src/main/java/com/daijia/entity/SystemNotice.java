package com.daijia.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 系统通知实体类
 * 
 * 对应数据库表：t_system_notice
 * 
 * 通知类型：
 * 0: 系统通知
 * 1: 订单通知
 * 2: 紧急求助通知
 * 
 * 目标类型：
 * 0: 全部用户
 * 1: 用户（乘客）
 * 2: 司机
 * 3: 管理员
 * 
 * @author daijia
 * @version 1.0.0
 */
@Entity
@Table(name = "t_system_notice")
public class SystemNotice {

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 通知标题
     */
    @Column(name = "title", nullable = false, length = 200)
    private String title;

    /**
     * 通知内容
     */
    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    /**
     * 通知类型
     * 0: 系统通知, 1: 订单通知, 2: 紧急求助通知
     */
    @Column(name = "notice_type")
    private Integer noticeType;

    /**
     * 目标类型
     * 0: 全部, 1: 用户, 2: 司机, 3: 管理员
     */
    @Column(name = "target_type")
    private Integer targetType;

    /**
     * 目标用户ID（针对特定用户的通知）
     */
    @Column(name = "target_id")
    private Long targetId;

    /**
     * 是否已读
     * 0: 未读, 1: 已读
     */
    @Column(name = "is_read")
    private Integer isRead;

    /**
     * 阅读时间
     */
    @Column(name = "read_time")
    private LocalDateTime readTime;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private LocalDateTime createTime;

    public SystemNotice() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(Integer noticeType) {
        this.noticeType = noticeType;
    }

    public Integer getTargetType() {
        return targetType;
    }

    public void setTargetType(Integer targetType) {
        this.targetType = targetType;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public Integer getIsRead() {
        return isRead;
    }

    public void setIsRead(Integer isRead) {
        this.isRead = isRead;
    }

    public LocalDateTime getReadTime() {
        return readTime;
    }

    public void setReadTime(LocalDateTime readTime) {
        this.readTime = readTime;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    @PrePersist
    public void prePersist() {
        if (this.createTime == null) {
            this.createTime = LocalDateTime.now();
        }
        if (this.isRead == null) {
            this.isRead = 0;
        }
        if (this.noticeType == null) {
            this.noticeType = 0;
        }
        if (this.targetType == null) {
            this.targetType = 0;
        }
    }
}

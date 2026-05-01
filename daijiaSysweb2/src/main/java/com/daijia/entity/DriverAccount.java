package com.daijia.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 代驾账户实体类
 * 对应数据库表：driver_account
 */
@Entity
@Table(name = "driver_account")
public class DriverAccount {

    /**
     * 账户ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 司机姓名
     */
    @Column(name = "driver_name", nullable = false, length = 100)
    private String driverName;

    /**
     * 手机号
     */
    @Column(name = "phone", nullable = false, length = 20)
    private String phone;

    /**
     * 身份证号
     */
    @Column(name = "id_card", length = 18)
    private String idCard;

    /**
     * 账户余额
     */
    @Column(name = "balance", precision = 10, scale = 2)
    private BigDecimal balance = BigDecimal.ZERO;

    /**
     * 冻结余额
     */
    @Column(name = "frozen_balance", precision = 10, scale = 2)
    private BigDecimal frozenBalance = BigDecimal.ZERO;

    /**
     * 累计收入
     */
    @Column(name = "total_income", precision = 10, scale = 2)
    private BigDecimal totalIncome = BigDecimal.ZERO;

    /**
     * 累计提现
     */
    @Column(name = "total_withdraw", precision = 10, scale = 2)
    private BigDecimal totalWithdraw = BigDecimal.ZERO;

    /**
     * 账户状态：ACTIVE-正常，FROZEN-冻结，CANCELLED-注销
     */
    @Column(name = "status", length = 20)
    private String status = "ACTIVE";

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

    /**
     * 持久化前自动设置创建时间和更新时间
     */
    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
    }

    /**
     * 更新时自动设置更新时间
     */
    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }

    // Getter and Setter methods

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
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

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getFrozenBalance() {
        return frozenBalance;
    }

    public void setFrozenBalance(BigDecimal frozenBalance) {
        this.frozenBalance = frozenBalance;
    }

    public BigDecimal getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(BigDecimal totalIncome) {
        this.totalIncome = totalIncome;
    }

    public BigDecimal getTotalWithdraw() {
        return totalWithdraw;
    }

    public void setTotalWithdraw(BigDecimal totalWithdraw) {
        this.totalWithdraw = totalWithdraw;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

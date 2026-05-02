package com.daijia.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "orders")
public class Order extends BaseEntity {
    
    @Column(unique = true, nullable = false, length = 32)
    private String orderNo;
    
    @Column(nullable = false)
    private Long clientId;
    
    private Long driverId;
    
    @Column(length = 20)
    private String orderType;
    
    @Column(length = 20)
    private String dispatchType;
    
    private LocalDateTime appointTime;
    
    @Column(nullable = false, length = 255)
    private String startAddress;
    
    private Double startLat;
    
    private Double startLng;
    
    @Column(nullable = false, length = 255)
    private String endAddress;
    
    private Double endLat;
    
    private Double endLng;
    
    @Column(length = 50)
    private String vehicleType;
    
    @Column(length = 50)
    private String serviceType;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal estimateDistance;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal estimateFee;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal actualDistance;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal actualFee;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal discountFee;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal payFee;
    
    @Column(length = 20)
    private String payStatus;
    
    @Column(length = 20)
    private String payMethod;
    
    private LocalDateTime payTime;
    
    private LocalDateTime acceptTime;
    
    private LocalDateTime startServiceTime;
    
    private LocalDateTime endServiceTime;
    
    private LocalDateTime cancelTime;
    
    private Long cancelUserId;
    
    @Column(length = 20)
    private String cancelReason;
    
    @Column(nullable = false, length = 20)
    private String status;
    
    @Column(length = 500)
    private String remark;
    
    private Long promotionId;
    
    @Transient
    private Client client;
    
    @Transient
    private Driver driver;
}

package com.daijia.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "order_push")
public class OrderPush extends BaseEntity {
    
    @Column(nullable = false)
    private Long orderId;
    
    @Column(nullable = false)
    private Long driverId;
    
    @Column(nullable = false, length = 20)
    private String pushType;
    
    @Column(nullable = false, length = 20)
    private String status;
    
    private LocalDateTime pushTime;
    
    private LocalDateTime readTime;
    
    private LocalDateTime handleTime;
    
    @Column(length = 500)
    private String remark;
}

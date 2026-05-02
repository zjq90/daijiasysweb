package com.daijia.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "promotion")
public class Promotion extends BaseEntity {
    
    @Column(nullable = false, length = 100)
    private String title;
    
    @Column(length = 500)
    private String description;
    
    @Column(nullable = false, length = 20)
    private String type;
    
    @Column(nullable = false, length = 20)
    private String targetUser;
    
    private LocalDateTime startTime;
    
    private LocalDateTime endTime;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal discountAmount;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal discountRate;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal minOrderAmount;
    
    @Column(nullable = false, columnDefinition = "int default 0")
    private Integer totalCount = 0;
    
    @Column(nullable = false, columnDefinition = "int default 0")
    private Integer usedCount = 0;
    
    @Column(nullable = false, columnDefinition = "int default 0")
    private Integer perUserLimit = 0;
    
    @Column(length = 20)
    private String status;
    
    @Column(length = 255)
    private String image;
    
    @Column(length = 500)
    private String rules;
}

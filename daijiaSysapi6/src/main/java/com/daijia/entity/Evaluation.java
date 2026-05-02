package com.daijia.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "evaluation")
public class Evaluation extends BaseEntity {
    
    @Column(nullable = false, unique = true)
    private Long orderId;
    
    @Column(nullable = false)
    private Long fromUserId;
    
    @Column(nullable = false)
    private Long toUserId;
    
    @Column(nullable = false, length = 20)
    private String userType;
    
    @Column(nullable = false, precision = 2, scale = 1)
    private BigDecimal rating;
    
    @Column(length = 500)
    private String content;
    
    private Integer isDrunk;
    
    private Integer isChangedDestination;
    
    private BigDecimal drivingSkillRating;
    
    private BigDecimal serviceAttitudeRating;
    
    @Column(length = 500)
    private String tags;
    
    @Column(length = 20)
    private String status;
}

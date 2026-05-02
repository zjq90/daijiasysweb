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
@Table(name = "emergency_help")
public class EmergencyHelp extends BaseEntity {
    
    @Column(nullable = false)
    private Long userId;
    
    @Column(nullable = false, length = 20)
    private String userType;
    
    private Long orderId;
    
    @Column(length = 255)
    private String location;
    
    private Double lat;
    
    private Double lng;
    
    @Column(length = 500)
    private String tripInfo;
    
    @Column(length = 20)
    private String recordingStatus;
    
    @Column(length = 255)
    private String recordingUrl;
    
    @Column(nullable = false, length = 20)
    private String status;
    
    private Long handlerId;
    
    @Column(length = 500)
    private String handleResult;
    
    private LocalDateTime handleTime;
}

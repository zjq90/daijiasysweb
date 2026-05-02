package com.daijia.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "driver_setting")
public class DriverSetting extends BaseEntity {
    
    @Column(nullable = false, unique = true)
    private Long driverId;
    
    @Column(nullable = false, columnDefinition = "int default 10")
    private Integer dailyOrderLimit = 10;
    
    @Column(nullable = false, columnDefinition = "int default 4")
    private Integer continuousWorkHours = 4;
    
    @Column(nullable = false, columnDefinition = "int default 30")
    private Integer restReminderMinutes = 30;
    
    @Column(nullable = false, columnDefinition = "int default 1")
    private Integer enableRestReminder = 1;
    
    @Column(nullable = false, columnDefinition = "int default 1")
    private Integer enableOrderPush = 1;
    
    @Column(length = 500)
    private String serviceArea;
    
    private Double maxDistance;
    
    @Column(length = 255)
    private String vehicleTypes;
    
    @Column(length = 255)
    private String serviceTypes;
}

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
@Table(name = "complaint")
public class Complaint extends BaseEntity {
    
    @Column(nullable = false)
    private Long orderId;
    
    @Column(nullable = false)
    private Long complainantId;
    
    @Column(nullable = false)
    private Long respondentId;
    
    @Column(nullable = false, length = 20)
    private String complainantType;
    
    @Column(length = 100)
    private String type;
    
    @Column(nullable = false, length = 500)
    private String content;
    
    @Column(length = 500)
    private String images;
    
    @Column(nullable = false, length = 20)
    private String status;
    
    private Long handlerId;
    
    @Column(length = 500)
    private String handleResult;
    
    private LocalDateTime handleTime;
}

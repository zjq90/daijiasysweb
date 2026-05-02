package com.daijia.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "client")
public class Client extends BaseEntity {
    
    @Column(unique = true, nullable = false, length = 20)
    private String phone;
    
    @Column(length = 64)
    private String password;
    
    @Column(length = 50)
    private String nickname;
    
    @Column(length = 255)
    private String avatar;
    
    @Column(length = 20)
    private String realName;
    
    @Column(length = 18)
    private String idCard;
    
    @Column(nullable = false, columnDefinition = "int default 0")
    private Integer isVerified = 0;
    
    @Column(nullable = false, precision = 3, scale = 1, columnDefinition = "decimal(3,1) default 5.0")
    private BigDecimal creditRating = new BigDecimal("5.0");
    
    @Column(nullable = false, columnDefinition = "int default 0")
    private Integer orderCount = 0;
    
    @Column(length = 50)
    private String loginToken;
    
    @Column(nullable = false, columnDefinition = "int default 1")
    private Integer status = 1;
}

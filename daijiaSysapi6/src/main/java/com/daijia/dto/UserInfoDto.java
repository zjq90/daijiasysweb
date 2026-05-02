package com.daijia.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(description = "用户信息")
public class UserInfoDto {
    
    @ApiModelProperty(value = "用户ID")
    private Long id;
    
    @ApiModelProperty(value = "手机号")
    private String phone;
    
    @ApiModelProperty(value = "昵称")
    private String nickname;
    
    @ApiModelProperty(value = "头像")
    private String avatar;
    
    @ApiModelProperty(value = "真实姓名")
    private String realName;
    
    @ApiModelProperty(value = "是否实名认证")
    private Integer isVerified;
    
    @ApiModelProperty(value = "信用等级")
    private BigDecimal creditRating;
    
    @ApiModelProperty(value = "订单数")
    private Integer orderCount;
    
    @ApiModelProperty(value = "在线状态：0-离线，1-在线，2-忙碌")
    private Integer onlineStatus;
}

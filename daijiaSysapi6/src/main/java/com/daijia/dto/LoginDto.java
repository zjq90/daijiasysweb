package com.daijia.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@ApiModel(description = "登录请求")
public class LoginDto {
    
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    @ApiModelProperty(value = "手机号", required = true, example = "13800138000")
    private String phone;
    
    @ApiModelProperty(value = "验证码(验证码登录时使用", example = "123456")
    private String verifyCode;
    
    @ApiModelProperty(value = "密码(MD5加密，密码登录时使用)", example = "e10adc3949ba59abbe56e057f20f883e")
    private String password;
    
    @NotBlank(message = "登录类型不能为空")
    @ApiModelProperty(value = "登录类型：VERIFY_CODE-验证码登录，PASSWORD-密码登录", required = true, example = "VERIFY_CODE")
    private String loginType;
}

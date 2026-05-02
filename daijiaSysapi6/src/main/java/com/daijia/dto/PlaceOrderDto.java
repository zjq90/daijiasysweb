package com.daijia.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@ApiModel(description = "下单请求")
public class PlaceOrderDto {
    
    @NotBlank(message = "订单类型不能为空")
    @ApiModelProperty(value = "订单类型：IMMEDIATE-立即代驾，APPOINTMENT-预约代驾", required = true, example = "IMMEDIATE")
    private String orderType;
    
    @ApiModelProperty(value = "预约时间(预约代驾时必填)", example = "2026-05-03T10:00:00")
    private LocalDateTime appointTime;
    
    @NotBlank(message = "起点地址不能为空")
    @ApiModelProperty(value = "起点地址", required = true, example = "北京市朝阳区xxx")
    private String startAddress;
    
    @ApiModelProperty(value = "起点纬度", example = "39.9042")
    private Double startLat;
    
    @ApiModelProperty(value = "起点经度", example = "116.4074")
    private Double startLng;
    
    @NotBlank(message = "终点地址不能为空")
    @ApiModelProperty(value = "终点地址", required = true, example = "北京市海淀区xxx")
    private String endAddress;
    
    @ApiModelProperty(value = "终点纬度", example = "39.9542")
    private Double endLat;
    
    @ApiModelProperty(value = "终点经度", example = "116.3574")
    private Double endLng;
    
    @ApiModelProperty(value = "车型", example = "经济型")
    private String vehicleType;
    
    @ApiModelProperty(value = "服务类型", example = "普通代驾")
    private String serviceType;
    
    @ApiModelProperty(value = "派单类型：ASSIGN-派单，ROBBERY-抢单", example = "ROBBERY")
    private String dispatchType;
    
    @ApiModelProperty(value = "营销活动ID")
    private Long promotionId;
    
    @ApiModelProperty(value = "备注", example = "请准时到达")
    private String remark;
}

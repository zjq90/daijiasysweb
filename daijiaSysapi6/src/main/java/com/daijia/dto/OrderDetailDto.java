package com.daijia.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@ApiModel(description = "订单详情响应")
public class OrderDetailDto {
    
    @ApiModelProperty(value = "订单ID")
    private Long id;
    
    @ApiModelProperty(value = "订单号")
    private String orderNo;
    
    @ApiModelProperty(value = "订单类型：IMMEDIATE-立即代驾，APPOINTMENT-预约代驾")
    private String orderType;
    
    @ApiModelProperty(value = "派单类型：ASSIGN-派单，ROBBERY-抢单")
    private String dispatchType;
    
    @ApiModelProperty(value = "预约时间")
    private LocalDateTime appointTime;
    
    @ApiModelProperty(value = "起点地址")
    private String startAddress;
    
    @ApiModelProperty(value = "终点地址")
    private String endAddress;
    
    @ApiModelProperty(value = "车型")
    private String vehicleType;
    
    @ApiModelProperty(value = "服务类型")
    private String serviceType;
    
    @ApiModelProperty(value = "预估距离(公里)")
    private BigDecimal estimateDistance;
    
    @ApiModelProperty(value = "预估费用")
    private BigDecimal estimateFee;
    
    @ApiModelProperty(value = "实际距离(公里)")
    private BigDecimal actualDistance;
    
    @ApiModelProperty(value = "实际费用")
    private BigDecimal actualFee;
    
    @ApiModelProperty(value = "优惠金额")
    private BigDecimal discountFee;
    
    @ApiModelProperty(value = "支付金额")
    private BigDecimal payFee;
    
    @ApiModelProperty(value = "订单状态")
    private String status;
    
    @ApiModelProperty(value = "接单时间")
    private LocalDateTime acceptTime;
    
    @ApiModelProperty(value = "开始服务时间")
    private LocalDateTime startServiceTime;
    
    @ApiModelProperty(value = "结束服务时间")
    private LocalDateTime endServiceTime;
    
    @ApiModelProperty(value = "客户信息")
    private UserInfoDto clientInfo;
    
    @ApiModelProperty(value = "司机信息")
    private UserInfoDto driverInfo;
}

package com.daijia.controller.driver;

import com.daijia.common.Result;
import com.daijia.dto.OrderDetailDto;
import com.daijia.entity.Driver;
import com.daijia.entity.OrderPush;
import com.daijia.service.DriverService;
import com.daijia.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Api(tags = "司机端-订单管理")
@RestController
@RequestMapping("/api/driver/order")
@RequiredArgsConstructor
public class DriverOrderController {
    
    private final DriverService driverService;
    private final OrderService orderService;
    
    @ApiOperation("获取待接单列表(抢单池)")
    @GetMapping("/pending")
    public Result<List<OrderDetailDto>> getPendingOrders(@RequestHeader("token") String token) {
        driverService.getDriverByToken(token);
        return Result.success(orderService.getPendingRobberyOrders());
    }
    
    @ApiOperation("获取我的待处理订单推送")
    @GetMapping("/pushes")
    public Result<List<OrderPush>> getPendingPushes(@RequestHeader("token") String token) {
        Driver driver = driverService.getDriverByToken(token);
        return Result.success(orderService.getDriverPendingPushes(driver.getId()));
    }
    
    @ApiOperation("接单")
    @PostMapping("/accept")
    public Result<OrderDetailDto> acceptOrder(
            @RequestHeader("token") String token,
            @ApiParam("订单ID") @RequestParam Long orderId) {
        Driver driver = driverService.getDriverByToken(token);
        orderService.acceptOrder(driver.getId(), orderId);
        return Result.success(orderService.getOrderDetail(orderId));
    }
    
    @ApiOperation("忽略订单")
    @PostMapping("/ignore")
    public Result<Void> ignoreOrder(
            @RequestHeader("token") String token,
            @ApiParam("订单ID") @RequestParam Long orderId) {
        Driver driver = driverService.getDriverByToken(token);
        orderService.ignoreOrder(driver.getId(), orderId);
        return Result.success();
    }
    
    @ApiOperation("开始服务")
    @PostMapping("/start-service")
    public Result<OrderDetailDto> startService(
            @RequestHeader("token") String token,
            @ApiParam("订单ID") @RequestParam Long orderId) {
        Driver driver = driverService.getDriverByToken(token);
        orderService.startService(driver.getId(), orderId);
        return Result.success(orderService.getOrderDetail(orderId));
    }
    
    @ApiOperation("结束服务")
    @PostMapping("/end-service")
    public Result<OrderDetailDto> endService(
            @RequestHeader("token") String token,
            @ApiParam("订单ID") @RequestParam Long orderId,
            @ApiParam("实际距离(公里)") @RequestParam(required = false) BigDecimal actualDistance,
            @ApiParam("实际费用") @RequestParam(required = false) BigDecimal actualFee) {
        Driver driver = driverService.getDriverByToken(token);
        orderService.endService(driver.getId(), orderId, actualDistance, actualFee);
        return Result.success(orderService.getOrderDetail(orderId));
    }
    
    @ApiOperation("获取订单详情")
    @GetMapping("/detail/{orderId}")
    public Result<OrderDetailDto> getOrderDetail(
            @RequestHeader("token") String token,
            @PathVariable Long orderId) {
        driverService.getDriverByToken(token);
        return Result.success(orderService.getOrderDetail(orderId));
    }
    
    @ApiOperation("获取我的订单列表")
    @GetMapping("/list")
    public Result<List<OrderDetailDto>> getOrderList(
            @RequestHeader("token") String token,
            @ApiParam("订单状态") @RequestParam(required = false) String status) {
        Driver driver = driverService.getDriverByToken(token);
        return Result.success(orderService.getDriverOrders(driver.getId(), status));
    }
    
    @ApiOperation("取消订单")
    @PostMapping("/cancel")
    public Result<OrderDetailDto> cancelOrder(
            @RequestHeader("token") String token,
            @ApiParam("订单ID") @RequestParam Long orderId,
            @ApiParam("取消原因") @RequestParam(required = false) String reason) {
        Driver driver = driverService.getDriverByToken(token);
        orderService.cancelOrder(driver.getId(), "DRIVER", orderId, reason);
        return Result.success(orderService.getOrderDetail(orderId));
    }
}

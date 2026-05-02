package com.daijia.controller.client;

import com.daijia.common.Result;
import com.daijia.dto.OrderDetailDto;
import com.daijia.dto.PlaceOrderDto;
import com.daijia.entity.Client;
import com.daijia.entity.Order;
import com.daijia.service.ClientService;
import com.daijia.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "客户端-订单管理")
@RestController
@RequestMapping("/api/client/order")
@RequiredArgsConstructor
public class ClientOrderController {
    
    private final ClientService clientService;
    private final OrderService orderService;
    
    @ApiOperation("下单")
    @PostMapping("/place")
    public Result<OrderDetailDto> placeOrder(
            @RequestHeader("token") String token,
            @Valid @RequestBody PlaceOrderDto dto) {
        Client client = clientService.getClientByToken(token);
        Order order = orderService.createOrder(client.getId(), dto);
        return Result.success(orderService.getOrderDetail(order.getId()));
    }
    
    @ApiOperation("获取订单详情")
    @GetMapping("/detail/{orderId}")
    public Result<OrderDetailDto> getOrderDetail(
            @RequestHeader("token") String token,
            @PathVariable Long orderId) {
        clientService.getClientByToken(token);
        return Result.success(orderService.getOrderDetail(orderId));
    }
    
    @ApiOperation("获取我的订单列表")
    @GetMapping("/list")
    public Result<List<OrderDetailDto>> getOrderList(
            @RequestHeader("token") String token,
            @ApiParam("订单状态") @RequestParam(required = false) String status) {
        Client client = clientService.getClientByToken(token);
        return Result.success(orderService.getClientOrders(client.getId(), status));
    }
    
    @ApiOperation("取消订单")
    @PostMapping("/cancel")
    public Result<OrderDetailDto> cancelOrder(
            @RequestHeader("token") String token,
            @ApiParam("订单ID") @RequestParam Long orderId,
            @ApiParam("取消原因") @RequestParam(required = false) String reason) {
        Client client = clientService.getClientByToken(token);
        orderService.cancelOrder(client.getId(), "CLIENT", orderId, reason);
        return Result.success(orderService.getOrderDetail(orderId));
    }
}

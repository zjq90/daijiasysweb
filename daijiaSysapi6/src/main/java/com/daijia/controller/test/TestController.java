package com.daijia.controller.test;

import com.daijia.common.Result;
import com.daijia.dto.LoginDto;
import com.daijia.dto.OrderDetailDto;
import com.daijia.dto.PlaceOrderDto;
import com.daijia.entity.*;
import com.daijia.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "测试接口-功能测试")
@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {
    
    private final DriverService driverService;
    private final ClientService clientService;
    private final OrderService orderService;
    private final EvaluationService evaluationService;
    private final ComplaintService complaintService;
    private final CommonService commonService;
    
    @ApiOperation("测试登录流程 - 完整流程演示")
    @PostMapping("/login-flow")
    public Result<Map<String, Object>> testLoginFlow() {
        Map<String, Object> result = new HashMap<>();
        
        LoginDto clientLoginDto = new LoginDto();
        clientLoginDto.setPhone("13800138001");
        clientLoginDto.setLoginType("VERIFY_CODE");
        Map<String, Object> clientLogin = clientService.login(clientLoginDto);
        result.put("clientLogin", clientLogin);
        
        LoginDto driverLoginDto = new LoginDto();
        driverLoginDto.setPhone("13900139001");
        driverLoginDto.setLoginType("VERIFY_CODE");
        Map<String, Object> driverLogin = driverService.login(driverLoginDto);
        result.put("driverLogin", driverLogin);
        
        return Result.success(result);
    }
    
    @ApiOperation("测试完整订单流程 - 下单->接单->开始服务->结束服务")
    @PostMapping("/order-flow")
    public Result<Map<String, Object>> testOrderFlow() {
        Map<String, Object> result = new HashMap<>();
        
        LoginDto clientLoginDto = new LoginDto();
        clientLoginDto.setPhone("13800138001");
        clientLoginDto.setLoginType("VERIFY_CODE");
        Map<String, Object> clientLogin = clientService.login(clientLoginDto);
        Long clientId = (Long) clientLogin.get("clientId");
        result.put("clientLogin", clientLogin);
        
        LoginDto driverLoginDto = new LoginDto();
        driverLoginDto.setPhone("13900139001");
        driverLoginDto.setLoginType("VERIFY_CODE");
        Map<String, Object> driverLogin = driverService.login(driverLoginDto);
        Long driverId = (Long) driverLogin.get("driverId");
        result.put("driverLogin", driverLogin);
        
        PlaceOrderDto placeOrderDto = new PlaceOrderDto();
        placeOrderDto.setOrderType("IMMEDIATE");
        placeOrderDto.setStartAddress("北京市朝阳区国贸中心");
        placeOrderDto.setStartLat(39.9087);
        placeOrderDto.setStartLng(116.4605);
        placeOrderDto.setEndAddress("北京市海淀区中关村");
        placeOrderDto.setEndLat(39.9842);
        placeOrderDto.setEndLng(116.3074);
        placeOrderDto.setDispatchType("ROBBERY");
        placeOrderDto.setVehicleType("经济型");
        placeOrderDto.setServiceType("普通代驾");
        Order order = orderService.createOrder(clientId, placeOrderDto);
        result.put("createOrder", orderService.getOrderDetail(order.getId()));
        
        List<OrderDetailDto> pendingOrders = orderService.getPendingRobberyOrders();
        result.put("pendingOrders", pendingOrders);
        
        orderService.acceptOrder(driverId, order.getId());
        OrderDetailDto acceptedOrder = orderService.getOrderDetail(order.getId());
        result.put("acceptedOrder", acceptedOrder);
        
        orderService.startService(driverId, order.getId());
        OrderDetailDto inServiceOrder = orderService.getOrderDetail(order.getId());
        result.put("inServiceOrder", inServiceOrder);
        
        orderService.endService(driverId, order.getId(), new BigDecimal("15.5"), new BigDecimal("55.00"));
        OrderDetailDto completedOrder = orderService.getOrderDetail(order.getId());
        result.put("completedOrder", completedOrder);
        
        return Result.success(result);
    }
    
    @ApiOperation("测试评价功能")
    @PostMapping("/evaluation-flow")
    public Result<Map<String, Object>> testEvaluationFlow() {
        Map<String, Object> result = new HashMap<>();
        
        LoginDto clientLoginDto = new LoginDto();
        clientLoginDto.setPhone("13800138001");
        clientLoginDto.setLoginType("VERIFY_CODE");
        Map<String, Object> clientLogin = clientService.login(clientLoginDto);
        Long clientId = (Long) clientLogin.get("clientId");
        
        LoginDto driverLoginDto = new LoginDto();
        driverLoginDto.setPhone("13900139001");
        driverLoginDto.setLoginType("VERIFY_CODE");
        Map<String, Object> driverLogin = driverService.login(driverLoginDto);
        Long driverId = (Long) driverLogin.get("driverId");
        
        PlaceOrderDto placeOrderDto = new PlaceOrderDto();
        placeOrderDto.setOrderType("IMMEDIATE");
        placeOrderDto.setStartAddress("测试起点");
        placeOrderDto.setEndAddress("测试终点");
        placeOrderDto.setDispatchType("ROBBERY");
        Order order = orderService.createOrder(clientId, placeOrderDto);
        orderService.acceptOrder(driverId, order.getId());
        orderService.startService(driverId, order.getId());
        orderService.endService(driverId, order.getId(), new BigDecimal("10.0"), new BigDecimal("35.00"));
        
        Evaluation clientEval = new Evaluation();
        clientEval.setOrderId(order.getId());
        clientEval.setFromUserId(clientId);
        clientEval.setToUserId(driverId);
        clientEval.setUserType("CLIENT");
        clientEval.setRating(new BigDecimal("5.0"));
        clientEval.setContent("司机服务态度很好，驾驶技术娴熟！");
        clientEval.setDrivingSkillRating(new BigDecimal("5.0"));
        clientEval.setServiceAttitudeRating(new BigDecimal("5.0"));
        Evaluation clientEvaluation = evaluationService.createEvaluation(clientEval);
        result.put("clientEvaluation", clientEvaluation);
        
        Evaluation driverEval = new Evaluation();
        driverEval.setOrderId(order.getId());
        driverEval.setFromUserId(driverId);
        driverEval.setToUserId(clientId);
        driverEval.setUserType("DRIVER");
        driverEval.setRating(new BigDecimal("4.5"));
        driverEval.setContent("乘客很礼貌，配合度高");
        driverEval.setIsDrunk(0);
        driverEval.setIsChangedDestination(0);
        Evaluation driverEvaluation = evaluationService.createEvaluation(driverEval);
        result.put("driverEvaluation", driverEvaluation);
        
        BigDecimal driverAvgRating = evaluationService.getAverageRating(driverId, "CLIENT");
        result.put("driverAvgRating", driverAvgRating);
        
        return Result.success(result);
    }
    
    @ApiOperation("测试营销活动")
    @GetMapping("/promotion-test")
    public Result<Map<String, Object>> testPromotion() {
        Map<String, Object> result = new HashMap<>();
        
        List<Promotion> clientPromotions = commonService.getActivePromotions("CLIENT");
        result.put("clientPromotions", clientPromotions);
        
        List<Promotion> driverPromotions = commonService.getActivePromotions("DRIVER");
        result.put("driverPromotions", driverPromotions);
        
        return Result.success(result);
    }
    
    @ApiOperation("测试紧急求助功能")
    @PostMapping("/emergency-test")
    public Result<Map<String, Object>> testEmergency() {
        Map<String, Object> result = new HashMap<>();
        
        LoginDto clientLoginDto = new LoginDto();
        clientLoginDto.setPhone("13800138001");
        clientLoginDto.setLoginType("VERIFY_CODE");
        Map<String, Object> clientLogin = clientService.login(clientLoginDto);
        Long clientId = (Long) clientLogin.get("clientId");
        
        EmergencyHelp help = new EmergencyHelp();
        help.setUserId(clientId);
        help.setUserType("CLIENT");
        help.setLocation("北京市朝阳区测试位置");
        help.setLat(39.9042);
        help.setLng(116.4074);
        help.setTripInfo("测试行程信息");
        EmergencyHelp createdHelp = commonService.createEmergencyHelp(help);
        result.put("createdHelp", createdHelp);
        
        commonService.updateEmergencyHelpStatus(createdHelp.getId(), "RECORDING");
        result.put("recordingHelp", commonService.getEmergencyHelpById(createdHelp.getId()));
        
        return Result.success(result);
    }
    
    @ApiOperation("测试司机设置功能")
    @PostMapping("/driver-setting-test")
    public Result<Map<String, Object>> testDriverSetting() {
        Map<String, Object> result = new HashMap<>();
        
        LoginDto driverLoginDto = new LoginDto();
        driverLoginDto.setPhone("13900139001");
        driverLoginDto.setLoginType("VERIFY_CODE");
        Map<String, Object> driverLogin = driverService.login(driverLoginDto);
        Long driverId = (Long) driverLogin.get("driverId");
        
        DriverSetting setting = driverService.getDriverSetting(driverId);
        result.put("originalSetting", setting);
        
        DriverSetting updateSetting = new DriverSetting();
        updateSetting.setDailyOrderLimit(20);
        updateSetting.setContinuousWorkHours(6);
        updateSetting.setEnableRestReminder(1);
        DriverSetting updatedSetting = driverService.updateDriverSetting(driverId, updateSetting);
        result.put("updatedSetting", updatedSetting);
        
        return Result.success(result);
    }
    
    @ApiOperation("获取所有测试数据汇总")
    @GetMapping("/summary")
    public Result<Map<String, Object>> getTestSummary() {
        Map<String, Object> result = new HashMap<>();
        
        Map<String, String> testAccounts = new HashMap<>();
        testAccounts.put("client1", "手机号:13800138001, 密码:123456");
        testAccounts.put("client2", "手机号:13800138002, 密码:123456");
        testAccounts.put("driver1", "手机号:13900139001, 密码:123456");
        testAccounts.put("driver2", "手机号:13900139002, 密码:123456");
        result.put("testAccounts", testAccounts);
        
        result.put("apiDocUrl", "http://localhost:8080/doc.html");
        result.put("h2ConsoleUrl", "http://localhost:8080/h2-console");
        
        return Result.success(result);
    }
}

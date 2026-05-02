package com.daijia.controller.common;

import com.daijia.common.Constants;
import com.daijia.common.Result;
import com.daijia.entity.*;
import com.daijia.service.ClientService;
import com.daijia.service.ComplaintService;
import com.daijia.service.CommonService;
import com.daijia.service.DriverService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "公共接口-紧急求助")
@RestController
@RequestMapping("/api/common/emergency")
@RequiredArgsConstructor
public class EmergencyHelpController {
    
    private final DriverService driverService;
    private final ClientService clientService;
    private final CommonService commonService;
    
    @ApiOperation("发起紧急求助(司机端)")
    @PostMapping("/driver/create")
    public Result<EmergencyHelp> createDriverEmergency(
            @RequestHeader("token") String token,
            @ApiParam("订单ID") @RequestParam(required = false) Long orderId,
            @ApiParam("位置描述") @RequestParam(required = false) String location,
            @ApiParam("纬度") @RequestParam(required = false) Double lat,
            @ApiParam("经度") @RequestParam(required = false) Double lng,
            @ApiParam("行程信息") @RequestParam(required = false) String tripInfo) {
        Driver driver = driverService.getDriverByToken(token);
        
        EmergencyHelp help = new EmergencyHelp();
        help.setUserId(driver.getId());
        help.setUserType("DRIVER");
        help.setOrderId(orderId);
        help.setLocation(location);
        help.setLat(lat);
        help.setLng(lng);
        help.setTripInfo(tripInfo);
        
        return Result.success(commonService.createEmergencyHelp(help));
    }
    
    @ApiOperation("发起紧急求助(客户端)")
    @PostMapping("/client/create")
    public Result<EmergencyHelp> createClientEmergency(
            @RequestHeader("token") String token,
            @ApiParam("订单ID") @RequestParam(required = false) Long orderId,
            @ApiParam("位置描述") @RequestParam(required = false) String location,
            @ApiParam("纬度") @RequestParam(required = false) Double lat,
            @ApiParam("经度") @RequestParam(required = false) Double lng,
            @ApiParam("行程信息") @RequestParam(required = false) String tripInfo) {
        Client client = clientService.getClientByToken(token);
        
        EmergencyHelp help = new EmergencyHelp();
        help.setUserId(client.getId());
        help.setUserType("CLIENT");
        help.setOrderId(orderId);
        help.setLocation(location);
        help.setLat(lat);
        help.setLng(lng);
        help.setTripInfo(tripInfo);
        
        return Result.success(commonService.createEmergencyHelp(help));
    }
    
    @ApiOperation("更新录音状态")
    @PostMapping("/recording-status")
    public Result<EmergencyHelp> updateRecordingStatus(
            @ApiParam("求助记录ID") @RequestParam Long id,
            @ApiParam("录音状态：NOT_STARTED-未开始，RECORDING-录音中，COMPLETED-已完成") @RequestParam String recordingStatus) {
        return Result.success(commonService.updateEmergencyHelpStatus(id, recordingStatus));
    }
    
    @ApiOperation("获取我的求助记录(司机端)")
    @GetMapping("/driver/list")
    public Result<List<EmergencyHelp>> getDriverEmergencyList(@RequestHeader("token") String token) {
        Driver driver = driverService.getDriverByToken(token);
        return Result.success(commonService.getEmergencyHelpByUserId(driver.getId()));
    }
    
    @ApiOperation("获取我的求助记录(客户端)")
    @GetMapping("/client/list")
    public Result<List<EmergencyHelp>> getClientEmergencyList(@RequestHeader("token") String token) {
        Client client = clientService.getClientByToken(token);
        return Result.success(commonService.getEmergencyHelpByUserId(client.getId()));
    }
    
    @ApiOperation("获取求助详情")
    @GetMapping("/detail/{id}")
    public Result<EmergencyHelp> getDetail(@PathVariable Long id) {
        return Result.success(commonService.getEmergencyHelpById(id));
    }
}

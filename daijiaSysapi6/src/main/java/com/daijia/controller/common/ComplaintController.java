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

@Api(tags = "公共接口-投诉管理")
@RestController
@RequestMapping("/api/common/complaint")
@RequiredArgsConstructor
public class ComplaintController {
    
    private final DriverService driverService;
    private final ClientService clientService;
    private final ComplaintService complaintService;
    
    @ApiOperation("司机投诉订单")
    @PostMapping("/driver/create")
    public Result<Complaint> createDriverComplaint(
            @RequestHeader("token") String token,
            @ApiParam("订单ID") @RequestParam Long orderId,
            @ApiParam("投诉类型") @RequestParam(required = false) String type,
            @ApiParam("投诉内容") @RequestParam String content,
            @ApiParam("图片证据(多个用逗号分隔)") @RequestParam(required = false) String images,
            @ApiParam("被投诉客户ID") @RequestParam Long respondentId) {
        Driver driver = driverService.getDriverByToken(token);
        
        Complaint complaint = new Complaint();
        complaint.setOrderId(orderId);
        complaint.setComplainantId(driver.getId());
        complaint.setRespondentId(respondentId);
        complaint.setComplainantType("DRIVER");
        complaint.setType(type);
        complaint.setContent(content);
        complaint.setImages(images);
        
        return Result.success(complaintService.createComplaint(complaint));
    }
    
    @ApiOperation("客户投诉订单")
    @PostMapping("/client/create")
    public Result<Complaint> createClientComplaint(
            @RequestHeader("token") String token,
            @ApiParam("订单ID") @RequestParam Long orderId,
            @ApiParam("投诉类型") @RequestParam(required = false) String type,
            @ApiParam("投诉内容") @RequestParam String content,
            @ApiParam("图片证据(多个用逗号分隔)") @RequestParam(required = false) String images,
            @ApiParam("被投诉司机ID") @RequestParam Long respondentId) {
        Client client = clientService.getClientByToken(token);
        
        Complaint complaint = new Complaint();
        complaint.setOrderId(orderId);
        complaint.setComplainantId(client.getId());
        complaint.setRespondentId(respondentId);
        complaint.setComplainantType("CLIENT");
        complaint.setType(type);
        complaint.setContent(content);
        complaint.setImages(images);
        
        return Result.success(complaintService.createComplaint(complaint));
    }
    
    @ApiOperation("获取我提出的投诉(司机端)")
    @GetMapping("/driver/my-complaints")
    public Result<List<Complaint>> getDriverMyComplaints(@RequestHeader("token") String token) {
        Driver driver = driverService.getDriverByToken(token);
        return Result.success(complaintService.getByComplainantId(driver.getId()));
    }
    
    @ApiOperation("获取我收到的投诉(司机端)")
    @GetMapping("/driver/received")
    public Result<List<Complaint>> getDriverReceivedComplaints(@RequestHeader("token") String token) {
        Driver driver = driverService.getDriverByToken(token);
        return Result.success(complaintService.getByRespondentId(driver.getId()));
    }
    
    @ApiOperation("获取我提出的投诉(客户端)")
    @GetMapping("/client/my-complaints")
    public Result<List<Complaint>> getClientMyComplaints(@RequestHeader("token") String token) {
        Client client = clientService.getClientByToken(token);
        return Result.success(complaintService.getByComplainantId(client.getId()));
    }
    
    @ApiOperation("获取我收到的投诉(客户端)")
    @GetMapping("/client/received")
    public Result<List<Complaint>> getClientReceivedComplaints(@RequestHeader("token") String token) {
        Client client = clientService.getClientByToken(token);
        return Result.success(complaintService.getByRespondentId(client.getId()));
    }
    
    @ApiOperation("获取投诉详情")
    @GetMapping("/detail/{id}")
    public Result<Complaint> getDetail(@PathVariable Long id) {
        return Result.success(complaintService.getById(id));
    }
}

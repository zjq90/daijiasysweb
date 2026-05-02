package com.daijia.controller.driver;

import com.daijia.common.Constants;
import com.daijia.common.Result;
import com.daijia.entity.Driver;
import com.daijia.entity.Evaluation;
import com.daijia.service.DriverService;
import com.daijia.service.EvaluationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Api(tags = "司机端-评价管理")
@RestController
@RequestMapping("/api/driver/evaluation")
@RequiredArgsConstructor
public class DriverEvaluationController {
    
    private final DriverService driverService;
    private final EvaluationService evaluationService;
    
    @ApiOperation("对客户进行评价")
    @PostMapping("/create")
    public Result<Evaluation> createEvaluation(
            @RequestHeader("token") String token,
            @ApiParam("订单ID") @RequestParam Long orderId,
            @ApiParam("评分(1-5分)") @RequestParam BigDecimal rating,
            @ApiParam("评价内容") @RequestParam(required = false) String content,
            @ApiParam("是否醉酒") @RequestParam(required = false) Integer isDrunk,
            @ApiParam("是否更改目的地") @RequestParam(required = false) Integer isChangedDestination,
            @ApiParam("客户ID") @RequestParam Long clientId) {
        Driver driver = driverService.getDriverByToken(token);
        
        Evaluation evaluation = new Evaluation();
        evaluation.setOrderId(orderId);
        evaluation.setFromUserId(driver.getId());
        evaluation.setToUserId(clientId);
        evaluation.setUserType(Constants.EVALUATION_FROM_DRIVER);
        evaluation.setRating(rating);
        evaluation.setContent(content);
        evaluation.setIsDrunk(isDrunk);
        evaluation.setIsChangedDestination(isChangedDestination);
        
        return Result.success(evaluationService.createEvaluation(evaluation));
    }
    
    @ApiOperation("获取我的评价列表(收到的)")
    @GetMapping("/received")
    public Result<List<Evaluation>> getReceivedEvaluations(@RequestHeader("token") String token) {
        Driver driver = driverService.getDriverByToken(token);
        return Result.success(evaluationService.getByToUserId(driver.getId()));
    }
    
    @ApiOperation("获取我的评价列表(发出的)")
    @GetMapping("/sent")
    public Result<List<Evaluation>> getSentEvaluations(@RequestHeader("token") String token) {
        Driver driver = driverService.getDriverByToken(token);
        return Result.success(evaluationService.getByFromUserId(driver.getId()));
    }
    
    @ApiOperation("获取订单评价")
    @GetMapping("/order/{orderId}")
    public Result<Evaluation> getByOrderId(
            @RequestHeader("token") String token,
            @PathVariable Long orderId) {
        driverService.getDriverByToken(token);
        return evaluationService.getByOrderId(orderId)
                .map(Result::success)
                .orElse(Result.success(null));
    }
}

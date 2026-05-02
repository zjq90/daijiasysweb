package com.daijia.controller.client;

import com.daijia.common.Constants;
import com.daijia.common.Result;
import com.daijia.entity.Client;
import com.daijia.entity.Evaluation;
import com.daijia.service.ClientService;
import com.daijia.service.EvaluationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Api(tags = "客户端-评价管理")
@RestController
@RequestMapping("/api/client/evaluation")
@RequiredArgsConstructor
public class ClientEvaluationController {
    
    private final ClientService clientService;
    private final EvaluationService evaluationService;
    
    @ApiOperation("对司机进行评价")
    @PostMapping("/create")
    public Result<Evaluation> createEvaluation(
            @RequestHeader("token") String token,
            @ApiParam("订单ID") @RequestParam Long orderId,
            @ApiParam("评分(1-5分)") @RequestParam BigDecimal rating,
            @ApiParam("评价内容") @RequestParam(required = false) String content,
            @ApiParam("驾驶技术评分") @RequestParam(required = false) BigDecimal drivingSkillRating,
            @ApiParam("服务态度评分") @RequestParam(required = false) BigDecimal serviceAttitudeRating,
            @ApiParam("司机ID") @RequestParam Long driverId) {
        Client client = clientService.getClientByToken(token);
        
        Evaluation evaluation = new Evaluation();
        evaluation.setOrderId(orderId);
        evaluation.setFromUserId(client.getId());
        evaluation.setToUserId(driverId);
        evaluation.setUserType(Constants.EVALUATION_FROM_CLIENT);
        evaluation.setRating(rating);
        evaluation.setContent(content);
        evaluation.setDrivingSkillRating(drivingSkillRating);
        evaluation.setServiceAttitudeRating(serviceAttitudeRating);
        
        return Result.success(evaluationService.createEvaluation(evaluation));
    }
    
    @ApiOperation("获取我的评价列表(收到的)")
    @GetMapping("/received")
    public Result<List<Evaluation>> getReceivedEvaluations(@RequestHeader("token") String token) {
        Client client = clientService.getClientByToken(token);
        return Result.success(evaluationService.getByToUserId(client.getId()));
    }
    
    @ApiOperation("获取我的评价列表(发出的)")
    @GetMapping("/sent")
    public Result<List<Evaluation>> getSentEvaluations(@RequestHeader("token") String token) {
        Client client = clientService.getClientByToken(token);
        return Result.success(evaluationService.getByFromUserId(client.getId()));
    }
    
    @ApiOperation("获取订单评价")
    @GetMapping("/order/{orderId}")
    public Result<Evaluation> getByOrderId(
            @RequestHeader("token") String token,
            @PathVariable Long orderId) {
        clientService.getClientByToken(token);
        return evaluationService.getByOrderId(orderId)
                .map(Result::success)
                .orElse(Result.success(null));
    }
}

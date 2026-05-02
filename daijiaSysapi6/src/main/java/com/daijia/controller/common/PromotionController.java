package com.daijia.controller.common;

import com.daijia.common.Result;
import com.daijia.entity.Promotion;
import com.daijia.service.ClientService;
import com.daijia.service.CommonService;
import com.daijia.service.DriverService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "公共接口-营销活动")
@RestController
@RequestMapping("/api/common/promotion")
@RequiredArgsConstructor
public class PromotionController {
    
    private final CommonService commonService;
    
    @ApiOperation("获取可用的营销活动(司机端)")
    @GetMapping("/driver/list")
    public Result<List<Promotion>> getDriverPromotions() {
        return Result.success(commonService.getActivePromotions("DRIVER"));
    }
    
    @ApiOperation("获取可用的营销活动(客户端)")
    @GetMapping("/client/list")
    public Result<List<Promotion>> getClientPromotions() {
        return Result.success(commonService.getActivePromotions("CLIENT"));
    }
    
    @ApiOperation("获取营销活动详情")
    @GetMapping("/detail/{id}")
    public Result<Promotion> getDetail(@PathVariable Long id) {
        return Result.success(commonService.getPromotionById(id));
    }
}

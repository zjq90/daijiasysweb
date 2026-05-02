package com.daijia.controller.driver;

import com.daijia.common.Result;
import com.daijia.dto.LoginDto;
import com.daijia.entity.Driver;
import com.daijia.entity.DriverSetting;
import com.daijia.service.DriverService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@Api(tags = "司机端-账号管理")
@RestController
@RequestMapping("/api/driver/account")
@RequiredArgsConstructor
public class DriverAccountController {
    
    private final DriverService driverService;
    
    @ApiOperation("司机登录")
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@Valid @RequestBody LoginDto dto) {
        return Result.success(driverService.login(dto));
    }
    
    @ApiOperation("退出登录")
    @PostMapping("/logout")
    public Result<Void> logout(@RequestHeader("token") String token) {
        Driver driver = driverService.getDriverByToken(token);
        driverService.logout(driver.getId());
        return Result.success();
    }
    
    @ApiOperation("获取司机信息")
    @GetMapping("/info")
    public Result<Driver> getInfo(@RequestHeader("token") String token) {
        Driver driver = driverService.getDriverByToken(token);
        driver.setPassword(null);
        driver.setLoginToken(null);
        return Result.success(driver);
    }
    
    @ApiOperation("更新司机信息")
    @PostMapping("/update")
    public Result<Driver> updateInfo(
            @RequestHeader("token") String token,
            @RequestBody Map<String, Object> params) {
        Driver driver = driverService.getDriverByToken(token);
        Driver updated = driverService.updateProfile(driver.getId(), params);
        updated.setPassword(null);
        updated.setLoginToken(null);
        return Result.success(updated);
    }
    
    @ApiOperation("更新在线状态")
    @PostMapping("/online-status")
    public Result<Driver> updateOnlineStatus(
            @RequestHeader("token") String token,
            @ApiParam("在线状态：0-离线，1-在线，2-忙碌") @RequestParam Integer status) {
        Driver driver = driverService.getDriverByToken(token);
        Driver updated = driverService.updateOnlineStatus(driver.getId(), status);
        updated.setPassword(null);
        updated.setLoginToken(null);
        return Result.success(updated);
    }
    
    @ApiOperation("更新位置信息")
    @PostMapping("/location")
    public Result<Driver> updateLocation(
            @RequestHeader("token") String token,
            @ApiParam("纬度") @RequestParam Double lat,
            @ApiParam("经度") @RequestParam Double lng) {
        Driver driver = driverService.getDriverByToken(token);
        Driver updated = driverService.updateLocation(driver.getId(), lat, lng);
        updated.setPassword(null);
        updated.setLoginToken(null);
        return Result.success(updated);
    }
    
    @ApiOperation("获取司机设置")
    @GetMapping("/setting")
    public Result<DriverSetting> getSetting(@RequestHeader("token") String token) {
        Driver driver = driverService.getDriverByToken(token);
        return Result.success(driverService.getDriverSetting(driver.getId()));
    }
    
    @ApiOperation("更新司机设置")
    @PostMapping("/setting")
    public Result<DriverSetting> updateSetting(
            @RequestHeader("token") String token,
            @RequestBody DriverSetting setting) {
        Driver driver = driverService.getDriverByToken(token);
        return Result.success(driverService.updateDriverSetting(driver.getId(), setting));
    }
}

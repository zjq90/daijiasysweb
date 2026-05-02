package com.daijia.controller;

import com.daijia.entity.Driver;
import com.daijia.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 司机API控制器
 * 提供司机相关的RESTful接口
 *
 * @author daijia
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/driver")
public class DriverApiController {

    @Autowired
    private DriverService driverService;

    /**
     * 获取所有司机列表
     *
     * @return 司机列表
     */
    @GetMapping("/list")
    public ResponseEntity<List<Driver>> list() {
        List<Driver> drivers = driverService.getAllDrivers();
        return ResponseEntity.ok(drivers);
    }

    /**
     * 根据ID获取司机详情
     *
     * @param id 司机ID
     * @return 司机详情
     */
    @GetMapping("/detail/{id}")
    public ResponseEntity<Driver> detail(@PathVariable Long id) {
        Driver driver = driverService.getDriverById(id);
        if (driver == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(driver);
    }

    /**
     * 创建新司机
     *
     * @param driver 司机信息
     * @return 创建结果
     */
    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> create(@RequestBody Driver driver) {
        Map<String, Object> result = new HashMap<>();
        try {
            Driver savedDriver = driverService.createDriver(driver);
            result.put("success", true);
            result.put("message", "创建司机成功");
            result.put("data", savedDriver);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "创建司机失败：" + e.getMessage());
            return ResponseEntity.badRequest().body(result);
        }
    }

    /**
     * 更新司机信息
     *
     * @param driver 司机信息
     * @return 更新结果
     */
    @PostMapping("/update")
    public ResponseEntity<Map<String, Object>> update(@RequestBody Driver driver) {
        Map<String, Object> result = new HashMap<>();
        try {
            Driver existingDriver = driverService.getDriverById(driver.getId());
            if (existingDriver == null) {
                result.put("success", false);
                result.put("message", "司机不存在");
                return ResponseEntity.badRequest().body(result);
            }
            Driver updatedDriver = driverService.updateDriver(driver);
            result.put("success", true);
            result.put("message", "更新司机成功");
            result.put("data", updatedDriver);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "更新司机失败：" + e.getMessage());
            return ResponseEntity.badRequest().body(result);
        }
    }

    /**
     * 删除司机
     *
     * @param id 司机ID
     * @return 删除结果
     */
    @PostMapping("/delete/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        boolean deleted = driverService.deleteDriver(id);
        if (deleted) {
            result.put("success", true);
            result.put("message", "删除司机成功");
            return ResponseEntity.ok(result);
        } else {
            result.put("success", false);
            result.put("message", "司机不存在");
            return ResponseEntity.badRequest().body(result);
        }
    }

    /**
     * 司机上线
     *
     * @param params 包含driverId、latitude、longitude
     * @return 结果
     */
    @PostMapping("/online")
    public ResponseEntity<Map<String, Object>> online(@RequestBody Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        try {
            Long driverId = Long.valueOf(params.get("driverId").toString());
            BigDecimal latitude = params.containsKey("latitude") 
                    ? new BigDecimal(params.get("latitude").toString()) 
                    : null;
            BigDecimal longitude = params.containsKey("longitude") 
                    ? new BigDecimal(params.get("longitude").toString()) 
                    : null;
            Driver driver = driverService.goOnline(driverId, latitude, longitude);
            if (driver != null) {
                result.put("success", true);
                result.put("message", "司机上线成功");
                result.put("data", driver);
                return ResponseEntity.ok(result);
            } else {
                result.put("success", false);
                result.put("message", "上线失败：司机状态不正确");
                return ResponseEntity.badRequest().body(result);
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "上线失败：" + e.getMessage());
            return ResponseEntity.badRequest().body(result);
        }
    }

    /**
     * 司机下线
     *
     * @param params 包含driverId
     * @return 结果
     */
    @PostMapping("/offline")
    public ResponseEntity<Map<String, Object>> offline(@RequestBody Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        try {
            Long driverId = Long.valueOf(params.get("driverId").toString());
            Driver driver = driverService.goOffline(driverId);
            if (driver != null) {
                result.put("success", true);
                result.put("message", "司机下线成功");
                result.put("data", driver);
                return ResponseEntity.ok(result);
            } else {
                result.put("success", false);
                result.put("message", "下线失败：司机已离线");
                return ResponseEntity.badRequest().body(result);
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "下线失败：" + e.getMessage());
            return ResponseEntity.badRequest().body(result);
        }
    }

    /**
     * 更新司机位置
     *
     * @param params 包含driverId、latitude、longitude
     * @return 结果
     */
    @PostMapping("/location")
    public ResponseEntity<Map<String, Object>> updateLocation(@RequestBody Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        try {
            Long driverId = Long.valueOf(params.get("driverId").toString());
            BigDecimal latitude = new BigDecimal(params.get("latitude").toString());
            BigDecimal longitude = new BigDecimal(params.get("longitude").toString());
            Driver driver = driverService.updateLocation(driverId, latitude, longitude);
            if (driver != null) {
                result.put("success", true);
                result.put("message", "更新位置成功");
                result.put("data", driver);
                return ResponseEntity.ok(result);
            } else {
                result.put("success", false);
                result.put("message", "更新位置失败：司机不存在");
                return ResponseEntity.badRequest().body(result);
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "更新位置失败：" + e.getMessage());
            return ResponseEntity.badRequest().body(result);
        }
    }

    /**
     * 获取司机状态统计
     *
     * @return 统计数据
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> stats() {
        Map<String, Object> stats = driverService.getDriverStatusStats();
        return ResponseEntity.ok(stats);
    }

    /**
     * 获取在线司机列表
     *
     * @return 司机列表
     */
    @GetMapping("/online")
    public ResponseEntity<List<Driver>> onlineDrivers() {
        List<Driver> drivers = driverService.getOnlineDrivers();
        return ResponseEntity.ok(drivers);
    }

    /**
     * 获取司机位置分布数据
     *
     * @return 位置数据列表
     */
    @GetMapping("/locations")
    public ResponseEntity<List<Map<String, Object>>> locations() {
        List<Map<String, Object>> locations = driverService.getDriverLocations();
        return ResponseEntity.ok(locations);
    }

    /**
     * 获取司机排行榜
     *
     * @param limit 返回数量
     * @return 司机列表
     */
    @GetMapping("/top")
    public ResponseEntity<List<Driver>> topDrivers(@RequestParam(defaultValue = "10") int limit) {
        List<Driver> drivers = driverService.getTopDrivers(limit);
        return ResponseEntity.ok(drivers);
    }
}

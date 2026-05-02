package com.daijia.controller;

import com.daijia.service.DriverService;
import com.daijia.service.OrderService;
import com.daijia.service.ServiceAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 运营监控API控制器
 * 提供运营监控相关的RESTful接口：订单状态监控、司机分布、服务区域热力图
 *
 * @author daijia
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/monitor")
public class MonitorApiController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private DriverService driverService;

    @Autowired
    private ServiceAreaService serviceAreaService;

    /**
     * 获取实时监控概览数据
     * 包含订单状态、司机状态、服务区域热力图概览
     *
     * @return 监控概览数据
     */
    @GetMapping("/overview")
    public ResponseEntity<Map<String, Object>> overview() {
        Map<String, Object> overview = new HashMap<>();
        
        // 订单状态统计
        Map<String, Object> orderStats = orderService.getOrderStatusStats();
        overview.put("orderStats", orderStats);
        
        // 司机状态统计
        Map<String, Object> driverStats = driverService.getDriverStatusStats();
        overview.put("driverStats", driverStats);
        
        // 服务区域统计
        Map<String, Object> areaStats = serviceAreaService.getAreaStats();
        overview.put("areaStats", areaStats);
        
        // 今日订单统计
        Map<String, Object> todayOrderStats = orderService.getTodayOrderStats();
        overview.put("todayOrderStats", todayOrderStats);
        
        return ResponseEntity.ok(overview);
    }

    /**
     * 获取订单状态监控数据
     * 实时监控各状态订单数量
     *
     * @return 订单状态数据
     */
    @GetMapping("/order-status")
    public ResponseEntity<Map<String, Object>> orderStatus() {
        Map<String, Object> result = new HashMap<>();
        
        // 获取订单状态统计
        Map<String, Object> stats = orderService.getOrderStatusStats();
        result.put("stats", stats);
        
        // 获取进行中的订单列表
        result.put("activeOrders", orderService.getActiveOrders());
        
        return ResponseEntity.ok(result);
    }

    /**
     * 获取司机分布监控数据
     * 实时监控在线司机的位置分布
     *
     * @return 司机分布数据
     */
    @GetMapping("/driver-distribution")
    public ResponseEntity<Map<String, Object>> driverDistribution() {
        Map<String, Object> result = new HashMap<>();
        
        // 司机状态统计
        Map<String, Object> stats = driverService.getDriverStatusStats();
        result.put("stats", stats);
        
        // 司机位置数据（用于地图展示）
        List<Map<String, Object>> locations = driverService.getDriverLocations();
        result.put("locations", locations);
        
        // 在线司机列表
        result.put("onlineDrivers", driverService.getOnlineDrivers());
        
        return ResponseEntity.ok(result);
    }

    /**
     * 获取服务区域热力图数据
     *
     * @return 热力图数据
     */
    @GetMapping("/heatmap")
    public ResponseEntity<Map<String, Object>> heatmap() {
        Map<String, Object> result = new HashMap<>();
        
        // 同步更新区域统计数据
        serviceAreaService.syncAreaStats();
        
        // 获取热力图数据
        List<Map<String, Object>> heatMapData = serviceAreaService.getHeatMapData();
        result.put("heatMapData", heatMapData);
        
        // 获取服务区域列表
        result.put("serviceAreas", serviceAreaService.getActiveServiceAreas());
        
        // 获取区域统计数据
        result.put("areaStats", serviceAreaService.getAreaStats());
        
        return ResponseEntity.ok(result);
    }

    /**
     * 获取实时刷新数据
     * 用于前端定时刷新
     *
     * @return 实时数据
     */
    @GetMapping("/realtime")
    public ResponseEntity<Map<String, Object>> realtime() {
        Map<String, Object> result = new HashMap<>();
        
        // 订单状态（关键指标）
        Map<String, Object> orderStats = orderService.getOrderStatusStats();
        result.put("pendingOrders", orderStats.get("pendingWaiting"));
        result.put("activeOrders", orderStats.get("accepted"));
        result.put("inServiceOrders", orderStats.get("inService"));
        result.put("todayTotalOrders", orderService.getTodayOrderStats().get("todayOrders"));
        
        // 司机状态（关键指标）
        Map<String, Object> driverStats = driverService.getDriverStatusStats();
        result.put("onlineDrivers", driverStats.get("onlineTotal"));
        result.put("totalDrivers", driverStats.get("total"));
        
        // 司机位置数据
        result.put("driverLocations", driverService.getDriverLocations());
        
        return ResponseEntity.ok(result);
    }
}

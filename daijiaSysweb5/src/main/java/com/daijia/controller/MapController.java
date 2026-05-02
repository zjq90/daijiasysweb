package com.daijia.controller;

import com.daijia.entity.LocationTrace;
import com.daijia.entity.Order;
import com.daijia.service.LocationTraceService;
import com.daijia.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 地图控制器
 * 
 * 处理实时跟踪与导航功能
 * 集成高德地图，支持：
 * 1. 实时显示客户与司机位置
 * 2. 路线规划与显示
 * 3. 预计到达时间显示
 * 4. 实时跟踪行驶路线
 * 
 * @author daijia
 * @version 1.0.0
 */
@Controller
@RequestMapping("/map")
public class MapController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private LocationTraceService locationTraceService;

    /**
     * 地图跟踪页面
     * 
     * @param model 模型对象
     * @param session 会话对象
     * @return 地图跟踪视图
     */
    @GetMapping("/tracking")
    public String tracking(Model model, HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/login";
        }

        List<Order> activeOrders = orderService.findActiveOrders();
        model.addAttribute("activeOrders", activeOrders);
        model.addAttribute("currentPage", "map");

        return "map/tracking";
    }

    /**
     * 订单地图详情页面
     * 
     * @param orderId 订单ID
     * @param model 模型对象
     * @param session 会话对象
     * @return 订单地图视图
     */
    @GetMapping("/order/{orderId}")
    public String orderMap(@PathVariable Long orderId, Model model, HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/login";
        }

        Optional<Order> orderOpt = orderService.findById(orderId);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            model.addAttribute("order", order);
            
            List<LocationTrace> traces = locationTraceService.findByOrderIdOrderByTimeAsc(orderId);
            model.addAttribute("traces", traces);
            
            LocationTrace latestTrace = locationTraceService.getLatestLocationByOrderId(orderId);
            model.addAttribute("latestTrace", latestTrace);
        }

        model.addAttribute("currentPage", "map");
        return "map/order-map";
    }

    /**
     * 获取订单位置数据API
     * 
     * @param orderId 订单ID
     * @param session 会话对象
     * @return JSON格式的位置数据
     */
    @GetMapping("/api/location/{orderId}")
    @ResponseBody
    public Map<String, Object> getLocation(@PathVariable Long orderId, HttpSession session) {
        Map<String, Object> result = new HashMap<>();

        if (session.getAttribute("admin") == null) {
            result.put("success", false);
            result.put("message", "未登录");
            return result;
        }

        Optional<Order> orderOpt = orderService.findById(orderId);
        if (!orderOpt.isPresent()) {
            result.put("success", false);
            result.put("message", "订单不存在");
            return result;
        }

        Order order = orderOpt.get();
        result.put("success", true);
        result.put("order", order);

        List<LocationTrace> traces = locationTraceService.findByOrderIdOrderByTimeAsc(orderId);
        result.put("traces", traces);

        LocationTrace latestTrace = locationTraceService.getLatestLocationByOrderId(orderId);
        result.put("latestTrace", latestTrace);

        return result;
    }

    /**
     * 获取所有进行中订单的位置数据
     * 
     * @param session 会话对象
     * @return JSON格式的位置数据
     */
    @GetMapping("/api/all-locations")
    @ResponseBody
    public Map<String, Object> getAllLocations(HttpSession session) {
        Map<String, Object> result = new HashMap<>();

        if (session.getAttribute("admin") == null) {
            result.put("success", false);
            result.put("message", "未登录");
            return result;
        }

        List<Order> activeOrders = orderService.findActiveOrders();
        result.put("success", true);
        result.put("orders", activeOrders);

        Map<Long, LocationTrace> latestTraces = new HashMap<>();
        for (Order order : activeOrders) {
            LocationTrace trace = locationTraceService.getLatestLocationByOrderId(order.getId());
            if (trace != null) {
                latestTraces.put(order.getId(), trace);
            }
        }
        result.put("latestTraces", latestTraces);

        return result;
    }

    /**
     * 模拟位置更新（用于测试）
     * 
     * @param orderId 订单ID
     * @param session 会话对象
     * @return JSON结果
     */
    @PostMapping("/api/simulate-update")
    @ResponseBody
    public Map<String, Object> simulateUpdate(@RequestParam Long orderId, HttpSession session) {
        Map<String, Object> result = new HashMap<>();

        if (session.getAttribute("admin") == null) {
            result.put("success", false);
            result.put("message", "未登录");
            return result;
        }

        Optional<Order> orderOpt = orderService.findById(orderId);
        if (!orderOpt.isPresent()) {
            result.put("success", false);
            result.put("message", "订单不存在");
            return result;
        }

        Order order = orderOpt.get();
        LocationTrace latestTrace = locationTraceService.getLatestLocationByOrderId(orderId);

        if (latestTrace == null) {
            LocationTrace newTrace = new LocationTrace();
            newTrace.setOrderId(orderId);
            newTrace.setDriverId(order.getDriverId());
            newTrace.setLatitude(order.getStartLatitude());
            newTrace.setLongitude(order.getStartLongitude());
            newTrace = locationTraceService.save(newTrace);
            result.put("success", true);
            result.put("trace", newTrace);
        } else {
            BigDecimal targetLat = order.getEndLatitude();
            BigDecimal targetLng = order.getEndLongitude();
            
            LocationTrace newTrace = locationTraceService.simulateLocationUpdate(
                orderId,
                order.getDriverId(),
                latestTrace.getLatitude(),
                latestTrace.getLongitude(),
                targetLat,
                targetLng,
                0.1
            );
            result.put("success", true);
            result.put("trace", newTrace);
        }

        return result;
    }

    /**
     * 实时获取订单位置（用于前端轮询）
     * 
     * @param orderId 订单ID
     * @param session 会话对象
     * @return JSON格式的最新位置
     */
    @GetMapping("/api/realtime/{orderId}")
    @ResponseBody
    public Map<String, Object> getRealtimeLocation(@PathVariable Long orderId, HttpSession session) {
        Map<String, Object> result = new HashMap<>();

        if (session.getAttribute("admin") == null) {
            result.put("success", false);
            return result;
        }

        Optional<Order> orderOpt = orderService.findById(orderId);
        if (!orderOpt.isPresent()) {
            result.put("success", false);
            result.put("message", "订单不存在");
            return result;
        }

        Order order = orderOpt.get();
        LocationTrace latestTrace = locationTraceService.getLatestLocationByOrderId(orderId);

        result.put("success", true);
        result.put("orderStatus", order.getStatus());
        result.put("latestTrace", latestTrace);
        
        if (latestTrace != null) {
            result.put("currentLat", latestTrace.getLatitude());
            result.put("currentLng", latestTrace.getLongitude());
            result.put("speed", latestTrace.getSpeed());
            result.put("direction", latestTrace.getDirection());
            result.put("locationTime", latestTrace.getLocationTime());
        }

        result.put("startLat", order.getStartLatitude());
        result.put("startLng", order.getStartLongitude());
        result.put("endLat", order.getEndLatitude());
        result.put("endLng", order.getEndLongitude());
        result.put("estimateTime", order.getEstimateTime());
        result.put("estimateDistance", order.getEstimateDistance());

        return result;
    }
}

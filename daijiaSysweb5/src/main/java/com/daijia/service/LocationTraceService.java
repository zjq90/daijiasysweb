package com.daijia.service;

import com.daijia.entity.LocationTrace;
import com.daijia.repository.LocationTraceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 位置跟踪服务类
 * 
 * 处理实时位置跟踪业务逻辑，支持地图导航显示
 * 
 * @author daijia
 * @version 1.0.0
 */
@Service
public class LocationTraceService {

    @Autowired
    private LocationTraceRepository locationTraceRepository;

    /**
     * 保存位置跟踪记录
     * 
     * @param locationTrace 位置跟踪对象
     * @return LocationTrace
     */
    @Transactional
    public LocationTrace save(LocationTrace locationTrace) {
        return locationTraceRepository.save(locationTrace);
    }

    /**
     * 根据订单ID获取位置跟踪记录（按时间升序）
     * 
     * @param orderId 订单ID
     * @return List<LocationTrace>
     */
    public List<LocationTrace> findByOrderIdOrderByTimeAsc(Long orderId) {
        return locationTraceRepository.findByOrderIdOrderByLocationTimeAsc(orderId);
    }

    /**
     * 根据订单ID获取位置跟踪记录（按时间降序）
     * 
     * @param orderId 订单ID
     * @return List<LocationTrace>
     */
    public List<LocationTrace> findByOrderIdOrderByTimeDesc(Long orderId) {
        return locationTraceRepository.findByOrderIdOrderByLocationTimeDesc(orderId);
    }

    /**
     * 获取订单的最新位置
     * 
     * @param orderId 订单ID
     * @return LocationTrace 最新位置记录，不存在返回null
     */
    public LocationTrace getLatestLocationByOrderId(Long orderId) {
        List<LocationTrace> traces = locationTraceRepository.findLatestByOrderId(orderId);
        if (traces != null && !traces.isEmpty()) {
            return traces.get(0);
        }
        return null;
    }

    /**
     * 添加位置跟踪记录
     * 
     * @param orderId 订单ID
     * @param driverId 司机ID
     * @param latitude 纬度
     * @param longitude 经度
     * @param speed 速度（可选）
     * @param direction 方向（可选）
     * @return LocationTrace
     */
    @Transactional
    public LocationTrace addTrace(Long orderId, Long driverId, 
                                   BigDecimal latitude, BigDecimal longitude,
                                   BigDecimal speed, Integer direction) {
        LocationTrace trace = new LocationTrace();
        trace.setOrderId(orderId);
        trace.setDriverId(driverId);
        trace.setLatitude(latitude);
        trace.setLongitude(longitude);
        trace.setSpeed(speed);
        trace.setDirection(direction);
        trace.setLocationTime(LocalDateTime.now());
        return locationTraceRepository.save(trace);
    }

    /**
     * 模拟位置更新（用于测试）
     * 
     * @param orderId 订单ID
     * @param driverId 司机ID
     * @param currentLat 当前纬度
     * @param currentLng 当前经度
     * @param targetLat 目标纬度
     * @param targetLng 目标经度
     * @param step 步长（每步移动的距离比例）
     * @return LocationTrace 新位置
     */
    @Transactional
    public LocationTrace simulateLocationUpdate(Long orderId, Long driverId,
                                                  BigDecimal currentLat, BigDecimal currentLng,
                                                  BigDecimal targetLat, BigDecimal targetLng,
                                                  double step) {
        double latDiff = targetLat.doubleValue() - currentLat.doubleValue();
        double lngDiff = targetLng.doubleValue() - currentLng.doubleValue();
        
        BigDecimal newLat = currentLat.add(BigDecimal.valueOf(latDiff * step));
        BigDecimal newLng = currentLng.add(BigDecimal.valueOf(lngDiff * step));
        
        BigDecimal speed = BigDecimal.valueOf(30 + Math.random() * 30);
        Integer direction = (int) (Math.random() * 360);
        
        return addTrace(orderId, driverId, newLat, newLng, speed, direction);
    }

    /**
     * 根据订单ID删除所有位置跟踪记录
     * 
     * @param orderId 订单ID
     */
    @Transactional
    public void deleteByOrderId(Long orderId) {
        locationTraceRepository.deleteByOrderId(orderId);
    }

    /**
     * 获取时间范围内的位置跟踪记录
     * 
     * @param orderId 订单ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return List<LocationTrace>
     */
    public List<LocationTrace> findByOrderIdAndTimeRange(Long orderId, 
                                                           LocalDateTime startTime, 
                                                           LocalDateTime endTime) {
        return locationTraceRepository.findByOrderIdAndTimeRange(orderId, startTime, endTime);
    }
}

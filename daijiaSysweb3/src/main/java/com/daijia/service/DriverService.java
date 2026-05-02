package com.daijia.service;

import com.daijia.entity.Driver;
import com.daijia.repository.DriverRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 司机服务类
 * 提供司机相关的业务逻辑处理，包括增删改查、状态管理、位置监控等
 *
 * @author daijia
 * @version 1.0.0
 */
@Service
@Transactional
public class DriverService {

    private static final Logger logger = LoggerFactory.getLogger(DriverService.class);

    @Autowired
    private DriverRepository driverRepository;

    /**
     * 生成唯一司机编号
     * 格式：SJ + 年月日 + 4位随机数
     *
     * @return 司机编号
     */
    public String generateDriverNo() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String random = String.format("%04d", new Random().nextInt(10000));
        return "SJ" + timestamp + random;
    }

    /**
     * 创建新司机
     *
     * @param driver 司机对象
     * @return 创建后的司机对象
     */
    public Driver createDriver(Driver driver) {
        if (driver.getDriverNo() == null || driver.getDriverNo().isEmpty()) {
            driver.setDriverNo(generateDriverNo());
        }
        if (driver.getRegisterTime() == null) {
            driver.setRegisterTime(LocalDateTime.now());
        }
        driver.setCreateTime(LocalDateTime.now());
        driver.setUpdateTime(LocalDateTime.now());
        Driver savedDriver = driverRepository.save(driver);
        logger.info("创建司机成功，司机编号：{}", savedDriver.getDriverNo());
        return savedDriver;
    }

    /**
     * 根据ID查询司机
     *
     * @param id 司机ID
     * @return 司机对象
     */
    public Driver getDriverById(Long id) {
        return driverRepository.findById(id).orElse(null);
    }

    /**
     * 根据司机编号查询司机
     *
     * @param driverNo 司机编号
     * @return 司机对象
     */
    public Driver getDriverByNo(String driverNo) {
        return driverRepository.findByDriverNo(driverNo);
    }

    /**
     * 更新司机信息
     *
     * @param driver 司机对象
     * @return 更新后的司机对象
     */
    public Driver updateDriver(Driver driver) {
        driver.setUpdateTime(LocalDateTime.now());
        Driver updatedDriver = driverRepository.save(driver);
        logger.info("更新司机成功，司机编号：{}", updatedDriver.getDriverNo());
        return updatedDriver;
    }

    /**
     * 删除司机
     *
     * @param id 司机ID
     * @return 是否删除成功
     */
    public boolean deleteDriver(Long id) {
        Optional<Driver> driverOpt = driverRepository.findById(id);
        if (driverOpt.isPresent()) {
            driverRepository.delete(driverOpt.get());
            logger.info("删除司机成功，司机ID：{}", id);
            return true;
        }
        return false;
    }

    /**
     * 查询所有司机
     *
     * @return 司机列表
     */
    public List<Driver> getAllDrivers() {
        return driverRepository.findAll();
    }

    /**
     * 司机上线
     *
     * @param driverId 司机ID
     * @param latitude 当前纬度
     * @param longitude 当前经度
     * @return 更新后的司机对象
     */
    public Driver goOnline(Long driverId, BigDecimal latitude, BigDecimal longitude) {
        Driver driver = getDriverById(driverId);
        if (driver != null && "OFFLINE".equals(driver.getStatus())) {
            driver.setStatus("ONLINE");
            driver.setOnlineTime(LocalDateTime.now());
            driver.setStatusUpdateTime(LocalDateTime.now());
            if (latitude != null && longitude != null) {
                driver.setCurrentLatitude(latitude);
                driver.setCurrentLongitude(longitude);
            }
            return updateDriver(driver);
        }
        return null;
    }

    /**
     * 司机下线
     *
     * @param driverId 司机ID
     * @return 更新后的司机对象
     */
    public Driver goOffline(Long driverId) {
        Driver driver = getDriverById(driverId);
        if (driver != null && !"OFFLINE".equals(driver.getStatus())) {
            driver.setStatus("OFFLINE");
            driver.setStatusUpdateTime(LocalDateTime.now());
            return updateDriver(driver);
        }
        return null;
    }

    /**
     * 更新司机位置
     *
     * @param driverId  司机ID
     * @param latitude  纬度
     * @param longitude 经度
     * @return 更新后的司机对象
     */
    public Driver updateLocation(Long driverId, BigDecimal latitude, BigDecimal longitude) {
        Driver driver = getDriverById(driverId);
        if (driver != null) {
            driver.setCurrentLatitude(latitude);
            driver.setCurrentLongitude(longitude);
            driver.setUpdateTime(LocalDateTime.now());
            return driverRepository.save(driver);
        }
        return null;
    }

    /**
     * 获取实时司机状态统计
     *
     * @return 司机状态统计数据
     */
    public Map<String, Object> getDriverStatusStats() {
        Map<String, Object> stats = new HashMap<>();
        List<Object[]> results = driverRepository.countDriversByStatus();
        
        Map<String, Long> statusMap = new HashMap<>();
        for (Object[] result : results) {
            String status = (String) result[0];
            Long count = (Long) result[1];
            statusMap.put(status, count);
        }
        
        stats.put("total", driverRepository.count());
        stats.put("offline", statusMap.getOrDefault("OFFLINE", 0L));
        stats.put("online", statusMap.getOrDefault("ONLINE", 0L));
        stats.put("inService", statusMap.getOrDefault("IN_SERVICE", 0L));
        stats.put("paused", statusMap.getOrDefault("PAUSED", 0L));
        
        // 计算在线总数
        long onlineTotal = statusMap.getOrDefault("ONLINE", 0L) + statusMap.getOrDefault("IN_SERVICE", 0L);
        stats.put("onlineTotal", onlineTotal);
        
        return stats;
    }

    /**
     * 获取在线司机列表
     *
     * @return 在线司机列表
     */
    public List<Driver> getOnlineDrivers() {
        return driverRepository.findOnlineDrivers();
    }

    /**
     * 获取司机位置分布数据
     * 用于地图展示司机实时位置
     *
     * @return 司机位置列表
     */
    public List<Map<String, Object>> getDriverLocations() {
        List<Map<String, Object>> locations = new ArrayList<>();
        List<Driver> onlineDrivers = getOnlineDrivers();
        
        for (Driver driver : onlineDrivers) {
            if (driver.getCurrentLatitude() != null && driver.getCurrentLongitude() != null) {
                Map<String, Object> location = new HashMap<>();
                location.put("driverId", driver.getId());
                location.put("driverNo", driver.getDriverNo());
                location.put("name", driver.getName());
                location.put("status", driver.getStatus());
                location.put("latitude", driver.getCurrentLatitude());
                location.put("longitude", driver.getCurrentLongitude());
                location.put("rating", driver.getRating());
                location.put("todayOrders", driver.getTodayOrders());
                location.put("todayIncome", driver.getTodayIncome());
                locations.add(location);
            }
        }
        
        return locations;
    }

    /**
     * 获取司机排行榜（按评分和订单数）
     *
     * @param limit 返回数量限制
     * @return 司机列表
     */
    public List<Driver> getTopDrivers(int limit) {
        List<Driver> allDrivers = driverRepository.findTopDrivers();
        if (limit <= 0 || limit >= allDrivers.size()) {
            return allDrivers;
        }
        return allDrivers.subList(0, limit);
    }

    /**
     * 根据服务区域查询司机
     *
     * @param serviceAreaId 服务区域ID
     * @return 司机列表
     */
    public List<Driver> getDriversByServiceArea(Long serviceAreaId) {
        return driverRepository.findByServiceAreaId(serviceAreaId);
    }

    /**
     * 更新司机今日数据
     *
     * @param driverId 司机ID
     * @param orderAmount 订单金额
     */
    public void updateTodayStats(Long driverId, BigDecimal orderAmount) {
        Driver driver = getDriverById(driverId);
        if (driver != null) {
            driver.setTodayOrders(driver.getTodayOrders() + 1);
            driver.setTotalOrders(driver.getTotalOrders() + 1);
            
            if (orderAmount != null) {
                driver.setTodayIncome(driver.getTodayIncome().add(orderAmount));
                driver.setTotalIncome(driver.getTotalIncome().add(orderAmount));
            }
            
            driver.setUpdateTime(LocalDateTime.now());
            driverRepository.save(driver);
        }
    }
}

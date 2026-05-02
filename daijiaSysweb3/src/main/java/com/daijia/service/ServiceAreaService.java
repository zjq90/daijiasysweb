package com.daijia.service;

import com.daijia.entity.ServiceArea;
import com.daijia.repository.DriverRepository;
import com.daijia.repository.OrderRepository;
import com.daijia.repository.ServiceAreaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 服务区域服务类
 * 提供服务区域相关的业务逻辑处理，包括增删改查、热力图数据生成等
 *
 * @author daijia
 * @version 1.0.0
 */
@Service
@Transactional
public class ServiceAreaService {

    private static final Logger logger = LoggerFactory.getLogger(ServiceAreaService.class);

    @Autowired
    private ServiceAreaRepository serviceAreaRepository;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private OrderRepository orderRepository;

    /**
     * 创建新服务区域
     *
     * @param serviceArea 服务区域对象
     * @return 创建后的服务区域对象
     */
    public ServiceArea createServiceArea(ServiceArea serviceArea) {
        serviceArea.setCreateTime(LocalDateTime.now());
        serviceArea.setUpdateTime(LocalDateTime.now());
        ServiceArea savedArea = serviceAreaRepository.save(serviceArea);
        logger.info("创建服务区域成功，区域名称：{}", savedArea.getAreaName());
        return savedArea;
    }

    /**
     * 根据ID查询服务区域
     *
     * @param id 服务区域ID
     * @return 服务区域对象
     */
    public ServiceArea getServiceAreaById(Long id) {
        return serviceAreaRepository.findById(id).orElse(null);
    }

    /**
     * 根据区域编码查询服务区域
     *
     * @param areaCode 区域编码
     * @return 服务区域对象
     */
    public ServiceArea getServiceAreaByCode(String areaCode) {
        return serviceAreaRepository.findByAreaCode(areaCode);
    }

    /**
     * 更新服务区域信息
     *
     * @param serviceArea 服务区域对象
     * @return 更新后的服务区域对象
     */
    public ServiceArea updateServiceArea(ServiceArea serviceArea) {
        serviceArea.setUpdateTime(LocalDateTime.now());
        ServiceArea updatedArea = serviceAreaRepository.save(serviceArea);
        logger.info("更新服务区域成功，区域名称：{}", updatedArea.getAreaName());
        return updatedArea;
    }

    /**
     * 删除服务区域
     *
     * @param id 服务区域ID
     * @return 是否删除成功
     */
    public boolean deleteServiceArea(Long id) {
        Optional<ServiceArea> areaOpt = serviceAreaRepository.findById(id);
        if (areaOpt.isPresent()) {
            serviceAreaRepository.delete(areaOpt.get());
            logger.info("删除服务区域成功，区域ID：{}", id);
            return true;
        }
        return false;
    }

    /**
     * 查询所有服务区域
     *
     * @return 服务区域列表
     */
    public List<ServiceArea> getAllServiceAreas() {
        return serviceAreaRepository.findAll();
    }

    /**
     * 查询所有活跃的服务区域
     *
     * @return 服务区域列表
     */
    public List<ServiceArea> getActiveServiceAreas() {
        return serviceAreaRepository.findActiveAreasOrderByHeat();
    }

    /**
     * 启用/禁用服务区域
     *
     * @param id     服务区域ID
     * @param active 是否启用
     * @return 更新后的服务区域对象
     */
    public ServiceArea toggleServiceArea(Long id, boolean active) {
        ServiceArea area = getServiceAreaById(id);
        if (area != null) {
            area.setStatus(active ? "ACTIVE" : "INACTIVE");
            area.setUpdateTime(LocalDateTime.now());
            return serviceAreaRepository.save(area);
        }
        return null;
    }

    /**
     * 同步更新服务区域的实时数据
     * 包括：在线司机数、当前订单数、热度等级
     */
    public void syncAreaStats() {
        List<ServiceArea> areas = getAllServiceAreas();
        
        for (ServiceArea area : areas) {
            // 统计在线司机数
            long onlineDrivers = driverRepository.countOnlineDriversByServiceArea(area.getId());
            area.setOnlineDrivers((int) onlineDrivers);
            
            // 统计当前订单数（进行中的订单）
            long currentOrders = orderRepository.countByServiceAreaId(area.getId());
            area.setCurrentOrders((int) currentOrders);
            
            // 计算热度等级（1-100）
            // 热度 = (当前订单数 * 50 + 在线司机数 * 30 + 今日订单数 * 20) / 3
            int heatLevel = calculateHeatLevel(area);
            area.setHeatLevel(heatLevel);
            
            area.setUpdateTime(LocalDateTime.now());
            serviceAreaRepository.save(area);
        }
        
        logger.info("同步服务区域统计数据完成");
    }

    /**
     * 计算服务区域热度等级
     *
     * @param area 服务区域
     * @return 热度等级（1-100）
     */
    private int calculateHeatLevel(ServiceArea area) {
        int currentOrders = area.getCurrentOrders() != null ? area.getCurrentOrders() : 0;
        int onlineDrivers = area.getOnlineDrivers() != null ? area.getOnlineDrivers() : 0;
        int todayOrders = area.getTodayOrders() != null ? area.getTodayOrders() : 0;
        
        // 综合评分计算
        double score = currentOrders * 0.5 + onlineDrivers * 0.3 + todayOrders * 0.2;
        
        // 归一化到1-100
        int heatLevel = (int) Math.min(100, Math.max(1, score * 10));
        
        return heatLevel;
    }

    /**
     * 获取热力图数据
     *
     * @return 热力图数据列表
     */
    public List<Map<String, Object>> getHeatMapData() {
        List<Map<String, Object>> heatMapData = new ArrayList<>();
        List<Object[]> data = serviceAreaRepository.getHeatMapData();
        
        for (Object[] row : data) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", row[0]);
            item.put("areaName", row[1]);
            item.put("heatLevel", row[2]);
            item.put("latitude", row[3]);
            item.put("longitude", row[4]);
            item.put("onlineDrivers", row[5]);
            item.put("currentOrders", row[6]);
            
            // 根据热度等级确定颜色
            int heatLevel = ((Number) row[2]).intValue();
            item.put("heatColor", getHeatColor(heatLevel));
            
            heatMapData.add(item);
        }
        
        return heatMapData;
    }

    /**
     * 根据热度等级获取颜色
     *
     * @param heatLevel 热度等级
     * @return RGB颜色值
     */
    private String getHeatColor(int heatLevel) {
        if (heatLevel >= 80) {
            return "rgba(255, 0, 0, 0.8)";  // 红色 - 高热度
        } else if (heatLevel >= 50) {
            return "rgba(255, 165, 0, 0.8)";  // 橙色 - 中热度
        } else if (heatLevel >= 20) {
            return "rgba(255, 255, 0, 0.8)";  // 黄色 - 低热度
        } else {
            return "rgba(0, 255, 0, 0.8)";  // 绿色 - 无热度
        }
    }

    /**
     * 获取服务区域统计信息
     *
     * @return 统计信息Map
     */
    public Map<String, Object> getAreaStats() {
        Map<String, Object> stats = new HashMap<>();
        
        long totalAreas = serviceAreaRepository.count();
        long activeAreas = serviceAreaRepository.findByStatusOrderBySortOrderAsc("ACTIVE").size();
        
        // 统计各城市的区域数量
        List<Object[]> cityStats = serviceAreaRepository.countAreasByCity();
        List<Map<String, Object>> cityData = new ArrayList<>();
        for (Object[] row : cityStats) {
            Map<String, Object> cityItem = new HashMap<>();
            cityItem.put("city", row[0]);
            cityItem.put("count", row[1]);
            cityData.add(cityItem);
        }
        
        stats.put("totalAreas", totalAreas);
        stats.put("activeAreas", activeAreas);
        stats.put("cityDistribution", cityData);
        
        return stats;
    }
}

package com.daijia.repository;

import com.daijia.entity.ServiceArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 服务区域数据访问层
 * 提供服务区域数据的增删改查及统计功能
 *
 * @author daijia
 * @version 1.0.0
 */
@Repository
public interface ServiceAreaRepository extends JpaRepository<ServiceArea, Long> {

    /**
     * 根据区域编码查询服务区域
     *
     * @param areaCode 区域编码
     * @return 服务区域对象
     */
    ServiceArea findByAreaCode(String areaCode);

    /**
     * 根据状态查询服务区域列表
     *
     * @param status 状态
     * @return 服务区域列表
     */
    List<ServiceArea> findByStatusOrderBySortOrderAsc(String status);

    /**
     * 根据城市查询服务区域列表
     *
     * @param city 城市
     * @return 服务区域列表
     */
    List<ServiceArea> findByCity(String city);

    /**
     * 查询所有活跃的服务区域，按热度排序
     *
     * @return 服务区域列表
     */
    @Query("SELECT s FROM ServiceArea s WHERE s.status = 'ACTIVE' ORDER BY s.heatLevel DESC, s.sortOrder ASC")
    List<ServiceArea> findActiveAreasOrderByHeat();

    /**
     * 统计各城市的服务区域数量
     *
     * @return 城市统计结果列表
     */
    @Query("SELECT s.city, COUNT(s) FROM ServiceArea s GROUP BY s.city")
    List<Object[]> countAreasByCity();

    /**
     * 获取服务区域热力图数据
     *
     * @return 热力图数据列表（区域ID, 区域名称, 热度, 纬度, 经度）
     */
    @Query("SELECT s.id, s.areaName, s.heatLevel, s.centerLatitude, s.centerLongitude, s.onlineDrivers, s.currentOrders FROM ServiceArea s WHERE s.status = 'ACTIVE'")
    List<Object[]> getHeatMapData();

    /**
     * 更新指定区域的热度等级
     *
     * @param id        区域ID
     * @param heatLevel 热度等级
     */
    @Modifying
    @Query("UPDATE ServiceArea s SET s.heatLevel = :heatLevel WHERE s.id = :id")
    void updateHeatLevel(@Param("id") Long id, @Param("heatLevel") Integer heatLevel);

    /**
     * 更新指定区域的在线司机数
     *
     * @param id            区域ID
     * @param onlineDrivers 在线司机数
     */
    @Modifying
    @Query("UPDATE ServiceArea s SET s.onlineDrivers = :onlineDrivers WHERE s.id = :id")
    void updateOnlineDrivers(@Param("id") Long id, @Param("onlineDrivers") Integer onlineDrivers);

    /**
     * 更新指定区域的当前订单数
     *
     * @param id            区域ID
     * @param currentOrders 当前订单数
     */
    @Modifying
    @Query("UPDATE ServiceArea s SET s.currentOrders = :currentOrders WHERE s.id = :id")
    void updateCurrentOrders(@Param("id") Long id, @Param("currentOrders") Integer currentOrders);
}

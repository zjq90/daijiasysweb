package com.daijia.repository;

import com.daijia.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 司机数据访问层
 * 提供司机数据的增删改查及统计功能
 *
 * @author daijia
 * @version 1.0.0
 */
@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {

    /**
     * 根据司机编号查询司机
     *
     * @param driverNo 司机编号
     * @return 司机对象
     */
    Driver findByDriverNo(String driverNo);

    /**
     * 根据手机号码查询司机
     *
     * @param phone 手机号码
     * @return 司机对象
     */
    Driver findByPhone(String phone);

    /**
     * 根据状态查询司机列表
     *
     * @param status 司机状态
     * @return 司机列表
     */
    List<Driver> findByStatus(String status);

    /**
     * 根据服务区域ID查询司机列表
     *
     * @param serviceAreaId 服务区域ID
     * @return 司机列表
     */
    List<Driver> findByServiceAreaId(Long serviceAreaId);

    /**
     * 查询在线司机（状态为ONLINE或IN_SERVICE）
     *
     * @return 司机列表
     */
    @Query("SELECT d FROM Driver d WHERE d.status IN ('ONLINE', 'IN_SERVICE')")
    List<Driver> findOnlineDrivers();

    /**
     * 统计各状态的司机数量
     *
     * @return 状态统计结果列表
     */
    @Query("SELECT d.status, COUNT(d) FROM Driver d GROUP BY d.status")
    List<Object[]> countDriversByStatus();

    /**
     * 统计指定服务区域的司机数量
     *
     * @param serviceAreaId 服务区域ID
     * @return 司机数量
     */
    long countByServiceAreaId(Long serviceAreaId);

    /**
     * 统计指定服务区域的在线司机数量
     *
     * @param serviceAreaId 服务区域ID
     * @return 在线司机数量
     */
    @Query("SELECT COUNT(d) FROM Driver d WHERE d.serviceAreaId = :serviceAreaId AND d.status IN ('ONLINE', 'IN_SERVICE')")
    long countOnlineDriversByServiceArea(@Param("serviceAreaId") Long serviceAreaId);

    /**
     * 统计指定服务区域各状态的司机数量
     *
     * @param serviceAreaId 服务区域ID
     * @return 状态统计结果列表
     */
    @Query("SELECT d.status, COUNT(d) FROM Driver d WHERE d.serviceAreaId = :serviceAreaId GROUP BY d.status")
    List<Object[]> countDriversByStatusAndServiceArea(@Param("serviceAreaId") Long serviceAreaId);

    /**
     * 查询评分最高的司机列表
     *
     * @return 司机列表
     */
    @Query("SELECT d FROM Driver d ORDER BY d.rating DESC, d.totalOrders DESC")
    List<Driver> findTopDrivers();

    /**
     * 查询指定服务区域的在线司机位置信息
     *
     * @param serviceAreaId 服务区域ID
     * @return 司机位置信息列表（ID, 姓名, 状态, 纬度, 经度）
     */
    @Query("SELECT d.id, d.name, d.status, d.currentLatitude, d.currentLongitude FROM Driver d WHERE d.serviceAreaId = :serviceAreaId AND d.status IN ('ONLINE', 'IN_SERVICE')")
    List<Object[]> findDriverLocationsByServiceArea(@Param("serviceAreaId") Long serviceAreaId);
}

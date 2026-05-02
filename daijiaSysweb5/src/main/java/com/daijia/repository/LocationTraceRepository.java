package com.daijia.repository;

import com.daijia.entity.LocationTrace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 位置跟踪数据访问接口
 * 
 * 用于实时跟踪司机位置，支持地图导航显示
 * 
 * @author daijia
 * @version 1.0.0
 */
@Repository
public interface LocationTraceRepository extends JpaRepository<LocationTrace, Long> {

    List<LocationTrace> findByOrderIdOrderByLocationTimeAsc(Long orderId);

    List<LocationTrace> findByOrderIdOrderByLocationTimeDesc(Long orderId);

    List<LocationTrace> findByDriverIdOrderByLocationTimeDesc(Long driverId);

    @Query("SELECT lt FROM LocationTrace lt WHERE lt.orderId = ?1 ORDER BY lt.locationTime DESC")
    List<LocationTrace> findLatestByOrderId(Long orderId);

    @Query("SELECT lt FROM LocationTrace lt WHERE lt.orderId = ?1 ORDER BY lt.locationTime DESC")
    List<LocationTrace> findFirstByOrderIdOrderByLocationTimeDesc(Long orderId);

    @Query("SELECT lt FROM LocationTrace lt WHERE lt.orderId = ?1 AND lt.locationTime BETWEEN ?2 AND ?3 ORDER BY lt.locationTime ASC")
    List<LocationTrace> findByOrderIdAndTimeRange(Long orderId, LocalDateTime startTime, LocalDateTime endTime);

    @Query("SELECT lt FROM LocationTrace lt WHERE lt.driverId = ?1 ORDER BY lt.locationTime DESC")
    List<LocationTrace> findLatestByDriverId(Long driverId);

    void deleteByOrderId(Long orderId);
}

package com.daijia.repository;

import com.daijia.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 司机数据访问接口
 * 
 * @author daijia
 * @version 1.0.0
 */
@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {

    Optional<Driver> findByUsername(String username);

    Optional<Driver> findByPhone(String phone);

    List<Driver> findByStatus(Integer status);

    List<Driver> findByOnlineStatus(Integer onlineStatus);

    List<Driver> findByStatusAndOnlineStatus(Integer status, Integer onlineStatus);

    boolean existsByUsername(String username);

    boolean existsByIdCard(String idCard);

    @Query("SELECT d FROM Driver d WHERE d.realName LIKE %?1% OR d.phone LIKE %?1% OR d.carPlate LIKE %?1%")
    List<Driver> search(String keyword);

    @Query("SELECT d FROM Driver d WHERE d.status = 1 AND d.onlineStatus = 1")
    List<Driver> findAvailableDrivers();
}

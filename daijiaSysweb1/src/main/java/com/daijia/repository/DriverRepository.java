
package com.daijia.repository;

import com.daijia.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {
    Optional<Driver> findByUsername(String username);
    Optional<Driver> findByPhone(String phone);
    boolean existsByUsername(String username);
    
    List<Driver> findByStatusAndOnlineStatus(Integer status, Integer onlineStatus);
    
    @Query("SELECT d FROM Driver d WHERE d.status = 1 AND d.onlineStatus = 1")
    List<Driver> findAvailableDrivers();
}

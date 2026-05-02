package com.daijia.repository;

import com.daijia.entity.DriverSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DriverSettingRepository extends JpaRepository<DriverSetting, Long> {
    
    Optional<DriverSetting> findByDriverId(Long driverId);
    
    boolean existsByDriverId(Long driverId);
}

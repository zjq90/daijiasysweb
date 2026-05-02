package com.daijia.repository;

import com.daijia.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {
    
    Optional<Driver> findByPhone(String phone);
    
    Optional<Driver> findByPhoneAndDeleted(String phone, Integer deleted);
    
    Optional<Driver> findByIdAndDeleted(Long id, Integer deleted);
    
    Optional<Driver> findByLoginToken(String loginToken);
    
    boolean existsByPhone(String phone);
    
    List<Driver> findByOnlineStatusAndDeleted(Integer onlineStatus, Integer deleted);
}

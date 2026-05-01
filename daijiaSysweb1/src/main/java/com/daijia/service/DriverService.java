
package com.daijia.service;

import com.daijia.entity.Driver;
import com.daijia.repository.DriverRepository;
import com.daijia.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DriverService {
    
    @Autowired
    private DriverRepository driverRepository;
    
    public List<Driver> findAll() {
        return driverRepository.findAll();
    }
    
    public Optional<Driver> findById(Long id) {
        return driverRepository.findById(id);
    }
    
    public Optional<Driver> findByUsername(String username) {
        return driverRepository.findByUsername(username);
    }
    
    public List<Driver> findAvailableDrivers() {
        return driverRepository.findAvailableDrivers();
    }
    
    @Transactional
    public Driver save(Driver driver) {
        if (driver.getId() == null) {
            if (driverRepository.existsByUsername(driver.getUsername())) {
                throw new RuntimeException("用户名已存在");
            }
            driver.setPassword(MD5Util.md5(driver.getPassword()));
        } else {
            Optional<Driver> existing = driverRepository.findById(driver.getId());
            if (existing.isPresent()) {
                if (!existing.get().getPassword().equals(driver.getPassword())) {
                    driver.setPassword(MD5Util.md5(driver.getPassword()));
                }
            }
        }
        return driverRepository.save(driver);
    }
    
    @Transactional
    public void deleteById(Long id) {
        driverRepository.deleteById(id);
    }
    
    public boolean existsByUsername(String username) {
        return driverRepository.existsByUsername(username);
    }
}

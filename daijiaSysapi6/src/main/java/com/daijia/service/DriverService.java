package com.daijia.service;

import cn.hutool.core.util.StrUtil;
import com.daijia.common.Constants;
import com.daijia.dto.LoginDto;
import com.daijia.entity.Driver;
import com.daijia.entity.DriverSetting;
import com.daijia.exception.BusinessException;
import com.daijia.repository.DriverRepository;
import com.daijia.repository.DriverSettingRepository;
import com.daijia.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DriverService {
    
    private final DriverRepository driverRepository;
    private final DriverSettingRepository driverSettingRepository;
    
    public Map<String, Object> login(LoginDto dto) {
        Driver driver = driverRepository.findByPhoneAndDeleted(dto.getPhone(), Constants.DELETED_NO)
                .orElseGet(() -> {
                    Driver newDriver = new Driver();
                    newDriver.setPhone(dto.getPhone());
                    newDriver.setNickname("司机" + dto.getPhone().substring(7));
                    newDriver.setStatus(Constants.STATUS_ENABLED);
                    return driverRepository.save(newDriver);
                });
        
        if ("PASSWORD".equals(dto.getLoginType())) {
            if (StrUtil.isBlank(driver.getPassword())) {
                throw new BusinessException("请先设置密码或使用验证码登录");
            }
            String inputPasswordMd5 = CommonUtil.md5(dto.getPassword() != null ? dto.getPassword() : "");
            if (!driver.getPassword().equals(inputPasswordMd5)) {
                throw new BusinessException("密码错误");
            }
        }
        
        String token = CommonUtil.generateToken();
        driver.setLoginToken(token);
        driverRepository.save(driver);
        
        if (!driverSettingRepository.existsByDriverId(driver.getId())) {
            DriverSetting setting = new DriverSetting();
            setting.setDriverId(driver.getId());
            driverSettingRepository.save(setting);
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("driverId", driver.getId());
        result.put("phone", driver.getPhone());
        result.put("nickname", driver.getNickname());
        result.put("avatar", driver.getAvatar());
        result.put("isVerified", driver.getIsVerified());
        result.put("creditRating", driver.getCreditRating());
        result.put("onlineStatus", driver.getOnlineStatus());
        return result;
    }
    
    @Transactional
    public void logout(Long driverId) {
        Driver driver = getDriverById(driverId);
        driver.setLoginToken(null);
        driver.setOnlineStatus(Constants.ONLINE_STATUS_OFFLINE);
        driverRepository.save(driver);
    }
    
    public Driver getDriverById(Long driverId) {
        return driverRepository.findByIdAndDeleted(driverId, Constants.DELETED_NO)
                .orElseThrow(() -> new BusinessException("司机不存在"));
    }
    
    public Driver getDriverByToken(String token) {
        if (StrUtil.isBlank(token)) {
            throw new BusinessException("请先登录");
        }
        return driverRepository.findByLoginToken(token)
                .orElseThrow(() -> new BusinessException("登录已过期，请重新登录"));
    }
    
    @Transactional
    public Driver updateProfile(Long driverId, Map<String, Object> params) {
        Driver driver = getDriverById(driverId);
        
        if (params.containsKey("nickname")) {
            driver.setNickname((String) params.get("nickname"));
        }
        if (params.containsKey("avatar")) {
            driver.setAvatar((String) params.get("avatar"));
        }
        if (params.containsKey("realName")) {
            driver.setRealName((String) params.get("realName"));
        }
        if (params.containsKey("idCard")) {
            driver.setIdCard((String) params.get("idCard"));
        }
        if (params.containsKey("driverLicenseNo")) {
            driver.setDriverLicenseNo((String) params.get("driverLicenseNo"));
        }
        if (params.containsKey("driverLicenseType")) {
            driver.setDriverLicenseType((String) params.get("driverLicenseType"));
        }
        if (params.containsKey("isVerified")) {
            driver.setIsVerified((Integer) params.get("isVerified"));
        }
        if (params.containsKey("password")) {
            driver.setPassword(CommonUtil.md5((String) params.get("password")));
        }
        
        return driverRepository.save(driver);
    }
    
    @Transactional
    public Driver updateOnlineStatus(Long driverId, Integer onlineStatus) {
        Driver driver = getDriverById(driverId);
        driver.setOnlineStatus(onlineStatus);
        return driverRepository.save(driver);
    }
    
    @Transactional
    public Driver updateLocation(Long driverId, Double lat, Double lng) {
        Driver driver = getDriverById(driverId);
        driver.setCurrentLat(lat);
        driver.setCurrentLng(lng);
        return driverRepository.save(driver);
    }
    
    public DriverSetting getDriverSetting(Long driverId) {
        return driverSettingRepository.findByDriverId(driverId)
                .orElseGet(() -> {
                    DriverSetting setting = new DriverSetting();
                    setting.setDriverId(driverId);
                    return driverSettingRepository.save(setting);
                });
    }
    
    @Transactional
    public DriverSetting updateDriverSetting(Long driverId, DriverSetting setting) {
        DriverSetting existSetting = getDriverSetting(driverId);
        
        if (setting.getDailyOrderLimit() != null) {
            existSetting.setDailyOrderLimit(setting.getDailyOrderLimit());
        }
        if (setting.getContinuousWorkHours() != null) {
            existSetting.setContinuousWorkHours(setting.getContinuousWorkHours());
        }
        if (setting.getRestReminderMinutes() != null) {
            existSetting.setRestReminderMinutes(setting.getRestReminderMinutes());
        }
        if (setting.getEnableRestReminder() != null) {
            existSetting.setEnableRestReminder(setting.getEnableRestReminder());
        }
        if (setting.getEnableOrderPush() != null) {
            existSetting.setEnableOrderPush(setting.getEnableOrderPush());
        }
        if (setting.getMaxDistance() != null) {
            existSetting.setMaxDistance(setting.getMaxDistance());
        }
        if (setting.getVehicleTypes() != null) {
            existSetting.setVehicleTypes(setting.getVehicleTypes());
        }
        if (setting.getServiceTypes() != null) {
            existSetting.setServiceTypes(setting.getServiceTypes());
        }
        
        return driverSettingRepository.save(existSetting);
    }
}

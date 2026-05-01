
package com.daijia.config;

import com.daijia.entity.AppUser;
import com.daijia.entity.Driver;
import com.daijia.entity.SysUser;
import com.daijia.service.AppUserService;
import com.daijia.service.DriverService;
import com.daijia.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private SysUserService sysUserService;
    
    @Autowired
    private AppUserService appUserService;
    
    @Autowired
    private DriverService driverService;
    
    @Override
    public void run(String... args) throws Exception {
        initSysUsers();
        initAppUsers();
        initDrivers();
    }
    
    private void initSysUsers() {
        if (sysUserService.findAll().isEmpty()) {
            SysUser admin = new SysUser();
            admin.setUsername("admin");
            admin.setPassword("123456");
            admin.setRealName("系统管理员");
            admin.setPhone("13800000001");
            admin.setRole("ADMIN");
            admin.setStatus(1);
            sysUserService.save(admin);
            
            SysUser operator = new SysUser();
            operator.setUsername("operator");
            operator.setPassword("123456");
            operator.setRealName("操作员");
            operator.setPhone("13800000002");
            operator.setRole("OPERATOR");
            operator.setStatus(1);
            sysUserService.save(operator);
        }
    }
    
    private void initAppUsers() {
        if (appUserService.findAll().isEmpty()) {
            for (int i = 1; i <= 5; i++) {
                AppUser user = new AppUser();
                user.setUsername("user" + i);
                user.setPassword("123456");
                user.setRealName("用户" + i);
                user.setPhone("1390000000" + i);
                user.setIdCard("1101011990010" + String.format("%02d", i));
                user.setBalance(new BigDecimal(1000 * i));
                user.setIsRealName(i <= 3 ? 1 : 0);
                user.setIsBindCard(i <= 2 ? 1 : 0);
                user.setStatus(1);
                appUserService.save(user);
            }
        }
    }
    
    private void initDrivers() {
        if (driverService.findAll().isEmpty()) {
            String[] locations = {
                "39.9042,116.4074",
                "39.9142,116.4174",
                "39.9242,116.3974",
                "39.8942,116.4274",
                "39.9342,116.3874",
                "39.8842,116.4374"
            };
            
            for (int i = 1; i <= 6; i++) {
                Driver driver = new Driver();
                driver.setUsername("driver" + i);
                driver.setPassword("123456");
                driver.setRealName("司机" + i);
                driver.setPhone("1370000000" + i);
                driver.setIdCard("1101011989010" + String.format("%02d", i));
                driver.setLicenseNumber("A1234567" + i);
                driver.setLicenseType("C1");
                driver.setBalance(new BigDecimal(500 * i));
                driver.setCreditRating(new BigDecimal(String.valueOf(4.0 + i * 0.2)));
                driver.setIsRealName(1);
                driver.setIsBindCard(i <= 4 ? 1 : 0);
                driver.setStatus(1);
                driver.setOnlineStatus(i <= 4 ? 1 : 0);
                
                String[] loc = locations[i - 1].split(",");
                driver.setLatitude(new BigDecimal(loc[0]));
                driver.setLongitude(new BigDecimal(loc[1]));
                
                driverService.save(driver);
            }
        }
    }
}

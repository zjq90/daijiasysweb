package com.daijia.init;

import com.daijia.common.Constants;
import com.daijia.entity.*;
import com.daijia.repository.*;
import com.daijia.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    
    private final ClientRepository clientRepository;
    private final DriverRepository driverRepository;
    private final PromotionRepository promotionRepository;
    private final DriverSettingRepository driverSettingRepository;
    
    @Override
    public void run(String... args) {
        if (clientRepository.count() == 0) {
            initTestData();
        }
    }
    
    private void initTestData() {
        log.info("开始初始化测试数据...");
        
        Client client1 = new Client();
        client1.setPhone("13800138001");
        client1.setPassword(CommonUtil.md5("123456"));
        client1.setNickname("张三");
        client1.setRealName("张三");
        client1.setIdCard("110101199001011234");
        client1.setIsVerified(Constants.VERIFIED_YES);
        client1.setCreditRating(new BigDecimal("4.8"));
        client1.setOrderCount(15);
        client1.setStatus(Constants.STATUS_ENABLED);
        clientRepository.save(client1);
        
        Client client2 = new Client();
        client2.setPhone("13800138002");
        client2.setPassword(CommonUtil.md5("123456"));
        client2.setNickname("李四");
        client2.setIsVerified(Constants.VERIFIED_NO);
        client2.setCreditRating(new BigDecimal("4.5"));
        client2.setStatus(Constants.STATUS_ENABLED);
        clientRepository.save(client2);
        
        Driver driver1 = new Driver();
        driver1.setPhone("13900139001");
        driver1.setPassword(CommonUtil.md5("123456"));
        driver1.setNickname("王师傅");
        driver1.setRealName("王五");
        driver1.setIdCard("110101198501015678");
        driver1.setDriverLicenseNo("A12345678");
        driver1.setDriverLicenseType("C1");
        driver1.setIsVerified(Constants.VERIFIED_YES);
        driver1.setCreditRating(new BigDecimal("4.9"));
        driver1.setOrderCount(128);
        driver1.setOnlineStatus(Constants.ONLINE_STATUS_ONLINE);
        driver1.setCurrentLat(39.9042);
        driver1.setCurrentLng(116.4074);
        driver1.setStatus(Constants.STATUS_ENABLED);
        driverRepository.save(driver1);
        
        DriverSetting setting1 = new DriverSetting();
        setting1.setDriverId(driver1.getId());
        setting1.setDailyOrderLimit(15);
        setting1.setContinuousWorkHours(4);
        setting1.setRestReminderMinutes(30);
        setting1.setEnableRestReminder(1);
        setting1.setEnableOrderPush(1);
        setting1.setMaxDistance(10.0);
        driverSettingRepository.save(setting1);
        
        Driver driver2 = new Driver();
        driver2.setPhone("13900139002");
        driver2.setPassword(CommonUtil.md5("123456"));
        driver2.setNickname("赵师傅");
        driver2.setRealName("赵六");
        driver2.setIdCard("110101198801019012");
        driver2.setDriverLicenseNo("B87654321");
        driver2.setDriverLicenseType("B2");
        driver2.setIsVerified(Constants.VERIFIED_YES);
        driver2.setCreditRating(new BigDecimal("4.7"));
        driver2.setOrderCount(86);
        driver2.setOnlineStatus(Constants.ONLINE_STATUS_ONLINE);
        driver2.setCurrentLat(39.9142);
        driver2.setCurrentLng(116.4174);
        driver2.setStatus(Constants.STATUS_ENABLED);
        driverRepository.save(driver2);
        
        DriverSetting setting2 = new DriverSetting();
        setting2.setDriverId(driver2.getId());
        setting2.setDailyOrderLimit(10);
        setting2.setEnableOrderPush(1);
        driverSettingRepository.save(setting2);
        
        Promotion promo1 = new Promotion();
        promo1.setTitle("新用户专享立减10元");
        promo1.setDescription("新用户首单立减10元");
        promo1.setType(Constants.PROMOTION_TYPE_FULL_REDUCTION);
        promo1.setTargetUser(Constants.TARGET_USER_NEW);
        promo1.setStartTime(LocalDateTime.now().minusDays(1));
        promo1.setEndTime(LocalDateTime.now().plusDays(30));
        promo1.setDiscountAmount(new BigDecimal("10.00"));
        promo1.setMinOrderAmount(new BigDecimal("20.00"));
        promo1.setTotalCount(1000);
        promo1.setUsedCount(0);
        promo1.setStatus(Constants.PROMOTION_STATUS_ACTIVE);
        promotionRepository.save(promo1);
        
        Promotion promo2 = new Promotion();
        promo2.setTitle("限时8折优惠");
        promo2.setDescription("所有用户享受8折优惠，最高减免20元");
        promo2.setType(Constants.PROMOTION_TYPE_DISCOUNT);
        promo2.setTargetUser(Constants.TARGET_USER_ALL);
        promo2.setStartTime(LocalDateTime.now().minusDays(1));
        promo2.setEndTime(LocalDateTime.now().plusDays(7));
        promo2.setDiscountRate(new BigDecimal("0.8"));
        promo2.setStatus(Constants.PROMOTION_STATUS_ACTIVE);
        promotionRepository.save(promo2);
        
        Promotion promo3 = new Promotion();
        promo3.setTitle("司机冲单奖励");
        promo3.setDescription("每日完成10单奖励50元");
        promo3.setType(Constants.PROMOTION_TYPE_CASHBACK);
        promo3.setTargetUser(Constants.TARGET_USER_DRIVER);
        promo3.setStartTime(LocalDateTime.now().minusDays(1));
        promo3.setEndTime(LocalDateTime.now().plusDays(30));
        promo3.setStatus(Constants.PROMOTION_STATUS_ACTIVE);
        promotionRepository.save(promo3);
        
        log.info("测试数据初始化完成！");
        log.info("========================================");
        log.info("测试账号信息：");
        log.info("客户端用户1: 手机号=13800138001, 密码=123456");
        log.info("客户端用户2: 手机号=13800138002, 密码=123456");
        log.info("司机端用户1: 手机号=13900139001, 密码=123456");
        log.info("司机端用户2: 手机号=13900139002, 密码=123456");
        log.info("========================================");
        log.info("API文档地址: http://localhost:8080/doc.html");
        log.info("H2控制台地址: http://localhost:8080/h2-console");
        log.info("========================================");
    }
}

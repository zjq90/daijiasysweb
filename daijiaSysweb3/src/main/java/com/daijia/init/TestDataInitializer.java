package com.daijia.init;

import com.daijia.entity.*;
import com.daijia.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Random;

/**
 * 测试数据初始化类
 * 应用启动时自动生成测试数据
 *
 * @author daijia
 * @version 1.0.0
 */
@Component
public class TestDataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(TestDataInitializer.class);

    @Autowired
    private DriverService driverService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ServiceAreaService serviceAreaService;

    @Autowired
    private OperationReportService operationReportService;

    @Autowired
    private ComplaintService complaintService;

    @Autowired
    private IncentiveActivityService incentiveActivityService;

    @Autowired
    private PromotionActivityService promotionActivityService;

    private Random random = new Random();

    @Override
    public void run(String... args) throws Exception {
        logger.info("开始初始化测试数据...");
        
        // 1. 创建服务区域数据
        createServiceAreas();
        
        // 2. 创建司机数据
        createDrivers();
        
        // 3. 创建订单数据
        createOrders();
        
        // 4. 生成运营报表数据
        createOperationReports();
        
        // 5. 创建投诉数据
        createComplaints();
        
        // 6. 创建激励奖励活动数据
        createIncentiveActivities();
        
        // 7. 创建优惠活动数据
        createPromotionActivities();
        
        logger.info("测试数据初始化完成！");
    }

    /**
     * 创建服务区域测试数据
     */
    private void createServiceAreas() {
        logger.info("创建服务区域数据...");
        
        String[] areas = {
            "朝阳区", "海淀区", "东城区", "西城区", "丰台区",
            "浦东新区", "黄浦区", "静安区", "徐汇区", "长宁区",
            "天河区", "越秀区", "海珠区", "白云区", "荔湾区"
        };
        
        String[] cities = {"北京市", "上海市", "广州市"};
        String[] provinces = {"北京市", "上海市", "广东省"};
        
        // 北京区域坐标
        double[][] beijingCoords = {
            {39.95, 116.48}, {39.96, 116.32}, {39.93, 116.42},
            {39.92, 116.37}, {39.86, 116.28}
        };
        
        // 上海区域坐标
        double[][] shanghaiCoords = {
            {31.23, 121.54}, {31.24, 121.49}, {31.23, 121.46},
            {31.19, 121.44}, {31.22, 121.42}
        };
        
        // 广州区域坐标
        double[][] guangzhouCoords = {
            {23.13, 113.36}, {23.14, 113.27}, {23.10, 113.33},
            {23.17, 113.27}, {23.13, 113.24}
        };
        
        for (int i = 0; i < areas.length; i++) {
            ServiceArea area = new ServiceArea();
            area.setAreaName(areas[i]);
            area.setAreaCode("AREA" + String.format("%03d", i + 1));
            
            // 分配城市
            int cityIndex;
            double[][] coords;
            if (i < 5) {
                cityIndex = 0;
                coords = beijingCoords;
            } else if (i < 10) {
                cityIndex = 1;
                coords = shanghaiCoords;
            } else {
                cityIndex = 2;
                coords = guangzhouCoords;
            }
            
            area.setProvince(provinces[cityIndex]);
            area.setCity(cities[cityIndex]);
            
            // 设置坐标
            int coordIndex = i % 5;
            area.setCenterLatitude(new BigDecimal(coords[coordIndex][0]));
            area.setCenterLongitude(new BigDecimal(coords[coordIndex][1]));
            area.setRadius(new BigDecimal("5.0"));
            
            // 随机热度
            area.setHeatLevel(random.nextInt(100));
            area.setOnlineDrivers(random.nextInt(20));
            area.setCurrentOrders(random.nextInt(30));
            area.setTodayOrders(random.nextInt(100));
            area.setTodayAmount(new BigDecimal(random.nextInt(10000)));
            area.setSortOrder(i + 1);
            
            serviceAreaService.createServiceArea(area);
        }
        
        logger.info("已创建 {} 个服务区域数据", areas.length);
    }

    /**
     * 创建司机测试数据
     */
    private void createDrivers() {
        logger.info("创建司机数据...");
        
        String[] names = {
            "张三", "李四", "王五", "赵六", "钱七",
            "孙八", "周九", "吴十", "郑十一", "王十二",
            "陈十三", "褚十四", "卫十五", "蒋十六", "沈十七",
            "韩十八", "杨十九", "朱二十", "秦二十一", "尤二十二"
        };
        
        String[] phones = {"138", "139", "137", "136", "135", "188", "189", "187", "186", "185"};
        String[] statuses = {"ONLINE", "ONLINE", "ONLINE", "IN_SERVICE", "OFFLINE"};
        
        // 北京坐标范围
        double[] latRange = {39.8, 40.1};
        double[] lngRange = {116.2, 116.6};
        
        for (int i = 0; i < names.length; i++) {
            Driver driver = new Driver();
            driver.setName(names[i]);
            driver.setPhone(phones[random.nextInt(phones.length)] + 
                String.format("%08d", random.nextInt(100000000)));
            driver.setIdCard("1101011990" + 
                String.format("%02d%02d%04d", 
                    random.nextInt(12) + 1, 
                    random.nextInt(28) + 1, 
                    random.nextInt(10000)));
            driver.setLicenseNo("A1" + String.format("%010d", random.nextInt(1000000000)));
            
            // 设置状态
            driver.setStatus(statuses[random.nextInt(statuses.length)]);
            
            // 设置位置
            double lat = latRange[0] + random.nextDouble() * (latRange[1] - latRange[0]);
            double lng = lngRange[0] + random.nextDouble() * (lngRange[1] - lngRange[0]);
            driver.setCurrentLatitude(new BigDecimal(String.format("%.6f", lat)));
            driver.setCurrentLongitude(new BigDecimal(String.format("%.6f", lng)));
            
            // 设置统计数据
            driver.setRating(new BigDecimal("4." + random.nextInt(10)));
            driver.setTotalOrders(50 + random.nextInt(500));
            driver.setTotalIncome(new BigDecimal(10000 + random.nextInt(50000)));
            driver.setTodayOrders(random.nextInt(20));
            driver.setTodayIncome(new BigDecimal(random.nextInt(500)));
            
            // 设置注册时间
            driver.setRegisterTime(LocalDateTime.now().minusDays(random.nextInt(365)));
            
            if ("ONLINE".equals(driver.getStatus()) || "IN_SERVICE".equals(driver.getStatus())) {
                driver.setOnlineTime(LocalDateTime.now().minusHours(random.nextInt(12)));
            }
            
            driver.setServiceAreaId(1L + random.nextInt(5));
            
            driverService.createDriver(driver);
        }
        
        logger.info("已创建 {} 个司机数据", names.length);
    }

    /**
     * 创建订单测试数据
     */
    private void createOrders() {
        logger.info("创建订单数据...");
        
        String[] locations = {
            "北京市朝阳区建国路88号",
            "北京市海淀区中关村大街1号",
            "北京市东城区王府井大街",
            "北京市西城区西单北大街",
            "北京市丰台区北京西站",
            "北京市朝阳区三里屯",
            "北京市海淀区五道口",
            "北京市朝阳区国贸中心",
            "北京市海淀区颐和园",
            "北京市东城区天安门"
        };
        
        String[] statuses = {
            "PENDING_WAITING", "ACCEPTED", "IN_SERVICE", "COMPLETED", "COMPLETED", "COMPLETED", "CANCELLED"
        };
        
        for (int i = 0; i < 50; i++) {
            Order order = new Order();
            order.setPassengerId(1000L + random.nextInt(100));
            
            // 随机分配司机（非待接单状态）
            String status = statuses[random.nextInt(statuses.length)];
            order.setStatus(status);
            
            if (!"PENDING_WAITING".equals(status)) {
                order.setDriverId(1L + random.nextInt(20));
                order.setAcceptTime(LocalDateTime.now().minusMinutes(random.nextInt(60)));
            }
            
            if ("IN_SERVICE".equals(status) || "COMPLETED".equals(status)) {
                order.setStartTime(LocalDateTime.now().minusMinutes(random.nextInt(120)));
            }
            
            if ("COMPLETED".equals(status)) {
                order.setCompleteTime(LocalDateTime.now().minusMinutes(random.nextInt(60)));
            }
            
            if ("CANCELLED".equals(status)) {
                order.setCancelTime(LocalDateTime.now().minusMinutes(random.nextInt(30)));
                order.setCancelReason("乘客主动取消");
            }
            
            // 设置地址
            int startIndex = random.nextInt(locations.length);
            int endIndex;
            do {
                endIndex = random.nextInt(locations.length);
            } while (endIndex == startIndex);
            
            order.setStartLocation(locations[startIndex]);
            order.setEndLocation(locations[endIndex]);
            
            // 设置坐标
            double[] startCoords = generateRandomCoords();
            double[] endCoords = generateRandomCoords();
            order.setStartLatitude(new BigDecimal(String.format("%.6f", startCoords[0])));
            order.setStartLongitude(new BigDecimal(String.format("%.6f", startCoords[1])));
            order.setEndLatitude(new BigDecimal(String.format("%.6f", endCoords[0])));
            order.setEndLongitude(new BigDecimal(String.format("%.6f", endCoords[1])));
            
            // 设置金额
            double distance = 5 + random.nextDouble() * 25;
            order.setEstimatedDistance(new BigDecimal(String.format("%.1f", distance)));
            
            double amount = 20 + distance * 3;
            order.setEstimatedAmount(new BigDecimal(String.format("%.2f", amount)));
            
            if ("COMPLETED".equals(status)) {
                order.setActualAmount(new BigDecimal(String.format("%.2f", amount * (0.9 + random.nextDouble() * 0.2))));
            }
            
            // 设置订单时间
            order.setOrderTime(LocalDateTime.now().minusMinutes(random.nextInt(1440)));
            
            // 设置评价（完成订单可能有评价）
            if ("COMPLETED".equals(status) && random.nextBoolean()) {
                order.setRating(3 + random.nextInt(3));
                order.setComment(random.nextBoolean() ? "服务很好，司机很专业！" : "准时到达，推荐！");
            }
            
            order.setServiceAreaId(1L + random.nextInt(5));
            
            orderService.createOrder(order);
        }
        
        logger.info("已创建 50 个订单数据");
    }

    /**
     * 生成随机坐标（北京区域）
     */
    private double[] generateRandomCoords() {
        double[] latRange = {39.8, 40.1};
        double[] lngRange = {116.2, 116.6};
        double lat = latRange[0] + random.nextDouble() * (latRange[1] - latRange[0]);
        double lng = lngRange[0] + random.nextDouble() * (lngRange[1] - lngRange[0]);
        return new double[]{lat, lng};
    }

    /**
     * 创建运营报表测试数据
     */
    private void createOperationReports() {
        logger.info("创建运营报表数据...");
        
        operationReportService.generateTestReports(30);
        
        logger.info("已创建运营报表数据");
    }

    /**
     * 创建投诉测试数据
     */
    private void createComplaints() {
        logger.info("创建投诉数据...");
        
        complaintService.generateTestComplaints(20);
        
        logger.info("已创建投诉数据");
    }

    /**
     * 创建激励奖励活动测试数据
     */
    private void createIncentiveActivities() {
        logger.info("创建激励奖励活动数据...");
        
        incentiveActivityService.generateTestActivities(5);
        
        logger.info("已创建激励奖励活动数据");
    }

    /**
     * 创建优惠活动测试数据
     */
    private void createPromotionActivities() {
        logger.info("创建优惠活动数据...");
        
        promotionActivityService.generateTestActivities(5);
        
        logger.info("已创建优惠活动数据");
    }
}

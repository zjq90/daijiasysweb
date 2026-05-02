package com.daijia.service;

import com.daijia.entity.IncentiveActivity;
import com.daijia.repository.IncentiveActivityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 激励奖励活动服务类
 * 提供激励奖励活动相关的业务逻辑处理，包括增删改查、活动管理、统计分析等
 *
 * @author daijia
 * @version 1.0.0
 */
@Service
@Transactional
public class IncentiveActivityService {

    private static final Logger logger = LoggerFactory.getLogger(IncentiveActivityService.class);

    @Autowired
    private IncentiveActivityRepository incentiveActivityRepository;

    /**
     * 生成唯一活动编号
     * 格式：JL + 年月日时分秒 + 4位随机数
     *
     * @return 活动编号
     */
    public String generateActivityNo() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String random = String.format("%04d", new Random().nextInt(10000));
        return "JL" + timestamp + random;
    }

    /**
     * 创建新激励活动
     *
     * @param activity 活动对象
     * @return 创建后的活动对象
     */
    public IncentiveActivity createActivity(IncentiveActivity activity) {
        if (activity.getActivityNo() == null || activity.getActivityNo().isEmpty()) {
            activity.setActivityNo(generateActivityNo());
        }
        activity.setCreateTime(LocalDateTime.now());
        activity.setUpdateTime(LocalDateTime.now());
        IncentiveActivity savedActivity = incentiveActivityRepository.save(activity);
        logger.info("创建激励活动成功，活动编号：{}", savedActivity.getActivityNo());
        return savedActivity;
    }

    /**
     * 根据ID查询活动
     *
     * @param id 活动ID
     * @return 活动对象
     */
    public IncentiveActivity getActivityById(Long id) {
        return incentiveActivityRepository.findById(id).orElse(null);
    }

    /**
     * 根据活动编号查询活动
     *
     * @param activityNo 活动编号
     * @return 活动对象
     */
    public IncentiveActivity getActivityByNo(String activityNo) {
        return incentiveActivityRepository.findByActivityNo(activityNo);
    }

    /**
     * 更新活动信息
     *
     * @param activity 活动对象
     * @return 更新后的活动对象
     */
    public IncentiveActivity updateActivity(IncentiveActivity activity) {
        activity.setUpdateTime(LocalDateTime.now());
        IncentiveActivity updatedActivity = incentiveActivityRepository.save(activity);
        logger.info("更新激励活动成功，活动编号：{}", updatedActivity.getActivityNo());
        return updatedActivity;
    }

    /**
     * 删除活动
     *
     * @param id 活动ID
     * @return 是否删除成功
     */
    public boolean deleteActivity(Long id) {
        Optional<IncentiveActivity> activityOpt = incentiveActivityRepository.findById(id);
        if (activityOpt.isPresent()) {
            incentiveActivityRepository.delete(activityOpt.get());
            logger.info("删除激励活动成功，活动ID：{}", id);
            return true;
        }
        return false;
    }

    /**
     * 查询所有活动
     *
     * @return 活动列表
     */
    public List<IncentiveActivity> getAllActivities() {
        return incentiveActivityRepository.findAll();
    }

    /**
     * 发布活动
     *
     * @param activityId 活动ID
     * @return 更新后的活动对象
     */
    public IncentiveActivity publishActivity(Long activityId) {
        IncentiveActivity activity = getActivityById(activityId);
        if (activity != null && "DRAFT".equals(activity.getStatus())) {
            activity.setStatus("PENDING");
            activity.setPublishTime(LocalDateTime.now());
            return updateActivity(activity);
        }
        return null;
    }

    /**
     * 启动活动
     *
     * @param activityId 活动ID
     * @return 更新后的活动对象
     */
    public IncentiveActivity startActivity(Long activityId) {
        IncentiveActivity activity = getActivityById(activityId);
        if (activity != null && ("DRAFT".equals(activity.getStatus()) || "PENDING".equals(activity.getStatus()))) {
            activity.setStatus("ACTIVE");
            if (activity.getStartTime() == null) {
                activity.setStartTime(LocalDateTime.now());
            }
            return updateActivity(activity);
        }
        return null;
    }

    /**
     * 暂停活动
     *
     * @param activityId 活动ID
     * @return 更新后的活动对象
     */
    public IncentiveActivity pauseActivity(Long activityId) {
        IncentiveActivity activity = getActivityById(activityId);
        if (activity != null && "ACTIVE".equals(activity.getStatus())) {
            activity.setStatus("PAUSED");
            return updateActivity(activity);
        }
        return null;
    }

    /**
     * 恢复活动
     *
     * @param activityId 活动ID
     * @return 更新后的活动对象
     */
    public IncentiveActivity resumeActivity(Long activityId) {
        IncentiveActivity activity = getActivityById(activityId);
        if (activity != null && "PAUSED".equals(activity.getStatus())) {
            activity.setStatus("ACTIVE");
            return updateActivity(activity);
        }
        return null;
    }

    /**
     * 结束活动
     *
     * @param activityId 活动ID
     * @return 更新后的活动对象
     */
    public IncentiveActivity endActivity(Long activityId) {
        IncentiveActivity activity = getActivityById(activityId);
        if (activity != null && !"CANCELLED".equals(activity.getStatus())) {
            activity.setStatus("EXPIRED");
            activity.setEndTime(LocalDateTime.now());
            return updateActivity(activity);
        }
        return null;
    }

    /**
     * 取消活动
     *
     * @param activityId 活动ID
     * @return 更新后的活动对象
     */
    public IncentiveActivity cancelActivity(Long activityId) {
        IncentiveActivity activity = getActivityById(activityId);
        if (activity != null && !"EXPIRED".equals(activity.getStatus())) {
            activity.setStatus("CANCELLED");
            return updateActivity(activity);
        }
        return null;
    }

    /**
     * 获取进行中的活动列表
     *
     * @return 活动列表
     */
    public List<IncentiveActivity> getActiveActivities() {
        return incentiveActivityRepository.findActiveActivities(LocalDateTime.now());
    }

    /**
     * 获取指定司机适用的进行中活动
     *
     * @param driverId 司机ID
     * @return 活动列表
     */
    public List<IncentiveActivity> getApplicableActivitiesForDriver(Long driverId) {
        return incentiveActivityRepository.findApplicableActivitiesForDriver(driverId, LocalDateTime.now());
    }

    /**
     * 获取活动统计数据
     *
     * @return 统计数据Map
     */
    public Map<String, Object> getActivityStats() {
        Map<String, Object> stats = new HashMap<>();
        
        // 各状态活动数量
        List<Object[]> statusStats = incentiveActivityRepository.countActivitiesByStatus();
        Map<String, Long> statusMap = new HashMap<>();
        for (Object[] row : statusStats) {
            String status = (String) row[0];
            Long count = (Long) row[1];
            statusMap.put(status, count);
        }
        
        stats.put("total", incentiveActivityRepository.count());
        stats.put("draft", statusMap.getOrDefault("DRAFT", 0L));
        stats.put("pending", statusMap.getOrDefault("PENDING", 0L));
        stats.put("active", statusMap.getOrDefault("ACTIVE", 0L));
        stats.put("paused", statusMap.getOrDefault("PAUSED", 0L));
        stats.put("expired", statusMap.getOrDefault("EXPIRED", 0L));
        stats.put("cancelled", statusMap.getOrDefault("CANCELLED", 0L));
        
        // 各类型活动数量
        List<Object[]> typeStats = incentiveActivityRepository.countActivitiesByType();
        List<Map<String, Object>> typeData = new ArrayList<>();
        for (Object[] row : typeStats) {
            Map<String, Object> typeItem = new HashMap<>();
            typeItem.put("type", row[0]);
            typeItem.put("count", row[1]);
            typeData.add(typeItem);
        }
        stats.put("typeDistribution", typeData);
        
        // 活动执行统计
        Integer benefitedDrivers = incentiveActivityRepository.sumBenefitedDrivers();
        Integer benefitedOrders = incentiveActivityRepository.sumBenefitedOrders();
        
        stats.put("totalBenefitedDrivers", benefitedDrivers != null ? benefitedDrivers : 0);
        stats.put("totalBenefitedOrders", benefitedOrders != null ? benefitedOrders : 0);
        
        return stats;
    }

    /**
     * 根据活动类型查询活动
     *
     * @param activityType 活动类型
     * @return 活动列表
     */
    public List<IncentiveActivity> getActivitiesByType(String activityType) {
        return incentiveActivityRepository.findByActivityTypeOrderByCreateTimeDesc(activityType);
    }

    /**
     * 根据状态查询活动
     *
     * @param status 活动状态
     * @return 活动列表
     */
    public List<IncentiveActivity> getActivitiesByStatus(String status) {
        return incentiveActivityRepository.findByStatusOrderBySortOrderAsc(status);
    }

    /**
     * 更新活动使用统计
     *
     * @param activityId 活动ID
     * @param amount     奖励金额
     */
    public void updateActivityUsage(Long activityId, BigDecimal amount) {
        IncentiveActivity activity = getActivityById(activityId);
        if (activity != null) {
            if (activity.getUsedAmount() == null) {
                activity.setUsedAmount(BigDecimal.ZERO);
            }
            activity.setUsedAmount(activity.getUsedAmount().add(amount));
            
            if (activity.getBenefitedOrders() == null) {
                activity.setBenefitedOrders(0);
            }
            activity.setBenefitedOrders(activity.getBenefitedOrders() + 1);
            
            updateActivity(activity);
        }
    }

    /**
     * 生成测试激励活动数据
     *
     * @param count 数量
     */
    public void generateTestActivities(int count) {
        Random random = new Random();
        String[] types = {"ORDER_BONUS", "RUSH_HOUR_BONUS", "NEW_DRIVER_BONUS", "ATTENDANCE_BONUS", "AREA_BONUS"};
        String[] statuses = {"DRAFT", "PENDING", "ACTIVE", "EXPIRED"};
        String[] rewardTypes = {"FIXED_AMOUNT", "PER_ORDER", "PERCENTAGE", "TIERED"};
        
        for (int i = 0; i < count; i++) {
            IncentiveActivity activity = new IncentiveActivity();
            activity.setActivityName("测试激励活动" + (i + 1));
            activity.setActivityType(types[random.nextInt(types.length)]);
            activity.setDescription("这是一个测试激励活动，用于系统功能测试。");
            activity.setRewardType(rewardTypes[random.nextInt(rewardTypes.length)]);
            activity.setStatus(statuses[random.nextInt(statuses.length)]);
            activity.setBudgetAmount(new BigDecimal(5000 + random.nextInt(15000)));
            activity.setUsedAmount(new BigDecimal(random.nextInt(5000)));
            activity.setFixedAmount(new BigDecimal(10 + random.nextInt(100)));
            activity.setPerOrderAmount(new BigDecimal(5 + random.nextInt(50)));
            activity.setMinOrders(1 + random.nextInt(20));
            activity.setMaxAmount(new BigDecimal(500 + random.nextInt(1000)));
            
            // 设置时间
            LocalDateTime now = LocalDateTime.now();
            activity.setStartTime(now.minusDays(random.nextInt(30)));
            activity.setEndTime(now.plusDays(random.nextInt(30)));
            
            activity.setBenefitedDrivers(random.nextInt(100));
            activity.setBenefitedOrders(random.nextInt(500));
            activity.setSortOrder(i + 1);
            
            createActivity(activity);
        }
        
        logger.info("已生成{}条测试激励活动数据", count);
    }
}

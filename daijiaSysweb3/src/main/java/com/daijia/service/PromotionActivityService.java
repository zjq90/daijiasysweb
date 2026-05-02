package com.daijia.service;

import com.daijia.entity.PromotionActivity;
import com.daijia.repository.PromotionActivityRepository;
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
 * 优惠活动服务类
 * 提供优惠活动相关的业务逻辑处理，包括增删改查、活动管理、统计分析等
 *
 * @author daijia
 * @version 1.0.0
 */
@Service
@Transactional
public class PromotionActivityService {

    private static final Logger logger = LoggerFactory.getLogger(PromotionActivityService.class);

    @Autowired
    private PromotionActivityRepository promotionActivityRepository;

    /**
     * 生成唯一活动编号
     * 格式：YH + 年月日时分秒 + 4位随机数
     *
     * @return 活动编号
     */
    public String generateActivityNo() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String random = String.format("%04d", new Random().nextInt(10000));
        return "YH" + timestamp + random;
    }

    /**
     * 创建新优惠活动
     *
     * @param activity 活动对象
     * @return 创建后的活动对象
     */
    public PromotionActivity createActivity(PromotionActivity activity) {
        if (activity.getActivityNo() == null || activity.getActivityNo().isEmpty()) {
            activity.setActivityNo(generateActivityNo());
        }
        activity.setCreateTime(LocalDateTime.now());
        activity.setUpdateTime(LocalDateTime.now());
        PromotionActivity savedActivity = promotionActivityRepository.save(activity);
        logger.info("创建优惠活动成功，活动编号：{}", savedActivity.getActivityNo());
        return savedActivity;
    }

    /**
     * 根据ID查询活动
     *
     * @param id 活动ID
     * @return 活动对象
     */
    public PromotionActivity getActivityById(Long id) {
        return promotionActivityRepository.findById(id).orElse(null);
    }

    /**
     * 根据活动编号查询活动
     *
     * @param activityNo 活动编号
     * @return 活动对象
     */
    public PromotionActivity getActivityByNo(String activityNo) {
        return promotionActivityRepository.findByActivityNo(activityNo);
    }

    /**
     * 更新活动信息
     *
     * @param activity 活动对象
     * @return 更新后的活动对象
     */
    public PromotionActivity updateActivity(PromotionActivity activity) {
        activity.setUpdateTime(LocalDateTime.now());
        PromotionActivity updatedActivity = promotionActivityRepository.save(activity);
        logger.info("更新优惠活动成功，活动编号：{}", updatedActivity.getActivityNo());
        return updatedActivity;
    }

    /**
     * 删除活动
     *
     * @param id 活动ID
     * @return 是否删除成功
     */
    public boolean deleteActivity(Long id) {
        Optional<PromotionActivity> activityOpt = promotionActivityRepository.findById(id);
        if (activityOpt.isPresent()) {
            promotionActivityRepository.delete(activityOpt.get());
            logger.info("删除优惠活动成功，活动ID：{}", id);
            return true;
        }
        return false;
    }

    /**
     * 查询所有活动
     *
     * @return 活动列表
     */
    public List<PromotionActivity> getAllActivities() {
        return promotionActivityRepository.findAll();
    }

    /**
     * 发布活动
     *
     * @param activityId 活动ID
     * @return 更新后的活动对象
     */
    public PromotionActivity publishActivity(Long activityId) {
        PromotionActivity activity = getActivityById(activityId);
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
    public PromotionActivity startActivity(Long activityId) {
        PromotionActivity activity = getActivityById(activityId);
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
    public PromotionActivity pauseActivity(Long activityId) {
        PromotionActivity activity = getActivityById(activityId);
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
    public PromotionActivity resumeActivity(Long activityId) {
        PromotionActivity activity = getActivityById(activityId);
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
    public PromotionActivity endActivity(Long activityId) {
        PromotionActivity activity = getActivityById(activityId);
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
    public PromotionActivity cancelActivity(Long activityId) {
        PromotionActivity activity = getActivityById(activityId);
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
    public List<PromotionActivity> getActiveActivities() {
        return promotionActivityRepository.findActiveActivities(LocalDateTime.now());
    }

    /**
     * 获取新人专享活动列表
     *
     * @return 活动列表
     */
    public List<PromotionActivity> getNewUserActivities() {
        return promotionActivityRepository.findNewUserActivities(LocalDateTime.now());
    }

    /**
     * 获取即将过期的活动列表（7天内）
     *
     * @return 活动列表
     */
    public List<PromotionActivity> getExpiringActivities() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expireTime = now.plusDays(7);
        return promotionActivityRepository.findExpiringActivities(now, expireTime);
    }

    /**
     * 获取活动统计数据
     *
     * @return 统计数据Map
     */
    public Map<String, Object> getActivityStats() {
        Map<String, Object> stats = new HashMap<>();
        
        // 各状态活动数量
        List<Object[]> statusStats = promotionActivityRepository.countActivitiesByStatus();
        Map<String, Long> statusMap = new HashMap<>();
        for (Object[] row : statusStats) {
            String status = (String) row[0];
            Long count = (Long) row[1];
            statusMap.put(status, count);
        }
        
        stats.put("total", promotionActivityRepository.count());
        stats.put("draft", statusMap.getOrDefault("DRAFT", 0L));
        stats.put("pending", statusMap.getOrDefault("PENDING", 0L));
        stats.put("active", statusMap.getOrDefault("ACTIVE", 0L));
        stats.put("paused", statusMap.getOrDefault("PAUSED", 0L));
        stats.put("expired", statusMap.getOrDefault("EXPIRED", 0L));
        stats.put("cancelled", statusMap.getOrDefault("CANCELLED", 0L));
        
        // 各类型活动数量
        List<Object[]> typeStats = promotionActivityRepository.countActivitiesByType();
        List<Map<String, Object>> typeData = new ArrayList<>();
        for (Object[] row : typeStats) {
            Map<String, Object> typeItem = new HashMap<>();
            typeItem.put("type", row[0]);
            typeItem.put("count", row[1]);
            typeData.add(typeItem);
        }
        stats.put("typeDistribution", typeData);
        
        // 活动执行统计
        Integer issuedQuantity = promotionActivityRepository.sumIssuedQuantity();
        Integer usedQuantity = promotionActivityRepository.sumUsedQuantity();
        
        stats.put("totalIssued", issuedQuantity != null ? issuedQuantity : 0);
        stats.put("totalUsed", usedQuantity != null ? usedQuantity : 0);
        
        // 使用率
        if (issuedQuantity != null && issuedQuantity > 0) {
            stats.put("usageRate", new BigDecimal(usedQuantity != null ? usedQuantity : 0)
                    .divide(new BigDecimal(issuedQuantity), 4, java.math.RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100")));
        } else {
            stats.put("usageRate", BigDecimal.ZERO);
        }
        
        return stats;
    }

    /**
     * 根据活动类型查询活动
     *
     * @param activityType 活动类型
     * @return 活动列表
     */
    public List<PromotionActivity> getActivitiesByType(String activityType) {
        return promotionActivityRepository.findByActivityTypeOrderByCreateTimeDesc(activityType);
    }

    /**
     * 根据状态查询活动
     *
     * @param status 活动状态
     * @return 活动列表
     */
    public List<PromotionActivity> getActivitiesByStatus(String status) {
        return promotionActivityRepository.findByStatusOrderBySortOrderAsc(status);
    }

    /**
     * 更新活动使用统计
     *
     * @param activityId 活动ID
     * @param discountAmount 优惠金额
     */
    public void updateActivityUsage(Long activityId, BigDecimal discountAmount) {
        PromotionActivity activity = getActivityById(activityId);
        if (activity != null) {
            if (activity.getUsedAmount() == null) {
                activity.setUsedAmount(BigDecimal.ZERO);
            }
            activity.setUsedAmount(activity.getUsedAmount().add(discountAmount));
            
            if (activity.getUsedQuantity() == null) {
                activity.setUsedQuantity(0);
            }
            activity.setUsedQuantity(activity.getUsedQuantity() + 1);
            
            updateActivity(activity);
        }
    }

    /**
     * 计算优惠金额
     *
     * @param activityId 活动ID
     * @param orderAmount 订单金额
     * @return 优惠金额
     */
    public BigDecimal calculateDiscount(Long activityId, BigDecimal orderAmount) {
        PromotionActivity activity = getActivityById(activityId);
        if (activity == null || !"ACTIVE".equals(activity.getStatus())) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal discount = BigDecimal.ZERO;
        
        switch (activity.getDiscountType()) {
            case "FIXED_AMOUNT":
                // 固定金额减免
                discount = activity.getDiscountAmount() != null ? activity.getDiscountAmount() : BigDecimal.ZERO;
                break;
                
            case "PERCENTAGE":
                // 百分比折扣
                if (activity.getDiscountRate() != null && orderAmount != null) {
                    discount = orderAmount.multiply(BigDecimal.ONE.subtract(activity.getDiscountRate()));
                    // 封顶限制
                    if (activity.getMaxDiscountAmount() != null && discount.compareTo(activity.getMaxDiscountAmount()) > 0) {
                        discount = activity.getMaxDiscountAmount();
                    }
                }
                break;
                
            case "FULL_REDUCTION":
                // 满减
                if (activity.getThresholdAmount() != null && activity.getDiscountAmount() != null 
                        && orderAmount != null && orderAmount.compareTo(activity.getThresholdAmount()) >= 0) {
                    discount = activity.getDiscountAmount();
                }
                break;
                
            default:
                break;
        }
        
        // 优惠金额不能超过订单金额
        if (orderAmount != null && discount.compareTo(orderAmount) > 0) {
            discount = orderAmount;
        }
        
        return discount.setScale(2, java.math.RoundingMode.HALF_UP);
    }

    /**
     * 生成测试优惠活动数据
     *
     * @param count 数量
     */
    public void generateTestActivities(int count) {
        Random random = new Random();
        String[] types = {"NEW_USER", "FULL_REDUCTION", "DISCOUNT", "COUPON", "TIME_LIMITED"};
        String[] statuses = {"DRAFT", "PENDING", "ACTIVE", "EXPIRED"};
        String[] discountTypes = {"FIXED_AMOUNT", "PERCENTAGE", "FULL_REDUCTION"};
        String[] userTypes = {"ALL", "NEW_USER", "VIP_USER"};
        
        for (int i = 0; i < count; i++) {
            PromotionActivity activity = new PromotionActivity();
            activity.setActivityName("测试优惠活动" + (i + 1));
            activity.setActivityType(types[random.nextInt(types.length)]);
            activity.setDescription("这是一个测试优惠活动，用于系统功能测试。");
            activity.setDiscountType(discountTypes[random.nextInt(discountTypes.length)]);
            activity.setApplicableUserType(userTypes[random.nextInt(userTypes.length)]);
            activity.setStatus(statuses[random.nextInt(statuses.length)]);
            activity.setBudgetAmount(new BigDecimal(10000 + random.nextInt(50000)));
            activity.setUsedAmount(new BigDecimal(random.nextInt(10000)));
            
            // 设置优惠规则
            switch (activity.getDiscountType()) {
                case "FIXED_AMOUNT":
                    activity.setDiscountAmount(new BigDecimal(10 + random.nextInt(50)));
                    break;
                case "PERCENTAGE":
                    activity.setDiscountRate(new BigDecimal("0.7").add(new BigDecimal(random.nextDouble() * 0.2)));
                    activity.setMaxDiscountAmount(new BigDecimal(50 + random.nextInt(100)));
                    break;
                case "FULL_REDUCTION":
                    activity.setThresholdAmount(new BigDecimal(50 + random.nextInt(200)));
                    activity.setDiscountAmount(new BigDecimal(10 + random.nextInt(50)));
                    break;
            }
            
            // 设置时间
            LocalDateTime now = LocalDateTime.now();
            activity.setStartTime(now.minusDays(random.nextInt(30)));
            activity.setEndTime(now.plusDays(random.nextInt(30)));
            
            activity.setTotalQuantity(1000 + random.nextInt(5000));
            activity.setIssuedQuantity(random.nextInt(1000));
            activity.setUsedQuantity(random.nextInt(500));
            activity.setPerUserLimit(1 + random.nextInt(5));
            activity.setSortOrder(i + 1);
            activity.setAutoIssue(random.nextBoolean());
            
            createActivity(activity);
        }
        
        logger.info("已生成{}条测试优惠活动数据", count);
    }
}

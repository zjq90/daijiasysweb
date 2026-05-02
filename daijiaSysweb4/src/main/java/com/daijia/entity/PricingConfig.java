package com.daijia.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalTime;
import java.time.LocalDateTime;

/**
 * 计费配置实体类
 * 存储系统的计费规则配置，包括起步价、里程费、等待费等
 * 支持多套配置，系统可以根据时间、场景自动切换
 */
@Data
@Entity
@Table(name = "pricing_configs")
public class PricingConfig {

    /**
     * 配置唯一标识ID，主键，自增
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 配置名称
     * 如：日间标准计费、夜间标准计费、高峰期计费等
     */
    @Column(name = "config_name", nullable = false, length = 100)
    private String configName;

    /**
     * 起步价
     * 用户下单的基础费用
     */
    @Column(name = "start_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal startPrice = BigDecimal.ZERO;

    /**
     * 起步包含距离（公里）
     * 起步价包含的行驶距离
     */
    @Column(name = "start_distance", precision = 10, scale = 2, nullable = false)
    private BigDecimal startDistance = new BigDecimal("3.00");

    /**
     * 每公里价格（超出起步距离后）
     * 超出起步距离后，每公里的额外费用
     */
    @Column(name = "per_km_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal perKmPrice = BigDecimal.ZERO;

    /**
     * 免费等待时间（分钟）
     * 司机到达后，免费等待的时间
     */
    @Column(name = "free_wait_time", nullable = false)
    private Integer freeWaitTime = 10;

    /**
     * 超时每分钟等待费
     * 超出免费等待时间后，每分钟的等待费用
     */
    @Column(name = "per_minute_wait_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal perMinuteWaitPrice = BigDecimal.ZERO;

    /**
     * 夜间加价开始时间
     * 如：22:00:00
     */
    @Column(name = "night_surcharge_start")
    private LocalTime nightSurchargeStart;

    /**
     * 夜间加价结束时间
     * 如：06:00:00
     */
    @Column(name = "night_surcharge_end")
    private LocalTime nightSurchargeEnd;

    /**
     * 夜间加价倍率
     * 如：1.3 表示加价30%
     */
    @Column(name = "night_surcharge_rate", precision = 3, scale = 2)
    private BigDecimal nightSurchargeRate = new BigDecimal("1.00");

    /**
     * 配置状态：1-启用，0-禁用
     */
    @Column(name = "status", nullable = false)
    private Integer status = 1;

    /**
     * 是否默认配置
     * 系统只有一个默认配置
     */
    @Column(name = "is_default")
    private Boolean isDefault = false;

    /**
     * 创建时间
     */
    @Column(name = "created_time", updatable = false)
    private LocalDateTime createdTime = LocalDateTime.now();

    /**
     * 更新时间
     */
    @Column(name = "updated_time")
    private LocalDateTime updatedTime = LocalDateTime.now();

    /**
     * 计算订单费用
     * 根据行驶距离和等待时间计算总费用
     * 
     * @param distance 行驶距离（公里）
     * @param waitTime 等待时间（分钟）
     * @param currentTime 当前时间（用于判断是否夜间加价）
     * @return 计算出的总费用
     */
    public BigDecimal calculatePrice(BigDecimal distance, Integer waitTime, LocalTime currentTime) {
        if (distance == null || distance.compareTo(BigDecimal.ZERO) < 0) {
            distance = BigDecimal.ZERO;
        }
        if (waitTime == null || waitTime < 0) {
            waitTime = 0;
        }

        BigDecimal totalPrice = BigDecimal.ZERO;

        // 1. 计算里程费用
        // 如果距离小于等于起步距离，只收起步价
        if (distance.compareTo(startDistance) <= 0) {
            totalPrice = startPrice;
        } else {
            // 超出起步距离的部分按里程费计算
            BigDecimal extraDistance = distance.subtract(startDistance);
            totalPrice = startPrice.add(extraDistance.multiply(perKmPrice));
        }

        // 2. 计算等待费用
        if (waitTime > freeWaitTime) {
            int extraWaitMinutes = waitTime - freeWaitTime;
            BigDecimal waitFee = new BigDecimal(extraWaitMinutes).multiply(perMinuteWaitPrice);
            totalPrice = totalPrice.add(waitFee);
        }

        // 3. 判断是否需要夜间加价
        if (isNightTime(currentTime) && nightSurchargeRate != null 
            && nightSurchargeRate.compareTo(new BigDecimal("1.00")) > 0) {
            totalPrice = totalPrice.multiply(nightSurchargeRate);
        }

        // 保留两位小数，四舍五入
        return totalPrice.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * 判断当前时间是否为夜间加价时间段
     * 
     * @param currentTime 当前时间
     * @return 是否为夜间时段
     */
    private Boolean isNightTime(LocalTime currentTime) {
        if (nightSurchargeStart == null || nightSurchargeEnd == null || currentTime == null) {
            return false;
        }

        // 如果开始时间小于结束时间，说明是同一天的时间段（如 22:00 - 23:59）
        // 如果开始时间大于结束时间，说明是跨天的时间段（如 22:00 - 06:00）
        if (nightSurchargeStart.isBefore(nightSurchargeEnd)) {
            return !currentTime.isBefore(nightSurchargeStart) && !currentTime.isAfter(nightSurchargeEnd);
        } else {
            // 跨天情况：当前时间 >= 开始时间 或者 <= 结束时间
            return !currentTime.isBefore(nightSurchargeStart) || !currentTime.isAfter(nightSurchargeEnd);
        }
    }

    /**
     * 获取配置说明文本
     * 用于在页面上显示该配置的具体计费规则
     * 
     * @return 配置说明文本
     */
    public String getConfigDescription() {
        StringBuilder desc = new StringBuilder();
        desc.append("起步价: ").append(startPrice).append("元（含").append(startDistance).append("公里）");
        desc.append("，超出后: ").append(perKmPrice).append("元/公里");
        desc.append("，免费等待: ").append(freeWaitTime).append("分钟");
        desc.append("，超时等待费: ").append(perMinuteWaitPrice).append("元/分钟");
        
        if (nightSurchargeStart != null && nightSurchargeEnd != null 
            && nightSurchargeRate != null && nightSurchargeRate.compareTo(new BigDecimal("1.00")) > 0) {
            desc.append("，夜间加价: ").append(nightSurchargeStart).append("-")
                .append(nightSurchargeEnd).append(" 加价").append(nightSurchargeRate.multiply(new BigDecimal("100")).subtract(new BigDecimal("100")).intValue()).append("%");
        }
        
        return desc.toString();
    }
}

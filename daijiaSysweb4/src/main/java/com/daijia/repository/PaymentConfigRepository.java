package com.daijia.repository;

import com.daijia.entity.PaymentConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 支付方式配置数据访问接口
 * 继承JpaRepository，提供基础的增删改查功能
 * 同时定义与支付类型、支付模式相关的自定义查询方法
 */
@Repository
public interface PaymentConfigRepository extends JpaRepository<PaymentConfig, Long> {

    /**
     * 查询所有启用状态的配置
     * 
     * @param status 配置状态
     * @return 启用的配置列表
     */
    List<PaymentConfig> findByStatusOrderByCreatedTimeDesc(@Param("status") Integer status);

    /**
     * 根据支付类型查询配置
     * 
     * @param paymentType 支付类型（WECHAT/ALIPAY/UNIONPAY）
     * @return 支付配置（可选）
     */
    Optional<PaymentConfig> findByPaymentType(@Param("paymentType") String paymentType);

    /**
     * 根据支付类型和状态查询配置
     * 
     * @param paymentType 支付类型
     * @param status 配置状态
     * @return 支付配置列表
     */
    List<PaymentConfig> findByPaymentTypeAndStatusOrderByCreatedTimeDesc(
            @Param("paymentType") String paymentType,
            @Param("status") Integer status);

    /**
     * 查询支持预付模式的支付配置
     * 
     * @param status 配置状态
     * @return 支持预付的支付配置列表
     */
    @Query("SELECT p FROM PaymentConfig p WHERE p.status = :status AND (p.payMode = 'PREPAY' OR p.payMode = 'BOTH') ORDER BY p.createdTime DESC")
    List<PaymentConfig> findPrepayEnabledConfigs(@Param("status") Integer status);

    /**
     * 查询支持后付模式的支付配置
     * 
     * @param status 配置状态
     * @return 支持后付的支付配置列表
     */
    @Query("SELECT p FROM PaymentConfig p WHERE p.status = :status AND (p.payMode = 'POSTPAY' OR p.payMode = 'BOTH') ORDER BY p.createdTime DESC")
    List<PaymentConfig> findPostpayEnabledConfigs(@Param("status") Integer status);

    /**
     * 根据支付名称模糊查询
     * 
     * @param keyword 关键词
     * @return 匹配的配置列表
     */
    @Query("SELECT p FROM PaymentConfig p WHERE p.paymentName LIKE %:keyword% OR p.paymentType LIKE %:keyword%")
    List<PaymentConfig> searchByKeyword(@Param("keyword") String keyword);

    /**
     * 检查支付类型是否存在
     * 
     * @param paymentType 支付类型
     * @return 是否存在
     */
    boolean existsByPaymentType(@Param("paymentType") String paymentType);

    /**
     * 检查支付类型是否存在（排除指定ID）
     * 用于更新时的重复检查
     * 
     * @param paymentType 支付类型
     * @param excludeId 排除的ID
     * @return 是否存在
     */
    @Query("SELECT COUNT(p) > 0 FROM PaymentConfig p WHERE p.paymentType = :paymentType AND p.id <> :excludeId")
    boolean existsByPaymentTypeExcludingId(@Param("paymentType") String paymentType, @Param("excludeId") Long excludeId);

    /**
     * 检查支付名称是否存在
     * 
     * @param paymentName 支付名称
     * @return 是否存在
     */
    boolean existsByPaymentName(@Param("paymentName") String paymentName);

    /**
     * 检查支付名称是否存在（排除指定ID）
     * 
     * @param paymentName 支付名称
     * @param excludeId 排除的ID
     * @return 是否存在
     */
    @Query("SELECT COUNT(p) > 0 FROM PaymentConfig p WHERE p.paymentName = :paymentName AND p.id <> :excludeId")
    boolean existsByPaymentNameExcludingId(@Param("paymentName") String paymentName, @Param("excludeId") Long excludeId);

    /**
     * 统计启用状态的配置数量
     * 
     * @param status 配置状态
     * @return 配置数量
     */
    long countByStatus(@Param("status") Integer status);

    /**
     * 查询最新创建的配置
     * 
     * @param limit 返回数量
     * @return 最新配置列表
     */
    @Query("SELECT p FROM PaymentConfig p ORDER BY p.createdTime DESC")
    List<PaymentConfig> findLatestConfigs(@Param("limit") int limit);

    /**
     * 根据支付模式查询配置
     * 
     * @param payMode 支付模式
     * @param status 配置状态
     * @return 配置列表
     */
    List<PaymentConfig> findByPayModeAndStatusOrderByCreatedTimeDesc(
            @Param("payMode") String payMode,
            @Param("status") Integer status);
}

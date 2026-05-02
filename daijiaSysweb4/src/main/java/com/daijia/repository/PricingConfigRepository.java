package com.daijia.repository;

import com.daijia.entity.PricingConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 计费配置数据访问接口
 * 继承JpaRepository，提供基础的增删改查功能
 * 同时定义与默认配置、启用配置相关的自定义查询方法
 */
@Repository
public interface PricingConfigRepository extends JpaRepository<PricingConfig, Long> {

    /**
     * 查询所有启用状态的配置
     * 
     * @param status 配置状态
     * @return 启用的配置列表
     */
    List<PricingConfig> findByStatusOrderByCreatedTimeDesc(@Param("status") Integer status);

    /**
     * 查询默认配置
     * 系统只能有一个默认配置
     * 
     * @return 默认配置（可选）
     */
    Optional<PricingConfig> findByIsDefaultTrue();

    /**
     * 查询所有默认配置
     * 用于检查是否存在多个默认配置
     * 
     * @return 默认配置列表
     */
    List<PricingConfig> findAllByIsDefaultTrue();

    /**
     * 根据配置名称模糊查询
     * 
     * @param keyword 关键词
     * @return 匹配的配置列表
     */
    @Query("SELECT p FROM PricingConfig p WHERE p.configName LIKE %:keyword%")
    List<PricingConfig> searchByKeyword(@Param("keyword") String keyword);

    /**
     * 检查配置名称是否存在
     * 
     * @param configName 配置名称
     * @return 是否存在
     */
    boolean existsByConfigName(@Param("configName") String configName);

    /**
     * 检查配置名称是否存在（排除指定ID）
     * 用于更新时的重复检查
     * 
     * @param configName 配置名称
     * @param excludeId 排除的ID
     * @return 是否存在
     */
    @Query("SELECT COUNT(p) > 0 FROM PricingConfig p WHERE p.configName = :configName AND p.id <> :excludeId")
    boolean existsByConfigNameExcludingId(@Param("configName") String configName, @Param("excludeId") Long excludeId);

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
    @Query("SELECT p FROM PricingConfig p ORDER BY p.createdTime DESC")
    List<PricingConfig> findLatestConfigs(@Param("limit") int limit);

    /**
     * 根据ID列表查询配置
     * 
     * @param ids ID列表
     * @return 配置列表
     */
    List<PricingConfig> findByIdInOrderByCreatedTimeDesc(List<Long> ids);
}

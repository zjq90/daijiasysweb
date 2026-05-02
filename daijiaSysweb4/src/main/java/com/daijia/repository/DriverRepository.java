package com.daijia.repository;

import com.daijia.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 司机数据访问接口
 * 继承JpaRepository，提供基础的增删改查功能
 * 同时定义与派单优先级、服务权限相关的自定义查询方法
 */
@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {

    /**
     * 根据用户名查询司机
     * 
     * @param username 用户名
     * @return 司机对象（可选）
     */
    Optional<Driver> findByUsername(String username);

    /**
     * 根据手机号查询司机
     * 
     * @param phone 手机号
     * @return 司机对象（可选）
     */
    Optional<Driver> findByPhone(String phone);

    /**
     * 查询所有正常状态的司机
     * 
     * @param status 司机状态
     * @return 正常司机列表
     */
    List<Driver> findByStatusOrderByCreatedTimeDesc(Integer status);

    /**
     * 根据派单优先级降序查询可用司机
     * 用于派单系统，优先选择优先级高的司机
     * 
     * @param status 司机状态（1-正常）
     * @param servicePermission 服务权限（1-全部服务）
     * @return 按优先级排序的司机列表
     */
    List<Driver> findByStatusAndServicePermissionOrderByDispatchPriorityDesc(
            @Param("status") Integer status,
            @Param("servicePermission") Integer servicePermission);

    /**
     * 查询高优先级司机（优先级 >= 8）
     * 用于推荐优质司机
     * 
     * @return 高优先级司机列表
     */
    @Query("SELECT d FROM Driver d WHERE d.status = 1 AND d.servicePermission = 1 AND d.dispatchPriority >= 8 ORDER BY d.dispatchPriority DESC, d.avgRating DESC")
    List<Driver> findHighPriorityDrivers();

    /**
     * 查询低评分司机（评分 < 4.0）
     * 用于监控需要关注的司机
     * 
     * @return 低评分司机列表
     */
    @Query("SELECT d FROM Driver d WHERE d.status = 1 AND d.avgRating < 4.0 ORDER BY d.avgRating ASC")
    List<Driver> findLowRatingDrivers();

    /**
     * 根据用户名或手机号或真实姓名模糊查询司机
     * 
     * @param keyword 关键词
     * @return 匹配的司机列表
     */
    @Query("SELECT d FROM Driver d WHERE d.username LIKE %:keyword% OR d.phone LIKE %:keyword% OR d.realName LIKE %:keyword% OR d.carPlate LIKE %:keyword%")
    List<Driver> searchByKeyword(@Param("keyword") String keyword);

    /**
     * 检查用户名是否存在
     * 
     * @param username 用户名
     * @return 是否存在
     */
    boolean existsByUsername(String username);

    /**
     * 检查手机号是否存在
     * 
     * @param phone 手机号
     * @return 是否存在
     */
    boolean existsByPhone(String phone);

    /**
     * 检查身份证号是否存在
     * 
     * @param idCard 身份证号
     * @return 是否存在
     */
    boolean existsByIdCard(String idCard);

    /**
     * 根据状态统计司机数量
     * 
     * @param status 司机状态
     * @return 司机数量
     */
    long countByStatus(Integer status);

    /**
     * 根据服务权限统计司机数量
     * 
     * @param servicePermission 服务权限
     * @return 司机数量
     */
    long countByServicePermission(Integer servicePermission);

    /**
     * 查询评分最高的前N个司机
     * 
     * @param limit 返回数量
     * @return 高评分司机列表
     */
    @Query("SELECT d FROM Driver d WHERE d.status = 1 ORDER BY d.avgRating DESC, d.totalRatings DESC")
    List<Driver> findTopDrivers(@Param("limit") int limit);

    /**
     * 根据服务权限查询司机
     * 
     * @param servicePermission 服务权限
     * @return 司机列表
     */
    List<Driver> findByServicePermissionOrderByDispatchPriorityDesc(Integer servicePermission);
}

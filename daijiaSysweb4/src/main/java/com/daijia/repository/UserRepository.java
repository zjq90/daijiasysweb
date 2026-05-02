package com.daijia.repository;

import com.daijia.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 用户数据访问接口
 * 继承JpaRepository，提供基础的增删改查功能
 * 同时定义自定义查询方法
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 根据用户名查询用户
     * 
     * @param username 用户名
     * @return 用户对象（可选）
     */
    Optional<User> findByUsername(String username);

    /**
     * 根据手机号查询用户
     * 
     * @param phone 手机号
     * @return 用户对象（可选）
     */
    Optional<User> findByPhone(String phone);

    /**
     * 查询所有正常状态的用户
     * 
     * @return 正常用户列表
     */
    List<User> findByStatusOrderByCreatedTimeDesc(Integer status);

    /**
     * 根据用户名或手机号模糊查询用户
     * 
     * @param keyword 关键词
     * @return 匹配的用户列表
     */
    @Query("SELECT u FROM User u WHERE u.username LIKE %:keyword% OR u.phone LIKE %:keyword% OR u.realName LIKE %:keyword%")
    List<User> searchByKeyword(@Param("keyword") String keyword);

    /**
     * 查询平均评分低于指定值的用户
     * 用于识别低评分用户，可能需要限制服务
     * 
     * @param rating 评分阈值
     * @return 低评分用户列表
     */
    List<User> findByAvgRatingLessThanOrderByAvgRatingAsc(java.math.BigDecimal rating);

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
     * 根据状态统计用户数量
     * 
     * @param status 用户状态
     * @return 用户数量
     */
    long countByStatus(Integer status);

    /**
     * 查询评分最高的前N个用户
     * 
     * @param limit 返回数量
     * @return 高评分用户列表
     */
    @Query("SELECT u FROM User u WHERE u.status = 1 ORDER BY u.avgRating DESC, u.totalRatings DESC")
    List<User> findTopUsers(@Param("limit") int limit);
}

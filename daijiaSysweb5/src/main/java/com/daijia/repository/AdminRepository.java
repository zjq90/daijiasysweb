package com.daijia.repository;

import com.daijia.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 管理员数据访问接口
 * 
 * 继承JpaRepository，提供基本的增删改查功能
 * 
 * @author daijia
 * @version 1.0.0
 */
@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

    /**
     * 根据用户名查找管理员
     * 
     * @param username 用户名
     * @return Optional<Admin> 管理员对象
     */
    Optional<Admin> findByUsername(String username);

    /**
     * 根据用户名和密码查找管理员（登录验证）
     * 
     * @param username 用户名
     * @param password 密码
     * @return Optional<Admin> 管理员对象
     */
    Optional<Admin> findByUsernameAndPassword(String username, String password);

    /**
     * 检查用户名是否存在
     * 
     * @param username 用户名
     * @return 是否存在
     */
    boolean existsByUsername(String username);
}

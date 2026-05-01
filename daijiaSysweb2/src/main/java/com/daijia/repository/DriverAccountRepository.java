package com.daijia.repository;

import com.daijia.entity.DriverAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 代驾账户数据访问层
 * 继承JpaRepository，提供基本的CRUD操作
 */
@Repository
public interface DriverAccountRepository extends JpaRepository<DriverAccount, Long> {

    /**
     * 根据手机号查询账户
     * @param phone 手机号
     * @return 账户信息
     */
    DriverAccount findByPhone(String phone);

    /**
     * 根据状态查询账户列表
     * @param status 状态
     * @return 账户列表
     */
    List<DriverAccount> findByStatus(String status);

    /**
     * 根据司机姓名模糊查询
     * @param driverName 司机姓名
     * @return 账户列表
     */
    List<DriverAccount> findByDriverNameContaining(String driverName);

    /**
     * 检查手机号是否存在
     * @param phone 手机号
     * @return 是否存在
     */
    boolean existsByPhone(String phone);
}

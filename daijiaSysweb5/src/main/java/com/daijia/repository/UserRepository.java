package com.daijia.repository;

import com.daijia.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 用户数据访问接口
 * 
 * @author daijia
 * @version 1.0.0
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByPhone(String phone);

    List<User> findByStatus(Integer status);

    boolean existsByUsername(String username);

    boolean existsByPhone(String phone);

    @Query("SELECT u FROM User u WHERE u.realName LIKE %?1% OR u.phone LIKE %?1%")
    List<User> search(String keyword);
}

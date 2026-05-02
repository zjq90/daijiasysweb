package com.daijia.repository;

import com.daijia.entity.OrderRating;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 订单评价数据访问接口
 * 
 * 用于处理订单评价功能的数据操作
 * 
 * @author daijia
 * @version 1.0.0
 */
@Repository
public interface OrderRatingRepository extends JpaRepository<OrderRating, Long> {

    Optional<OrderRating> findByOrderId(Long orderId);

    List<OrderRating> findByUserId(Long userId);

    List<OrderRating> findByUserIdOrderByCreateTimeDesc(Long userId);

    List<OrderRating> findByDriverId(Long driverId);

    List<OrderRating> findByDriverIdOrderByCreateTimeDesc(Long driverId);

    Page<OrderRating> findAll(Pageable pageable);

    Page<OrderRating> findByDriverId(Long driverId, Pageable pageable);

    @Query("SELECT AVG(r.rating) FROM OrderRating r WHERE r.driverId = ?1")
    Double findAverageRatingByDriverId(Long driverId);

    @Query("SELECT COUNT(r) FROM OrderRating r WHERE r.driverId = ?1 AND r.rating = ?2")
    long countByDriverIdAndRating(Long driverId, Integer rating);

    @Query("SELECT r FROM OrderRating r WHERE r.createTime BETWEEN ?1 AND ?2")
    List<OrderRating> findByCreateTimeBetween(LocalDateTime startTime, LocalDateTime endTime);

    @Query("SELECT r FROM OrderRating r WHERE r.rating >= ?1 ORDER BY r.createTime DESC")
    List<OrderRating> findByRatingGreaterThanEqual(Integer minRating);

    @Query("SELECT r FROM OrderRating r WHERE r.rating <= ?1 ORDER BY r.createTime DESC")
    List<OrderRating> findByRatingLessThanEqual(Integer maxRating);
}

package com.daijia.repository;

import com.daijia.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 订单数据访问接口
 * 
 * @author daijia
 * @version 1.0.0
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByOrderNo(String orderNo);

    List<Order> findByUserId(Long userId);

    List<Order> findByDriverId(Long driverId);

    List<Order> findByStatus(Integer status);

    List<Order> findByUserIdAndStatus(Long userId, Integer status);

    List<Order> findByDriverIdAndStatus(Long driverId, Integer status);

    Page<Order> findAll(Pageable pageable);

    Page<Order> findByStatus(Integer status, Pageable pageable);

    Page<Order> findByUserId(Long userId, Pageable pageable);

    Page<Order> findByDriverId(Long driverId, Pageable pageable);

    @Query("SELECT o FROM Order o WHERE o.orderNo LIKE %?1% OR o.startAddress LIKE %?1% OR o.endAddress LIKE %?1%")
    List<Order> search(String keyword);

    @Query("SELECT o FROM Order o WHERE o.orderTime BETWEEN ?1 AND ?2")
    List<Order> findByOrderTimeBetween(LocalDateTime startTime, LocalDateTime endTime);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.status = ?1")
    long countByStatus(Integer status);

    @Query("SELECT SUM(o.actualAmount) FROM Order o WHERE o.status = 5 AND o.orderTime BETWEEN ?1 AND ?2")
    java.math.BigDecimal sumActualAmountByDateRange(LocalDateTime startTime, LocalDateTime endTime);

    @Query("SELECT o FROM Order o WHERE o.status IN (0,1,2,3)")
    List<Order> findActiveOrders();
}

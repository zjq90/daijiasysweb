
package com.daijia.repository;

import com.daijia.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByOrderNo(String orderNo);
    List<Order> findByStatus(String status);
    List<Order> findByUserId(Long userId);
    List<Order> findByDriverId(Long driverId);
    
    @Query("SELECT o FROM Order o WHERE o.status = 'PENDING'")
    List<Order> findPendingOrders();
    
    @Query("SELECT o FROM Order o ORDER BY o.createTime DESC")
    List<Order> findAllOrderByCreateTimeDesc();
}

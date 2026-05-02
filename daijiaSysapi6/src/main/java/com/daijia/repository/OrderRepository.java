package com.daijia.repository;

import com.daijia.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    Optional<Order> findByOrderNo(String orderNo);
    
    List<Order> findByClientIdOrderByCreateTimeDesc(Long clientId);
    
    List<Order> findByDriverIdOrderByCreateTimeDesc(Long driverId);
    
    List<Order> findByStatus(String status);
    
    @Query("SELECT o FROM Order o WHERE o.status = :status AND o.dispatchType = :dispatchType")
    List<Order> findPendingRobberyOrders(@Param("status") String status, @Param("dispatchType") String dispatchType);
    
    @Query("SELECT COUNT(o) FROM Order o WHERE o.driverId = :driverId AND o.createTime >= :startTime")
    long countTodayOrdersByDriverId(@Param("driverId") Long driverId, @Param("startTime") LocalDateTime startTime);
    
    @Query("SELECT o FROM Order o WHERE o.clientId = :clientId AND o.status IN :statuses ORDER BY o.createTime DESC")
    List<Order> findClientOrdersByStatuses(@Param("clientId") Long clientId, @Param("statuses") List<String> statuses);
    
    @Query("SELECT o FROM Order o WHERE o.driverId = :driverId AND o.status IN :statuses ORDER BY o.createTime DESC")
    List<Order> findDriverOrdersByStatuses(@Param("driverId") Long driverId, @Param("statuses") List<String> statuses);
}

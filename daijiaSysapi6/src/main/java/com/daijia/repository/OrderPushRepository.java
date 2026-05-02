package com.daijia.repository;

import com.daijia.entity.OrderPush;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderPushRepository extends JpaRepository<OrderPush, Long> {
    
    List<OrderPush> findByDriverIdOrderByCreateTimeDesc(Long driverId);
    
    List<OrderPush> findByOrderId(Long orderId);
    
    Optional<OrderPush> findByOrderIdAndDriverId(Long orderId, Long driverId);
    
    List<OrderPush> findByDriverIdAndStatusOrderByCreateTimeDesc(Long driverId, String status);
    
    boolean existsByOrderIdAndDriverIdAndStatus(Long orderId, Long driverId, String status);
}

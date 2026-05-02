package com.daijia.service;

import cn.hutool.core.util.StrUtil;
import com.daijia.common.Constants;
import com.daijia.dto.OrderDetailDto;
import com.daijia.dto.PlaceOrderDto;
import com.daijia.dto.UserInfoDto;
import com.daijia.entity.*;
import com.daijia.exception.BusinessException;
import com.daijia.repository.*;
import com.daijia.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
    
    private final OrderRepository orderRepository;
    private final OrderPushRepository orderPushRepository;
    private final DriverService driverService;
    private final ClientService clientService;
    private final DriverSettingRepository driverSettingRepository;
    private final DriverRepository driverRepository;
    
    @Transactional
    public Order createOrder(Long clientId, PlaceOrderDto dto) {
        Client client = clientService.getClientById(clientId);
        
        if (Constants.ORDER_TYPE_APPOINTMENT.equals(dto.getOrderType()) && dto.getAppointTime() == null) {
            throw new BusinessException("预约代驾请指定预约时间");
        }
        
        Order order = new Order();
        order.setOrderNo(CommonUtil.generateOrderNo());
        order.setClientId(clientId);
        order.setOrderType(dto.getOrderType());
        order.setDispatchType(dto.getDispatchType() != null ? dto.getDispatchType() : Constants.DISPATCH_TYPE_ROBBERY);
        order.setAppointTime(dto.getAppointTime());
        order.setStartAddress(dto.getStartAddress());
        order.setStartLat(dto.getStartLat());
        order.setStartLng(dto.getStartLng());
        order.setEndAddress(dto.getEndAddress());
        order.setEndLat(dto.getEndLat());
        order.setEndLng(dto.getEndLng());
        order.setVehicleType(dto.getVehicleType());
        order.setServiceType(dto.getServiceType());
        order.setPromotionId(dto.getPromotionId());
        order.setRemark(dto.getRemark());
        order.setStatus(Constants.ORDER_STATUS_PENDING);
        order.setPayStatus(Constants.PAY_STATUS_UNPAID);
        
        if (dto.getStartLat() != null && dto.getStartLng() != null 
                && dto.getEndLat() != null && dto.getEndLng() != null) {
            double distance = CommonUtil.calculateDistance(
                    dto.getStartLat(), dto.getStartLng(),
                    dto.getEndLat(), dto.getEndLng()
            );
            order.setEstimateDistance(BigDecimal.valueOf(distance));
            BigDecimal baseFee = new BigDecimal("15");
            BigDecimal perKmFee = new BigDecimal("3.5");
            order.setEstimateFee(baseFee.add(perKmFee.multiply(BigDecimal.valueOf(Math.max(0, distance - 3)))));
        } else {
            order.setEstimateDistance(new BigDecimal("10.0"));
            order.setEstimateFee(new BigDecimal("39.5"));
        }
        
        order = orderRepository.save(order);
        
        dispatchOrder(order);
        
        log.info("订单创建成功，订单号：{}", order.getOrderNo());
        return order;
    }
    
    private void dispatchOrder(Order order) {
        List<Driver> onlineDrivers = driverRepository.findByOnlineStatusAndDeleted(Constants.ONLINE_STATUS_ONLINE, Constants.DELETED_NO);
        
        if (onlineDrivers.isEmpty()) {
            log.warn("暂无在线司机，订单号：{}", order.getOrderNo());
            return;
        }
        
        if (Constants.DISPATCH_TYPE_ASSIGN.equals(order.getDispatchType())) {
            Driver assignedDriver = onlineDrivers.stream()
                    .findFirst()
                    .orElse(null);
            
            if (assignedDriver != null) {
                OrderPush push = new OrderPush();
                push.setOrderId(order.getId());
                push.setDriverId(assignedDriver.getId());
                push.setPushType(Constants.PUSH_TYPE_ASSIGN);
                push.setStatus(Constants.PUSH_STATUS_PENDING);
                push.setPushTime(LocalDateTime.now());
                orderPushRepository.save(push);
                log.info("派单成功，订单号：{}，司机ID：{}", order.getOrderNo(), assignedDriver.getId());
            }
        } else {
            for (Driver driver : onlineDrivers) {
                Optional<DriverSetting> settingOpt = driverSettingRepository.findByDriverId(driver.getId());
                if (settingOpt.isPresent()) {
                    DriverSetting setting = settingOpt.get();
                    if (setting.getEnableOrderPush() != null && setting.getEnableOrderPush() == 0) {
                        continue;
                    }
                }
                
                OrderPush push = new OrderPush();
                push.setOrderId(order.getId());
                push.setDriverId(driver.getId());
                push.setPushType(Constants.PUSH_TYPE_ROBBERY);
                push.setStatus(Constants.PUSH_STATUS_PENDING);
                push.setPushTime(LocalDateTime.now());
                orderPushRepository.save(push);
            }
            log.info("抢单推送完成，订单号：{}，推送司机数：{}", order.getOrderNo(), onlineDrivers.size());
        }
    }
    
    @Transactional
    public Order acceptOrder(Long driverId, Long orderId) {
        Driver driver = driverService.getDriverById(driverId);
        Order order = getOrderById(orderId);
        
        if (!Constants.ORDER_STATUS_PENDING.equals(order.getStatus())) {
            throw new BusinessException("订单已被接单或已取消");
        }
        
        DriverSetting setting = driverSettingRepository.findByDriverId(driverId)
                .orElseGet(() -> {
                    DriverSetting s = new DriverSetting();
                    s.setDriverId(driverId);
                    return s;
                });
        
        LocalDateTime todayStart = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        long todayOrderCount = orderRepository.countTodayOrdersByDriverId(driverId, todayStart);
        
        if (setting.getDailyOrderLimit() != null && todayOrderCount >= setting.getDailyOrderLimit()) {
            throw new BusinessException("今日接单已达上限");
        }
        
        Optional<OrderPush> pushOpt = orderPushRepository.findByOrderIdAndDriverId(orderId, driverId);
        if (pushOpt.isPresent()) {
            OrderPush push = pushOpt.get();
            if (Constants.PUSH_STATUS_ACCEPTED.equals(push.getStatus())) {
                throw new BusinessException("您已接此订单");
            }
            if (Constants.PUSH_STATUS_IGNORED.equals(push.getStatus())) {
                throw new BusinessException("您已忽略此订单");
            }
            
            push.setStatus(Constants.PUSH_STATUS_ACCEPTED);
            push.setHandleTime(LocalDateTime.now());
            orderPushRepository.save(push);
        }
        
        order.setDriverId(driverId);
        order.setStatus(Constants.ORDER_STATUS_ACCEPTED);
        order.setAcceptTime(LocalDateTime.now());
        order = orderRepository.save(order);
        
        driver.setOnlineStatus(Constants.ONLINE_STATUS_BUSY);
        driverRepository.save(driver);
        
        log.info("司机接单成功，订单号：{}，司机ID：{}", order.getOrderNo(), driverId);
        return order;
    }
    
    @Transactional
    public void ignoreOrder(Long driverId, Long orderId) {
        Order order = getOrderById(orderId);
        
        Optional<OrderPush> pushOpt = orderPushRepository.findByOrderIdAndDriverId(orderId, driverId);
        if (pushOpt.isPresent()) {
            OrderPush push = pushOpt.get();
            push.setStatus(Constants.PUSH_STATUS_IGNORED);
            push.setHandleTime(LocalDateTime.now());
            orderPushRepository.save(push);
        }
        log.info("司机忽略订单，订单号：{}，司机ID：{}", order.getOrderNo(), driverId);
    }
    
    @Transactional
    public Order startService(Long driverId, Long orderId) {
        Order order = getOrderById(orderId);
        
        if (!Constants.ORDER_STATUS_ACCEPTED.equals(order.getStatus())) {
            throw new BusinessException("订单状态不正确");
        }
        
        if (!order.getDriverId().equals(driverId)) {
            throw new BusinessException("您不是该订单的接单司机");
        }
        
        order.setStatus(Constants.ORDER_STATUS_IN_SERVICE);
        order.setStartServiceTime(LocalDateTime.now());
        order = orderRepository.save(order);
        
        log.info("开始服务，订单号：{}", order.getOrderNo());
        return order;
    }
    
    @Transactional
    public Order endService(Long driverId, Long orderId, BigDecimal actualDistance, BigDecimal actualFee) {
        Order order = getOrderById(orderId);
        
        if (!Constants.ORDER_STATUS_IN_SERVICE.equals(order.getStatus())) {
            throw new BusinessException("订单状态不正确");
        }
        
        if (!order.getDriverId().equals(driverId)) {
            throw new BusinessException("您不是该订单的接单司机");
        }
        
        order.setStatus(Constants.ORDER_STATUS_COMPLETED);
        order.setEndServiceTime(LocalDateTime.now());
        order.setActualDistance(actualDistance != null ? actualDistance : order.getEstimateDistance());
        order.setActualFee(actualFee != null ? actualFee : order.getEstimateFee());
        order.setPayFee(order.getActualFee());
        order = orderRepository.save(order);
        
        Driver driver = driverService.getDriverById(driverId);
        driver.setOrderCount(driver.getOrderCount() + 1);
        driver.setOnlineStatus(Constants.ONLINE_STATUS_ONLINE);
        driverRepository.save(driver);
        
        Client client = clientService.getClientById(order.getClientId());
        client.setOrderCount(client.getOrderCount() + 1);
        
        log.info("结束服务，订单号：{}", order.getOrderNo());
        return order;
    }
    
    @Transactional
    public Order cancelOrder(Long userId, String userType, Long orderId, String reason) {
        Order order = getOrderById(orderId);
        
        if (Constants.ORDER_STATUS_COMPLETED.equals(order.getStatus()) 
                || Constants.ORDER_STATUS_CANCELLED.equals(order.getStatus())) {
            throw new BusinessException("订单已完成或已取消");
        }
        
        order.setStatus(Constants.ORDER_STATUS_CANCELLED);
        order.setCancelTime(LocalDateTime.now());
        order.setCancelUserId(userId);
        order.setCancelReason(reason);
        order = orderRepository.save(order);
        
        if (order.getDriverId() != null) {
            Driver driver = driverService.getDriverById(order.getDriverId());
            driver.setOnlineStatus(Constants.ONLINE_STATUS_ONLINE);
            driverRepository.save(driver);
        }
        
        log.info("订单已取消，订单号：{}，取消人：{}", order.getOrderNo(), userId);
        return order;
    }
    
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException("订单不存在"));
    }
    
    public OrderDetailDto getOrderDetail(Long orderId) {
        Order order = getOrderById(orderId);
        return convertToDetailDto(order);
    }
    
    public List<OrderDetailDto> getClientOrders(Long clientId, String status) {
        List<Order> orders;
        if (StrUtil.isBlank(status)) {
            orders = orderRepository.findByClientIdOrderByCreateTimeDesc(clientId);
        } else {
            orders = orderRepository.findClientOrdersByStatuses(clientId, Arrays.asList(status));
        }
        return orders.stream().map(this::convertToDetailDto).collect(Collectors.toList());
    }
    
    public List<OrderDetailDto> getDriverOrders(Long driverId, String status) {
        List<Order> orders;
        if (StrUtil.isBlank(status)) {
            orders = orderRepository.findByDriverIdOrderByCreateTimeDesc(driverId);
        } else {
            orders = orderRepository.findDriverOrdersByStatuses(driverId, Arrays.asList(status));
        }
        return orders.stream().map(this::convertToDetailDto).collect(Collectors.toList());
    }
    
    public List<OrderDetailDto> getPendingRobberyOrders() {
        List<Order> orders = orderRepository.findPendingRobberyOrders(
                Constants.ORDER_STATUS_PENDING, 
                Constants.DISPATCH_TYPE_ROBBERY
        );
        return orders.stream().map(this::convertToDetailDto).collect(Collectors.toList());
    }
    
    public List<OrderPush> getDriverPendingPushes(Long driverId) {
        return orderPushRepository.findByDriverIdAndStatusOrderByCreateTimeDesc(
                driverId, Constants.PUSH_STATUS_PENDING
        );
    }
    
    private OrderDetailDto convertToDetailDto(Order order) {
        OrderDetailDto dto = new OrderDetailDto();
        BeanUtils.copyProperties(order, dto);
        
        if (order.getClientId() != null) {
            try {
                Client client = clientService.getClientById(order.getClientId());
                UserInfoDto clientInfo = new UserInfoDto();
                clientInfo.setId(client.getId());
                clientInfo.setPhone(client.getPhone());
                clientInfo.setNickname(client.getNickname());
                clientInfo.setAvatar(client.getAvatar());
                clientInfo.setCreditRating(client.getCreditRating());
                clientInfo.setOrderCount(client.getOrderCount());
                dto.setClientInfo(clientInfo);
            } catch (Exception e) {
                log.warn("获取客户信息失败，客户ID：{}", order.getClientId());
            }
        }
        
        if (order.getDriverId() != null) {
            try {
                Driver driver = driverService.getDriverById(order.getDriverId());
                UserInfoDto driverInfo = new UserInfoDto();
                driverInfo.setId(driver.getId());
                driverInfo.setPhone(driver.getPhone());
                driverInfo.setNickname(driver.getNickname());
                driverInfo.setAvatar(driver.getAvatar());
                driverInfo.setCreditRating(driver.getCreditRating());
                driverInfo.setOrderCount(driver.getOrderCount());
                driverInfo.setOnlineStatus(driver.getOnlineStatus());
                dto.setDriverInfo(driverInfo);
            } catch (Exception e) {
                log.warn("获取司机信息失败，司机ID：{}", order.getDriverId());
            }
        }
        
        return dto;
    }
}

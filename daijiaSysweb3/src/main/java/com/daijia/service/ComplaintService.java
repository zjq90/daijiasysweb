package com.daijia.service;

import com.daijia.entity.Complaint;
import com.daijia.repository.ComplaintRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 投诉服务类
 * 提供投诉相关的业务逻辑处理，包括增删改查、状态管理、统计分析等
 *
 * @author daijia
 * @version 1.0.0
 */
@Service
@Transactional
public class ComplaintService {

    private static final Logger logger = LoggerFactory.getLogger(ComplaintService.class);

    @Autowired
    private ComplaintRepository complaintRepository;

    /**
     * 生成唯一投诉编号
     * 格式：TS + 年月日时分秒 + 4位随机数
     *
     * @return 投诉编号
     */
    public String generateComplaintNo() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String random = String.format("%04d", new Random().nextInt(10000));
        return "TS" + timestamp + random;
    }

    /**
     * 创建新投诉
     *
     * @param complaint 投诉对象
     * @return 创建后的投诉对象
     */
    public Complaint createComplaint(Complaint complaint) {
        if (complaint.getComplaintNo() == null || complaint.getComplaintNo().isEmpty()) {
            complaint.setComplaintNo(generateComplaintNo());
        }
        if (complaint.getComplaintTime() == null) {
            complaint.setComplaintTime(LocalDateTime.now());
        }
        complaint.setCreateTime(LocalDateTime.now());
        complaint.setUpdateTime(LocalDateTime.now());
        Complaint savedComplaint = complaintRepository.save(complaint);
        logger.info("创建投诉成功，投诉编号：{}", savedComplaint.getComplaintNo());
        return savedComplaint;
    }

    /**
     * 根据ID查询投诉
     *
     * @param id 投诉ID
     * @return 投诉对象
     */
    public Complaint getComplaintById(Long id) {
        return complaintRepository.findById(id).orElse(null);
    }

    /**
     * 根据投诉编号查询投诉
     *
     * @param complaintNo 投诉编号
     * @return 投诉对象
     */
    public Complaint getComplaintByNo(String complaintNo) {
        return complaintRepository.findByComplaintNo(complaintNo);
    }

    /**
     * 更新投诉信息
     *
     * @param complaint 投诉对象
     * @return 更新后的投诉对象
     */
    public Complaint updateComplaint(Complaint complaint) {
        complaint.setUpdateTime(LocalDateTime.now());
        Complaint updatedComplaint = complaintRepository.save(complaint);
        logger.info("更新投诉成功，投诉编号：{}", updatedComplaint.getComplaintNo());
        return updatedComplaint;
    }

    /**
     * 删除投诉
     *
     * @param id 投诉ID
     * @return 是否删除成功
     */
    public boolean deleteComplaint(Long id) {
        Optional<Complaint> complaintOpt = complaintRepository.findById(id);
        if (complaintOpt.isPresent()) {
            complaintRepository.delete(complaintOpt.get());
            logger.info("删除投诉成功，投诉ID：{}", id);
            return true;
        }
        return false;
    }

    /**
     * 查询所有投诉
     *
     * @return 投诉列表
     */
    public List<Complaint> getAllComplaints() {
        return complaintRepository.findAll();
    }

    /**
     * 处理投诉
     *
     * @param complaintId  投诉ID
     * @param handlerId    处理人ID
     * @param handleResult 处理结果
     * @param isValid      是否有效投诉
     * @return 更新后的投诉对象
     */
    public Complaint handleComplaint(Long complaintId, Long handlerId, String handleResult, boolean isValid) {
        Complaint complaint = getComplaintById(complaintId);
        if (complaint != null && "PENDING".equals(complaint.getStatus())) {
            complaint.setStatus("RESOLVED");
            complaint.setHandlerId(handlerId);
            complaint.setHandleResult(handleResult);
            complaint.setHandleTime(LocalDateTime.now());
            complaint.setValid(isValid);
            return updateComplaint(complaint);
        }
        return null;
    }

    /**
     * 驳回投诉
     *
     * @param complaintId  投诉ID
     * @param handlerId    处理人ID
     * @param handleResult 驳回原因
     * @return 更新后的投诉对象
     */
    public Complaint rejectComplaint(Long complaintId, Long handlerId, String handleResult) {
        Complaint complaint = getComplaintById(complaintId);
        if (complaint != null) {
            complaint.setStatus("REJECTED");
            complaint.setHandlerId(handlerId);
            complaint.setHandleResult(handleResult);
            complaint.setHandleTime(LocalDateTime.now());
            complaint.setValid(false);
            return updateComplaint(complaint);
        }
        return null;
    }

    /**
     * 获取待处理的投诉列表
     *
     * @return 待处理投诉列表
     */
    public List<Complaint> getPendingComplaints() {
        return complaintRepository.findPendingComplaints();
    }

    /**
     * 获取投诉统计数据
     *
     * @return 统计数据Map
     */
    public Map<String, Object> getComplaintStats() {
        Map<String, Object> stats = new HashMap<>();
        
        // 各状态投诉数量
        List<Object[]> statusStats = complaintRepository.countComplaintsByStatus();
        Map<String, Long> statusMap = new HashMap<>();
        for (Object[] row : statusStats) {
            String status = (String) row[0];
            Long count = (Long) row[1];
            statusMap.put(status, count);
        }
        
        stats.put("total", complaintRepository.count());
        stats.put("pending", statusMap.getOrDefault("PENDING", 0L));
        stats.put("processing", statusMap.getOrDefault("PROCESSING", 0L));
        stats.put("resolved", statusMap.getOrDefault("RESOLVED", 0L));
        stats.put("rejected", statusMap.getOrDefault("REJECTED", 0L));
        
        // 各类型投诉数量
        List<Object[]> typeStats = complaintRepository.countComplaintsByType();
        List<Map<String, Object>> typeData = new ArrayList<>();
        for (Object[] row : typeStats) {
            Map<String, Object> typeItem = new HashMap<>();
            typeItem.put("type", row[0]);
            typeItem.put("count", row[1]);
            typeData.add(typeItem);
        }
        stats.put("typeDistribution", typeData);
        
        // 今日投诉数
        LocalDateTime startTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        stats.put("todayCount", complaintRepository.countByComplaintTimeBetween(startTime, endTime));
        
        return stats;
    }

    /**
     * 根据司机ID查询投诉
     *
     * @param driverId 司机ID
     * @return 投诉列表
     */
    public List<Complaint> getComplaintsByDriver(Long driverId) {
        return complaintRepository.findByDriverIdOrderByComplaintTimeDesc(driverId);
    }

    /**
     * 根据订单ID查询投诉
     *
     * @param orderId 订单ID
     * @return 投诉列表
     */
    public List<Complaint> getComplaintsByOrder(Long orderId) {
        return complaintRepository.findByOrderId(orderId);
    }

    /**
     * 根据状态查询投诉
     *
     * @param status 投诉状态
     * @return 投诉列表
     */
    public List<Complaint> getComplaintsByStatus(String status) {
        return complaintRepository.findByStatusOrderByComplaintTimeDesc(status);
    }

    /**
     * 生成测试投诉数据
     *
     * @param count 数量
     */
    public void generateTestComplaints(int count) {
        Random random = new Random();
        String[] types = {"DRIVER_ATTITUDE", "ROUTE_PROBLEM", "SAFETY_ISSUE", "PRICE_PROBLEM", "SERVICE_QUALITY", "OTHER"};
        String[] statuses = {"PENDING", "PROCESSING", "RESOLVED", "REJECTED"};
        
        for (int i = 0; i < count; i++) {
            Complaint complaint = new Complaint();
            complaint.setOrderId(1000L + random.nextInt(100));
            complaint.setPassengerId(1000L + random.nextInt(50));
            complaint.setDriverId(100L + random.nextInt(30));
            complaint.setComplaintType(types[random.nextInt(types.length)]);
            complaint.setStatus(statuses[random.nextInt(statuses.length)]);
            complaint.setComplaintTime(LocalDateTime.now().minusDays(random.nextInt(30)));
            
            if ("RESOLVED".equals(complaint.getStatus()) || "REJECTED".equals(complaint.getStatus())) {
                complaint.setHandlerId(1L);
                complaint.setHandleTime(LocalDateTime.now().minusDays(random.nextInt(30)));
                complaint.setHandleResult("已处理完成");
                complaint.setValid("RESOLVED".equals(complaint.getStatus()));
            }
            
            createComplaint(complaint);
        }
        
        logger.info("已生成{}条测试投诉数据", count);
    }
}

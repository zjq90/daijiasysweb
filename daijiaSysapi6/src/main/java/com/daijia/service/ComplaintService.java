package com.daijia.service;

import com.daijia.common.Constants;
import com.daijia.entity.Complaint;
import com.daijia.exception.BusinessException;
import com.daijia.repository.ComplaintRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ComplaintService {
    
    private final ComplaintRepository complaintRepository;
    
    @Transactional
    public Complaint createComplaint(Complaint complaint) {
        Optional<Complaint> existOpt = complaintRepository.findByOrderId(complaint.getOrderId());
        if (existOpt.isPresent()) {
            throw new BusinessException("该订单已投诉");
        }
        
        complaint.setStatus(Constants.COMPLAINT_STATUS_PENDING);
        complaint = complaintRepository.save(complaint);
        
        log.info("投诉单创建成功，订单ID：{}", complaint.getOrderId());
        return complaint;
    }
    
    public Complaint getById(Long id) {
        return complaintRepository.findById(id)
                .orElseThrow(() -> new BusinessException("投诉单不存在"));
    }
    
    public Optional<Complaint> getByOrderId(Long orderId) {
        return complaintRepository.findByOrderId(orderId);
    }
    
    public List<Complaint> getByComplainantId(Long complainantId) {
        return complaintRepository.findByComplainantIdOrderByCreateTimeDesc(complainantId);
    }
    
    public List<Complaint> getByRespondentId(Long respondentId) {
        return complaintRepository.findByRespondentIdOrderByCreateTimeDesc(respondentId);
    }
    
    @Transactional
    public Complaint processComplaint(Long id, Long handlerId, String handleResult, String status) {
        Complaint complaint = getById(id);
        
        complaint.setStatus(status);
        complaint.setHandlerId(handlerId);
        complaint.setHandleResult(handleResult);
        complaint.setHandleTime(LocalDateTime.now());
        
        complaint = complaintRepository.save(complaint);
        log.info("投诉单处理完成，ID：{}", id);
        return complaint;
    }
}

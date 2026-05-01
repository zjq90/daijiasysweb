package com.daijia.service;

import com.daijia.entity.Complaint;
import com.daijia.repository.ComplaintRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 投诉单服务层
 * 处理投诉单相关的业务逻辑
 */
@Service
@Transactional
public class ComplaintService {

    @Autowired
    private ComplaintRepository complaintRepository;

    /**
     * 查询所有投诉单
     * @return 投诉单列表
     */
    public List<Complaint> findAll() {
        return complaintRepository.findAll();
    }

    /**
     * 根据ID查询投诉单
     * @param id 投诉单ID
     * @return 投诉单信息
     */
    public Optional<Complaint> findById(Long id) {
        return complaintRepository.findById(id);
    }

    /**
     * 根据状态查询投诉单
     * @param status 状态
     * @return 投诉单列表
     */
    public List<Complaint> findByStatus(String status) {
        return complaintRepository.findByStatusOrderByCreateTimeDesc(status);
    }

    /**
     * 根据司机ID查询投诉单
     * @param driverId 司机ID
     * @return 投诉单列表
     */
    public List<Complaint> findByDriverId(Long driverId) {
        return complaintRepository.findByDriverIdOrderByCreateTimeDesc(driverId);
    }

    /**
     * 根据投诉单号查询
     * @param complaintNo 投诉单号
     * @return 投诉单
     */
    public Complaint findByComplaintNo(String complaintNo) {
        return complaintRepository.findByComplaintNo(complaintNo);
    }

    /**
     * 保存投诉单
     * @param complaint 投诉单
     * @return 保存后的投诉单
     */
    public Complaint save(Complaint complaint) {
        // 生成投诉单号
        if (complaint.getComplaintNo() == null || complaint.getComplaintNo().isEmpty()) {
            String complaintNo = "CP" + System.currentTimeMillis();
            complaint.setComplaintNo(complaintNo);
        }
        return complaintRepository.save(complaint);
    }

    /**
     * 处理投诉单
     * @param id 投诉单ID
     * @param status 新状态
     * @param handleRemark 处理备注
     * @param creditDeduct 信用扣分
     * @return 更新后的投诉单
     */
    public Complaint handleComplaint(Long id, String status, String handleRemark, Integer creditDeduct) {
        Optional<Complaint> complaintOpt = complaintRepository.findById(id);
        if (complaintOpt.isPresent()) {
            Complaint complaint = complaintOpt.get();
            complaint.setStatus(status);
            complaint.setHandleRemark(handleRemark);
            if (creditDeduct != null) {
                complaint.setCreditDeduct(creditDeduct);
            }
            complaint.setHandleTime(LocalDateTime.now());
            return complaintRepository.save(complaint);
        }
        throw new RuntimeException("投诉单不存在");
    }

    /**
     * 删除投诉单
     * @param id 投诉单ID
     */
    public void deleteById(Long id) {
        complaintRepository.deleteById(id);
    }
}

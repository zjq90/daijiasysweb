package com.daijia.service;

import com.daijia.common.Constants;
import com.daijia.entity.EmergencyHelp;
import com.daijia.entity.Promotion;
import com.daijia.exception.BusinessException;
import com.daijia.repository.EmergencyHelpRepository;
import com.daijia.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommonService {
    
    private final EmergencyHelpRepository emergencyHelpRepository;
    private final PromotionRepository promotionRepository;
    
    @Transactional
    public EmergencyHelp createEmergencyHelp(EmergencyHelp help) {
        help.setStatus(Constants.EMERGENCY_STATUS_PENDING);
        help.setRecordingStatus(Constants.RECORDING_STATUS_NOT_STARTED);
        help = emergencyHelpRepository.save(help);
        
        log.info("紧急求助创建成功，用户ID：{}", help.getUserId());
        return help;
    }
    
    public EmergencyHelp getEmergencyHelpById(Long id) {
        return emergencyHelpRepository.findById(id)
                .orElseThrow(() -> new BusinessException("求助记录不存在"));
    }
    
    public List<EmergencyHelp> getEmergencyHelpByUserId(Long userId) {
        return emergencyHelpRepository.findByUserIdOrderByCreateTimeDesc(userId);
    }
    
    @Transactional
    public EmergencyHelp updateEmergencyHelpStatus(Long id, String recordingStatus) {
        EmergencyHelp help = getEmergencyHelpById(id);
        help.setRecordingStatus(recordingStatus);
        help = emergencyHelpRepository.save(help);
        log.info("紧急求助状态更新，ID：{}，状态：{}", id, recordingStatus);
        return help;
    }
    
    @Transactional
    public EmergencyHelp processEmergencyHelp(Long id, Long handlerId, String handleResult) {
        EmergencyHelp help = getEmergencyHelpById(id);
        help.setStatus(Constants.EMERGENCY_STATUS_RESOLVED);
        help.setHandlerId(handlerId);
        help.setHandleResult(handleResult);
        help.setHandleTime(LocalDateTime.now());
        help = emergencyHelpRepository.save(help);
        log.info("紧急求助处理完成，ID：{}", id);
        return help;
    }
    
    public List<Promotion> getActivePromotions(String userType) {
        List<String> targetUsers;
        if ("CLIENT".equals(userType)) {
            targetUsers = Arrays.asList(Constants.TARGET_USER_ALL, Constants.TARGET_USER_CLIENT, Constants.TARGET_USER_NEW);
        } else if ("DRIVER".equals(userType)) {
            targetUsers = Arrays.asList(Constants.TARGET_USER_ALL, Constants.TARGET_USER_DRIVER);
        } else {
            targetUsers = Arrays.asList(Constants.TARGET_USER_ALL);
        }
        
        return promotionRepository.findActivePromotions(
                Constants.PROMOTION_STATUS_ACTIVE,
                LocalDateTime.now(),
                targetUsers
        );
    }
    
    public Promotion getPromotionById(Long id) {
        return promotionRepository.findById(id)
                .orElseThrow(() -> new BusinessException("营销活动不存在"));
    }
}

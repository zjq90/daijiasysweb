package com.daijia.service;

import com.daijia.entity.EmergencyHelp;
import com.daijia.entity.Order;
import com.daijia.entity.User;
import com.daijia.entity.Driver;
import com.daijia.entity.Admin;
import com.daijia.repository.EmergencyHelpRepository;
import com.daijia.repository.OrderRepository;
import com.daijia.repository.UserRepository;
import com.daijia.repository.DriverRepository;
import com.daijia.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * 安全求助服务类
 * 
 * 处理紧急求助业务逻辑
 * 
 * 求助类型：
 * 0: 一般求助
 * 1: 用户/乘客求助
 * 2: 司机求助
 * 3: 系统自动检测异常
 * 
 * 求助状态：
 * 0: 待处理
 * 1: 处理中
 * 2: 已处理
 * 3: 已关闭
 * 
 * @author daijia
 * @version 1.0.0
 */
@Service
public class EmergencyHelpService {

    @Autowired
    private EmergencyHelpRepository emergencyHelpRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private AdminRepository adminRepository;

    /**
     * 生成求助编号
     * 格式：EH + 年月日时分秒 + 4位随机数
     * 
     * @return 求助编号
     */
    public String generateHelpNo() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String timestamp = LocalDateTime.now().format(formatter);
        Random random = new Random();
        int suffix = random.nextInt(10000);
        return "EH" + timestamp + String.format("%04d", suffix);
    }

    /**
     * 创建紧急求助
     * 
     * @param help 求助对象
     * @return EmergencyHelp
     */
    @Transactional
    public EmergencyHelp createHelp(EmergencyHelp help) {
        if (help.getHelpNo() == null || help.getHelpNo().isEmpty()) {
            help.setHelpNo(generateHelpNo());
        }
        if (help.getStatus() == null) {
            help.setStatus(0);
        }
        return emergencyHelpRepository.save(help);
    }

    /**
     * 根据ID获取求助记录
     * 
     * @param id 求助ID
     * @return Optional<EmergencyHelp>
     */
    public Optional<EmergencyHelp> findById(Long id) {
        Optional<EmergencyHelp> helpOpt = emergencyHelpRepository.findById(id);
        helpOpt.ifPresent(this::fillHelpDetails);
        return helpOpt;
    }

    /**
     * 根据求助编号获取求助记录
     * 
     * @param helpNo 求助编号
     * @return Optional<EmergencyHelp>
     */
    public Optional<EmergencyHelp> findByHelpNo(String helpNo) {
        Optional<EmergencyHelp> helpOpt = emergencyHelpRepository.findByHelpNo(helpNo);
        helpOpt.ifPresent(this::fillHelpDetails);
        return helpOpt;
    }

    /**
     * 获取所有求助记录
     * 
     * @return List<EmergencyHelp>
     */
    public List<EmergencyHelp> findAll() {
        List<EmergencyHelp> helps = emergencyHelpRepository.findAll();
        helps.forEach(this::fillHelpDetails);
        return helps;
    }

    /**
     * 分页获取求助记录
     * 
     * @param pageable 分页参数
     * @return Page<EmergencyHelp>
     */
    public Page<EmergencyHelp> findAll(Pageable pageable) {
        Page<EmergencyHelp> helps = emergencyHelpRepository.findAll(pageable);
        helps.forEach(this::fillHelpDetails);
        return helps;
    }

    /**
     * 根据状态获取求助记录
     * 
     * @param status 状态
     * @return List<EmergencyHelp>
     */
    public List<EmergencyHelp> findByStatus(Integer status) {
        List<EmergencyHelp> helps = emergencyHelpRepository.findByStatus(status);
        helps.forEach(this::fillHelpDetails);
        return helps;
    }

    /**
     * 获取待处理和处理中的求助记录
     * 
     * @return List<EmergencyHelp>
     */
    public List<EmergencyHelp> findPendingAndProcessing() {
        List<EmergencyHelp> helps = emergencyHelpRepository.findPendingAndProcessing();
        helps.forEach(this::fillHelpDetails);
        return helps;
    }

    /**
     * 开始处理求助
     * 
     * @param helpId 求助ID
     * @param adminId 管理员ID
     * @return EmergencyHelp
     */
    @Transactional
    public EmergencyHelp startProcessing(Long helpId, Long adminId) {
        Optional<EmergencyHelp> helpOpt = emergencyHelpRepository.findById(helpId);
        if (helpOpt.isPresent()) {
            EmergencyHelp help = helpOpt.get();
            if (help.getStatus() == 0) {
                help.setStatus(1);
                help.setAdminId(adminId);
                return emergencyHelpRepository.save(help);
            }
        }
        return null;
    }

    /**
     * 完成处理求助
     * 
     * @param helpId 求助ID
     * @param handleResult 处理结果
     * @return EmergencyHelp
     */
    @Transactional
    public EmergencyHelp completeProcessing(Long helpId, String handleResult) {
        Optional<EmergencyHelp> helpOpt = emergencyHelpRepository.findById(helpId);
        if (helpOpt.isPresent()) {
            EmergencyHelp help = helpOpt.get();
            if (help.getStatus() == 1) {
                help.setStatus(2);
                help.setHandleResult(handleResult);
                help.setHandleTime(LocalDateTime.now());
                return emergencyHelpRepository.save(help);
            }
        }
        return null;
    }

    /**
     * 关闭求助
     * 
     * @param helpId 求助ID
     * @return EmergencyHelp
     */
    @Transactional
    public EmergencyHelp closeHelp(Long helpId) {
        Optional<EmergencyHelp> helpOpt = emergencyHelpRepository.findById(helpId);
        if (helpOpt.isPresent()) {
            EmergencyHelp help = helpOpt.get();
            help.setStatus(3);
            return emergencyHelpRepository.save(help);
        }
        return null;
    }

    /**
     * 保存求助记录
     * 
     * @param help 求助对象
     * @return EmergencyHelp
     */
    @Transactional
    public EmergencyHelp save(EmergencyHelp help) {
        return emergencyHelpRepository.save(help);
    }

    /**
     * 根据状态统计求助数量
     * 
     * @param status 状态
     * @return 数量
     */
    public long countByStatus(Integer status) {
        return emergencyHelpRepository.countByStatus(status);
    }

    /**
     * 填充求助详情（订单、用户、司机、管理员信息）
     * 
     * @param help 求助对象
     */
    private void fillHelpDetails(EmergencyHelp help) {
        if (help.getOrderId() != null) {
            Optional<Order> orderOpt = orderRepository.findById(help.getOrderId());
            orderOpt.ifPresent(help::setOrder);
        }
        if (help.getUserId() != null) {
            Optional<User> userOpt = userRepository.findById(help.getUserId());
            userOpt.ifPresent(help::setUser);
        }
        if (help.getDriverId() != null) {
            Optional<Driver> driverOpt = driverRepository.findById(help.getDriverId());
            driverOpt.ifPresent(help::setDriver);
        }
        if (help.getAdminId() != null) {
            Optional<Admin> adminOpt = adminRepository.findById(help.getAdminId());
            adminOpt.ifPresent(help::setAdmin);
        }
    }

    /**
     * 获取求助类型文本
     * 
     * @param helpType 类型码
     * @return 类型文本
     */
    public String getHelpTypeText(Integer helpType) {
        if (helpType == null) return "未知";
        switch (helpType) {
            case 0: return "一般求助";
            case 1: return "乘客求助";
            case 2: return "司机求助";
            case 3: return "系统检测";
            default: return "未知";
        }
    }

    /**
     * 获取状态文本
     * 
     * @param status 状态码
     * @return 状态文本
     */
    public String getStatusText(Integer status) {
        if (status == null) return "未知";
        switch (status) {
            case 0: return "待处理";
            case 1: return "处理中";
            case 2: return "已处理";
            case 3: return "已关闭";
            default: return "未知";
        }
    }

    /**
     * 获取状态对应的Bootstrap状态类
     * 
     * @param status 状态码
     * @return Bootstrap样式类
     */
    public String getStatusClass(Integer status) {
        if (status == null) return "secondary";
        switch (status) {
            case 0: return "danger";
            case 1: return "warning";
            case 2: return "success";
            case 3: return "secondary";
            default: return "secondary";
        }
    }
}

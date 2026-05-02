package com.daijia.service;

import com.daijia.entity.Admin;
import com.daijia.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 管理员服务类
 * 
 * 处理管理员相关业务逻辑
 * 
 * @author daijia
 * @version 1.0.0
 */
@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    /**
     * 管理员登录
     * 
     * @param username 用户名
     * @param password 密码
     * @return Admin 管理员对象，登录失败返回null
     */
    public Admin login(String username, String password) {
        Optional<Admin> adminOpt = adminRepository.findByUsernameAndPassword(username, password);
        return adminOpt.orElse(null);
    }

    /**
     * 根据ID获取管理员
     * 
     * @param id 管理员ID
     * @return Optional<Admin>
     */
    public Optional<Admin> findById(Long id) {
        return adminRepository.findById(id);
    }

    /**
     * 根据用户名获取管理员
     * 
     * @param username 用户名
     * @return Optional<Admin>
     */
    public Optional<Admin> findByUsername(String username) {
        return adminRepository.findByUsername(username);
    }

    /**
     * 获取所有管理员
     * 
     * @return List<Admin>
     */
    public List<Admin> findAll() {
        return adminRepository.findAll();
    }

    /**
     * 保存管理员
     * 
     * @param admin 管理员对象
     * @return Admin
     */
    @Transactional
    public Admin save(Admin admin) {
        return adminRepository.save(admin);
    }

    /**
     * 根据ID删除管理员
     * 
     * @param id 管理员ID
     */
    @Transactional
    public void deleteById(Long id) {
        adminRepository.deleteById(id);
    }

    /**
     * 检查用户名是否存在
     * 
     * @param username 用户名
     * @return boolean
     */
    public boolean existsByUsername(String username) {
        return adminRepository.existsByUsername(username);
    }
}

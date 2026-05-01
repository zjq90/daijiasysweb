
package com.daijia.service;

import com.daijia.entity.SysUser;
import com.daijia.repository.SysUserRepository;
import com.daijia.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SysUserService {
    
    @Autowired
    private SysUserRepository sysUserRepository;
    
    public List<SysUser> findAll() {
        return sysUserRepository.findAll();
    }
    
    public Optional<SysUser> findById(Long id) {
        return sysUserRepository.findById(id);
    }
    
    public Optional<SysUser> findByUsername(String username) {
        return sysUserRepository.findByUsername(username);
    }
    
    public SysUser login(String username, String password) {
        String md5Password = MD5Util.md5(password);
        return sysUserRepository.findByUsernameAndPassword(username, md5Password).orElse(null);
    }
    
    @Transactional
    public SysUser save(SysUser sysUser) {
        if (sysUser.getId() == null) {
            if (sysUserRepository.existsByUsername(sysUser.getUsername())) {
                throw new RuntimeException("用户名已存在");
            }
            sysUser.setPassword(MD5Util.md5(sysUser.getPassword()));
        } else {
            Optional<SysUser> existing = sysUserRepository.findById(sysUser.getId());
            if (existing.isPresent()) {
                if (!existing.get().getPassword().equals(sysUser.getPassword())) {
                    sysUser.setPassword(MD5Util.md5(sysUser.getPassword()));
                }
            }
        }
        return sysUserRepository.save(sysUser);
    }
    
    @Transactional
    public void deleteById(Long id) {
        sysUserRepository.deleteById(id);
    }
    
    public boolean existsByUsername(String username) {
        return sysUserRepository.existsByUsername(username);
    }
}


package com.daijia.service;

import com.daijia.entity.AppUser;
import com.daijia.repository.AppUserRepository;
import com.daijia.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AppUserService {
    
    @Autowired
    private AppUserRepository appUserRepository;
    
    public List<AppUser> findAll() {
        return appUserRepository.findAll();
    }
    
    public Optional<AppUser> findById(Long id) {
        return appUserRepository.findById(id);
    }
    
    public Optional<AppUser> findByUsername(String username) {
        return appUserRepository.findByUsername(username);
    }
    
    @Transactional
    public AppUser save(AppUser appUser) {
        if (appUser.getId() == null) {
            if (appUserRepository.existsByUsername(appUser.getUsername())) {
                throw new RuntimeException("用户名已存在");
            }
            appUser.setPassword(MD5Util.md5(appUser.getPassword()));
        } else {
            Optional<AppUser> existing = appUserRepository.findById(appUser.getId());
            if (existing.isPresent()) {
                if (!existing.get().getPassword().equals(appUser.getPassword())) {
                    appUser.setPassword(MD5Util.md5(appUser.getPassword()));
                }
            }
        }
        return appUserRepository.save(appUser);
    }
    
    @Transactional
    public void deleteById(Long id) {
        appUserRepository.deleteById(id);
    }
    
    public boolean existsByUsername(String username) {
        return appUserRepository.existsByUsername(username);
    }
}

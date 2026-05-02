package com.daijia.repository;

import com.daijia.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    
    Optional<Client> findByPhone(String phone);
    
    Optional<Client> findByPhoneAndDeleted(String phone, Integer deleted);
    
    Optional<Client> findByIdAndDeleted(Long id, Integer deleted);
    
    Optional<Client> findByLoginToken(String loginToken);
    
    boolean existsByPhone(String phone);
}

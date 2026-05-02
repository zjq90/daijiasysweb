package com.daijia.service;

import cn.hutool.core.util.StrUtil;
import com.daijia.common.Constants;
import com.daijia.dto.LoginDto;
import com.daijia.entity.Client;
import com.daijia.exception.BusinessException;
import com.daijia.repository.ClientRepository;
import com.daijia.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientService {
    
    private final ClientRepository clientRepository;
    
    public Map<String, Object> login(LoginDto dto) {
        Client client = clientRepository.findByPhoneAndDeleted(dto.getPhone(), Constants.DELETED_NO)
                .orElseGet(() -> {
                    Client newClient = new Client();
                    newClient.setPhone(dto.getPhone());
                    newClient.setNickname("用户" + dto.getPhone().substring(7));
                    newClient.setStatus(Constants.STATUS_ENABLED);
                    return clientRepository.save(newClient);
                });
        
        if ("PASSWORD".equals(dto.getLoginType())) {
            if (StrUtil.isBlank(client.getPassword())) {
                throw new BusinessException("请先设置密码或使用验证码登录");
            }
            String inputPasswordMd5 = CommonUtil.md5(dto.getPassword() != null ? dto.getPassword() : "");
            if (!client.getPassword().equals(inputPasswordMd5)) {
                throw new BusinessException("密码错误");
            }
        }
        
        String token = CommonUtil.generateToken();
        client.setLoginToken(token);
        clientRepository.save(client);
        
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("clientId", client.getId());
        result.put("phone", client.getPhone());
        result.put("nickname", client.getNickname());
        result.put("avatar", client.getAvatar());
        result.put("isVerified", client.getIsVerified());
        result.put("creditRating", client.getCreditRating());
        return result;
    }
    
    @Transactional
    public void logout(Long clientId) {
        Client client = getClientById(clientId);
        client.setLoginToken(null);
        clientRepository.save(client);
    }
    
    public Client getClientById(Long clientId) {
        return clientRepository.findByIdAndDeleted(clientId, Constants.DELETED_NO)
                .orElseThrow(() -> new BusinessException("用户不存在"));
    }
    
    public Client getClientByToken(String token) {
        if (StrUtil.isBlank(token)) {
            throw new BusinessException("请先登录");
        }
        return clientRepository.findByLoginToken(token)
                .orElseThrow(() -> new BusinessException("登录已过期，请重新登录"));
    }
    
    @Transactional
    public Client updateProfile(Long clientId, Map<String, Object> params) {
        Client client = getClientById(clientId);
        
        if (params.containsKey("nickname")) {
            client.setNickname((String) params.get("nickname"));
        }
        if (params.containsKey("avatar")) {
            client.setAvatar((String) params.get("avatar"));
        }
        if (params.containsKey("realName")) {
            client.setRealName((String) params.get("realName"));
        }
        if (params.containsKey("idCard")) {
            client.setIdCard((String) params.get("idCard"));
        }
        if (params.containsKey("isVerified")) {
            client.setIsVerified((Integer) params.get("isVerified"));
        }
        if (params.containsKey("password")) {
            client.setPassword(CommonUtil.md5((String) params.get("password")));
        }
        
        return clientRepository.save(client);
    }
}

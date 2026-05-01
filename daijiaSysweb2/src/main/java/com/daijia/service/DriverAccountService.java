package com.daijia.service;

import com.daijia.entity.DriverAccount;
import com.daijia.repository.DriverAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 代驾账户服务层
 * 处理账户相关的业务逻辑
 */
@Service
@Transactional
public class DriverAccountService {

    @Autowired
    private DriverAccountRepository driverAccountRepository;

    /**
     * 查询所有账户
     * @return 账户列表
     */
    public List<DriverAccount> findAll() {
        return driverAccountRepository.findAll();
    }

    /**
     * 根据ID查询账户
     * @param id 账户ID
     * @return 账户信息
     */
    public Optional<DriverAccount> findById(Long id) {
        return driverAccountRepository.findById(id);
    }

    /**
     * 根据手机号查询账户
     * @param phone 手机号
     * @return 账户信息
     */
    public DriverAccount findByPhone(String phone) {
        return driverAccountRepository.findByPhone(phone);
    }

    /**
     * 根据状态查询账户
     * @param status 状态
     * @return 账户列表
     */
    public List<DriverAccount> findByStatus(String status) {
        return driverAccountRepository.findByStatus(status);
    }

    /**
     * 根据姓名模糊查询
     * @param name 姓名关键字
     * @return 账户列表
     */
    public List<DriverAccount> findByNameLike(String name) {
        return driverAccountRepository.findByDriverNameContaining(name);
    }

    /**
     * 保存账户
     * @param account 账户信息
     * @return 保存后的账户
     */
    public DriverAccount save(DriverAccount account) {
        // 检查手机号是否已存在（新增时）
        if (account.getId() == null && driverAccountRepository.existsByPhone(account.getPhone())) {
            throw new RuntimeException("手机号已存在");
        }
        return driverAccountRepository.save(account);
    }

    /**
     * 更新账户
     * @param account 账户信息
     * @return 更新后的账户
     */
    public DriverAccount update(DriverAccount account) {
        if (!driverAccountRepository.existsById(account.getId())) {
            throw new RuntimeException("账户不存在");
        }
        return driverAccountRepository.save(account);
    }

    /**
     * 删除账户
     * @param id 账户ID
     */
    public void deleteById(Long id) {
        if (!driverAccountRepository.existsById(id)) {
            throw new RuntimeException("账户不存在");
        }
        driverAccountRepository.deleteById(id);
    }

    /**
     * 冻结账户
     * @param id 账户ID
     * @return 更新后的账户
     */
    public DriverAccount freezeAccount(Long id) {
        Optional<DriverAccount> accountOpt = driverAccountRepository.findById(id);
        if (accountOpt.isPresent()) {
            DriverAccount account = accountOpt.get();
            account.setStatus("FROZEN");
            return driverAccountRepository.save(account);
        }
        throw new RuntimeException("账户不存在");
    }

    /**
     * 解冻账户
     * @param id 账户ID
     * @return 更新后的账户
     */
    public DriverAccount unfreezeAccount(Long id) {
        Optional<DriverAccount> accountOpt = driverAccountRepository.findById(id);
        if (accountOpt.isPresent()) {
            DriverAccount account = accountOpt.get();
            account.setStatus("ACTIVE");
            return driverAccountRepository.save(account);
        }
        throw new RuntimeException("账户不存在");
    }
}

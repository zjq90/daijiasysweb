package com.daijia.service;

import com.daijia.entity.CreditLevel;
import com.daijia.repository.CreditLevelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 信用等级服务层
 * 处理信用等级相关的业务逻辑
 */
@Service
@Transactional
public class CreditLevelService {

    @Autowired
    private CreditLevelRepository creditLevelRepository;

    /**
     * 查询所有信用等级
     * @return 信用等级列表
     */
    public List<CreditLevel> findAll() {
        return creditLevelRepository.findAll();
    }

    /**
     * 根据ID查询信用等级
     * @param id 等级ID
     * @return 信用等级
     */
    public Optional<CreditLevel> findById(Long id) {
        return creditLevelRepository.findById(id);
    }

    /**
     * 根据等级代码查询
     * @param levelCode 等级代码
     * @return 信用等级
     */
    public Optional<CreditLevel> findByLevelCode(String levelCode) {
        return creditLevelRepository.findByLevelCode(levelCode);
    }

    /**
     * 根据分数查询对应的信用等级
     * @param score 分数
     * @return 信用等级
     */
    public CreditLevel findByScore(Integer score) {
        return creditLevelRepository.findByMinScoreLessThanEqualAndMaxScoreGreaterThanEqual(score, score);
    }

    /**
     * 保存信用等级
     * @param level 信用等级
     * @return 保存后的信用等级
     */
    public CreditLevel save(CreditLevel level) {
        return creditLevelRepository.save(level);
    }

    /**
     * 更新信用等级
     * @param level 信用等级
     * @return 更新后的信用等级
     */
    public CreditLevel update(CreditLevel level) {
        if (!creditLevelRepository.existsById(level.getId())) {
            throw new RuntimeException("信用等级不存在");
        }
        return creditLevelRepository.save(level);
    }

    /**
     * 删除信用等级
     * @param id 等级ID
     */
    public void deleteById(Long id) {
        if (!creditLevelRepository.existsById(id)) {
            throw new RuntimeException("信用等级不存在");
        }
        creditLevelRepository.deleteById(id);
    }
}

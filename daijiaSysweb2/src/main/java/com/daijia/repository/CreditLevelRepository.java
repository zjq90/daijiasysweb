package com.daijia.repository;

import com.daijia.entity.CreditLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 信用等级数据访问层
 * 继承JpaRepository，提供基本的CRUD操作
 */
@Repository
public interface CreditLevelRepository extends JpaRepository<CreditLevel, Long> {

    /**
     * 根据等级代码查询
     * @param levelCode 等级代码
     * @return 信用等级
     */
    Optional<CreditLevel> findByLevelCode(String levelCode);

    /**
     * 根据分数查询对应的信用等级
     * @param score 分数
     * @return 信用等级
     */
    CreditLevel findByMinScoreLessThanEqualAndMaxScoreGreaterThanEqual(Integer score, Integer score2);
}

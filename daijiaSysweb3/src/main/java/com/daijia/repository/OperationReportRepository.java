package com.daijia.repository;

import com.daijia.entity.OperationReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * 运营报表数据访问层
 * 提供运营报表数据的增删改查及统计功能
 *
 * @author daijia
 * @version 1.0.0
 */
@Repository
public interface OperationReportRepository extends JpaRepository<OperationReport, Long> {

    /**
     * 根据报表日期和类型查询报表
     *
     * @param reportDate 报表日期
     * @param reportType 报表类型
     * @return 报表对象
     */
    OperationReport findByReportDateAndReportType(LocalDate reportDate, String reportType);

    /**
     * 查询指定日期范围的报表
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @param reportType 报表类型
     * @return 报表列表
     */
    List<OperationReport> findByReportDateBetweenAndReportTypeOrderByReportDateAsc(
            LocalDate startDate, LocalDate endDate, String reportType);

    /**
     * 查询日报列表（按日期倒序）
     *
     * @return 日报列表
     */
    List<OperationReport> findByReportTypeOrderByReportDateDesc(String reportType);

    /**
     * 查询最近N天的日报数据
     *
     * @param days 天数
     * @return 日报列表
     */
    @Query("SELECT r FROM OperationReport r WHERE r.reportType = 'DAILY' AND r.reportDate >= :startDate ORDER BY r.reportDate ASC")
    List<OperationReport> findRecentDailyReports(@Param("startDate") LocalDate startDate);

    /**
     * 统计指定日期范围的总订单数
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 总订单数
     */
    @Query("SELECT SUM(r.totalOrders) FROM OperationReport r WHERE r.reportType = 'DAILY' AND r.reportDate BETWEEN :startDate AND :endDate")
    Long sumTotalOrders(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    /**
     * 统计指定日期范围的总营收
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 总营收
     */
    @Query("SELECT SUM(r.totalRevenue) FROM OperationReport r WHERE r.reportType = 'DAILY' AND r.reportDate BETWEEN :startDate AND :endDate")
    java.math.BigDecimal sumTotalRevenue(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    /**
     * 统计指定日期范围的平均成交率
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 平均成交率
     */
    @Query("SELECT AVG(r.completionRate) FROM OperationReport r WHERE r.reportType = 'DAILY' AND r.reportDate BETWEEN :startDate AND :endDate")
    java.math.BigDecimal avgCompletionRate(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    /**
     * 统计指定日期范围的平均投诉率
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 平均投诉率
     */
    @Query("SELECT AVG(r.complaintRate) FROM OperationReport r WHERE r.reportType = 'DAILY' AND r.reportDate BETWEEN :startDate AND :endDate")
    java.math.BigDecimal avgComplaintRate(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    /**
     * 获取订单趋势数据（最近7天）
     *
     * @return 订单趋势数据列表（日期, 总订单数, 完成订单数, 取消订单数）
     */
    @Query("SELECT r.reportDate, r.totalOrders, r.completedOrders, r.cancelledOrders FROM OperationReport r WHERE r.reportType = 'DAILY' ORDER BY r.reportDate DESC")
    List<Object[]> getOrderTrendData();

    /**
     * 获取收入趋势数据（最近7天）
     *
     * @return 收入趋势数据列表（日期, 总营收, 平均订单金额）
     */
    @Query("SELECT r.reportDate, r.totalRevenue, r.avgOrderAmount FROM OperationReport r WHERE r.reportType = 'DAILY' ORDER BY r.reportDate DESC")
    List<Object[]> getRevenueTrendData();
}

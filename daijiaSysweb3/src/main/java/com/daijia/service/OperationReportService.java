package com.daijia.service;

import com.daijia.entity.OperationReport;
import com.daijia.repository.OperationReportRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

/**
 * 运营报表服务类
 * 提供运营报表相关的业务逻辑处理，包括报表生成、统计分析等
 *
 * @author daijia
 * @version 1.0.0
 */
@Service
@Transactional
public class OperationReportService {

    private static final Logger logger = LoggerFactory.getLogger(OperationReportService.class);

    @Autowired
    private OperationReportRepository operationReportRepository;

    /**
     * 创建新报表
     *
     * @param report 报表对象
     * @return 创建后的报表对象
     */
    public OperationReport createReport(OperationReport report) {
        report.setCreateTime(LocalDateTime.now());
        report.setUpdateTime(LocalDateTime.now());
        OperationReport savedReport = operationReportRepository.save(report);
        logger.info("创建运营报表成功，报表日期：{}", savedReport.getReportDate());
        return savedReport;
    }

    /**
     * 根据ID查询报表
     *
     * @param id 报表ID
     * @return 报表对象
     */
    public OperationReport getReportById(Long id) {
        return operationReportRepository.findById(id).orElse(null);
    }

    /**
     * 根据日期和类型查询报表
     *
     * @param reportDate 报表日期
     * @param reportType 报表类型
     * @return 报表对象
     */
    public OperationReport getReportByDateAndType(LocalDate reportDate, String reportType) {
        return operationReportRepository.findByReportDateAndReportType(reportDate, reportType);
    }

    /**
     * 更新报表信息
     *
     * @param report 报表对象
     * @return 更新后的报表对象
     */
    public OperationReport updateReport(OperationReport report) {
        report.setUpdateTime(LocalDateTime.now());
        OperationReport updatedReport = operationReportRepository.save(report);
        logger.info("更新运营报表成功，报表日期：{}", updatedReport.getReportDate());
        return updatedReport;
    }

    /**
     * 删除报表
     *
     * @param id 报表ID
     * @return 是否删除成功
     */
    public boolean deleteReport(Long id) {
        Optional<OperationReport> reportOpt = operationReportRepository.findById(id);
        if (reportOpt.isPresent()) {
            operationReportRepository.delete(reportOpt.get());
            logger.info("删除运营报表成功，报表ID：{}", id);
            return true;
        }
        return false;
    }

    /**
     * 查询所有报表
     *
     * @return 报表列表
     */
    public List<OperationReport> getAllReports() {
        return operationReportRepository.findAll();
    }

    /**
     * 生成日报表
     *
     * @param date 报表日期
     * @return 生成的报表对象
     */
    public OperationReport generateDailyReport(LocalDate date) {
        // 检查是否已存在该日期的日报
        OperationReport existingReport = getReportByDateAndType(date, "DAILY");
        if (existingReport != null) {
            logger.info("日报表已存在，日期：{}", date);
            return existingReport;
        }

        // 创建新的日报表
        OperationReport report = new OperationReport();
        report.setReportDate(date);
        report.setReportType("DAILY");
        
        // 使用随机测试数据（实际项目中应该从订单表等统计）
        Random random = new Random();
        
        // 订单数据
        int totalOrders = 100 + random.nextInt(200);  // 100-299单
        int completedOrders = (int) (totalOrders * (0.85 + random.nextDouble() * 0.1));  // 85%-95%完成率
        int cancelledOrders = totalOrders - completedOrders;
        
        report.setTotalOrders(totalOrders);
        report.setCompletedOrders(completedOrders);
        report.setCancelledOrders(cancelledOrders);
        
        // 计算比率
        if (totalOrders > 0) {
            report.setCompletionRate(new BigDecimal(completedOrders)
                    .divide(new BigDecimal(totalOrders), 4, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100")));
            report.setCancellationRate(new BigDecimal(cancelledOrders)
                    .divide(new BigDecimal(totalOrders), 4, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100")));
        }

        // 收入数据
        BigDecimal totalRevenue = new BigDecimal(5000 + random.nextInt(15000));
        report.setTotalRevenue(totalRevenue);
        if (completedOrders > 0) {
            report.setAvgOrderAmount(totalRevenue.divide(new BigDecimal(completedOrders), 2, RoundingMode.HALF_UP));
        }

        // 司机数据
        report.setOnlineDrivers(30 + random.nextInt(30));
        report.setActiveDrivers(20 + random.nextInt(30));
        report.setAvgDriverIncome(totalRevenue.divide(new BigDecimal(report.getActiveDrivers()), 2, RoundingMode.HALF_UP));

        // 投诉数据
        int complaintCount = random.nextInt(5);
        report.setComplaintCount(complaintCount);
        if (completedOrders > 0) {
            report.setComplaintRate(new BigDecimal(complaintCount)
                    .divide(new BigDecimal(completedOrders), 4, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100")));
        }

        // 评价数据
        report.setAvgRating(new BigDecimal("4.5").add(new BigDecimal(random.nextDouble() * 0.5)));
        report.setFiveStarCount((int) (completedOrders * (0.6 + random.nextDouble() * 0.3)));

        // 营销数据
        report.setPromotionOrders((int) (totalOrders * (0.2 + random.nextDouble() * 0.3)));
        report.setTotalDiscount(new BigDecimal(500 + random.nextInt(1500)));
        report.setTotalIncentive(new BigDecimal(300 + random.nextInt(1000)));

        report.setStatus("COMPLETED");
        
        return createReport(report);
    }

    /**
     * 生成周报表
     *
     * @param weekStartDate 周开始日期
     * @return 生成的报表对象
     */
    public OperationReport generateWeeklyReport(LocalDate weekStartDate) {
        LocalDate weekEndDate = weekStartDate.plusDays(6);
        
        // 检查是否已存在该周的周报
        OperationReport existingReport = getReportByDateAndType(weekStartDate, "WEEKLY");
        if (existingReport != null) {
            logger.info("周报表已存在，开始日期：{}", weekStartDate);
            return existingReport;
        }

        // 简单实现：汇总本周日报数据
        List<OperationReport> dailyReports = operationReportRepository
                .findByReportDateBetweenAndReportTypeOrderByReportDateAsc(weekStartDate, weekEndDate, "DAILY");

        OperationReport weeklyReport = new OperationReport();
        weeklyReport.setReportDate(weekStartDate);
        weeklyReport.setReportType("WEEKLY");

        if (!dailyReports.isEmpty()) {
            int totalOrders = 0;
            int completedOrders = 0;
            int cancelledOrders = 0;
            BigDecimal totalRevenue = BigDecimal.ZERO;
            int onlineDrivers = 0;
            int complaintCount = 0;

            for (OperationReport report : dailyReports) {
                totalOrders += report.getTotalOrders();
                completedOrders += report.getCompletedOrders();
                cancelledOrders += report.getCancelledOrders();
                totalRevenue = totalRevenue.add(report.getTotalRevenue());
                onlineDrivers = Math.max(onlineDrivers, report.getOnlineDrivers());
                complaintCount += report.getComplaintCount();
            }

            weeklyReport.setTotalOrders(totalOrders);
            weeklyReport.setCompletedOrders(completedOrders);
            weeklyReport.setCancelledOrders(cancelledOrders);
            weeklyReport.setTotalRevenue(totalRevenue);
            weeklyReport.setOnlineDrivers(onlineDrivers);
            weeklyReport.setComplaintCount(complaintCount);
            weeklyReport.setStatus("COMPLETED");
        } else {
            // 如果没有日报数据，生成随机测试数据
            return generateDailyReport(weekStartDate);
        }

        return createReport(weeklyReport);
    }

    /**
     * 获取最近7天的订单趋势数据
     *
     * @return 趋势数据列表
     */
    public List<Map<String, Object>> getOrderTrendData() {
        List<Map<String, Object>> trendData = new ArrayList<>();
        List<Object[]> rawData = operationReportRepository.getOrderTrendData();
        
        // 取最近7天
        int limit = Math.min(7, rawData.size());
        for (int i = 0; i < limit; i++) {
            Object[] row = rawData.get(i);
            Map<String, Object> item = new HashMap<>();
            item.put("date", row[0]);
            item.put("totalOrders", row[1]);
            item.put("completedOrders", row[2]);
            item.put("cancelledOrders", row[3]);
            trendData.add(item);
        }
        
        return trendData;
    }

    /**
     * 获取最近7天的收入趋势数据
     *
     * @return 趋势数据列表
     */
    public List<Map<String, Object>> getRevenueTrendData() {
        List<Map<String, Object>> trendData = new ArrayList<>();
        List<Object[]> rawData = operationReportRepository.getRevenueTrendData();
        
        // 取最近7天
        int limit = Math.min(7, rawData.size());
        for (int i = 0; i < limit; i++) {
            Object[] row = rawData.get(i);
            Map<String, Object> item = new HashMap<>();
            item.put("date", row[0]);
            item.put("totalRevenue", row[1]);
            item.put("avgOrderAmount", row[2]);
            trendData.add(item);
        }
        
        return trendData;
    }

    /**
     * 获取综合统计数据
     *
     * @return 统计数据Map
     */
    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        
        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = today.with(TemporalAdjusters.firstDayOfMonth());
        
        // 今日数据
        OperationReport todayReport = getReportByDateAndType(today, "DAILY");
        if (todayReport == null) {
            todayReport = generateDailyReport(today);
        }
        
        // 本月数据
        Long monthTotalOrders = operationReportRepository.sumTotalOrders(startOfMonth, today);
        BigDecimal monthTotalRevenue = operationReportRepository.sumTotalRevenue(startOfMonth, today);
        BigDecimal monthAvgCompletionRate = operationReportRepository.avgCompletionRate(startOfMonth, today);
        BigDecimal monthAvgComplaintRate = operationReportRepository.avgComplaintRate(startOfMonth, today);
        
        stats.put("todayOrders", todayReport.getTotalOrders());
        stats.put("todayRevenue", todayReport.getTotalRevenue());
        stats.put("todayCompletionRate", todayReport.getCompletionRate());
        stats.put("todayComplaintRate", todayReport.getComplaintRate());
        stats.put("todayAvgOrderAmount", todayReport.getAvgOrderAmount());
        
        stats.put("monthTotalOrders", monthTotalOrders != null ? monthTotalOrders : 0L);
        stats.put("monthTotalRevenue", monthTotalRevenue != null ? monthTotalRevenue : BigDecimal.ZERO);
        stats.put("monthAvgCompletionRate", monthAvgCompletionRate != null ? monthAvgCompletionRate : BigDecimal.ZERO);
        stats.put("monthAvgComplaintRate", monthAvgComplaintRate != null ? monthAvgComplaintRate : BigDecimal.ZERO);
        
        // 订单趋势和收入趋势
        stats.put("orderTrend", getOrderTrendData());
        stats.put("revenueTrend", getRevenueTrendData());
        
        return stats;
    }

    /**
     * 获取日报列表
     *
     * @return 日报列表
     */
    public List<OperationReport> getDailyReports() {
        return operationReportRepository.findByReportTypeOrderByReportDateDesc("DAILY");
    }

    /**
     * 生成过去N天的测试报表数据
     *
     * @param days 天数
     */
    public void generateTestReports(int days) {
        LocalDate today = LocalDate.now();
        for (int i = 0; i < days; i++) {
            LocalDate reportDate = today.minusDays(i);
            generateDailyReport(reportDate);
        }
        logger.info("已生成{}天的测试报表数据", days);
    }
}

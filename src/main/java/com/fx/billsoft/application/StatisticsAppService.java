package com.fx.billsoft.application;

import com.fx.billsoft.domain.billnote.service.StatisticsService;
import com.fx.billsoft.domain.billnote.entity.Bill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class StatisticsAppService {
    
    private final StatisticsService statisticsService;
    
    @Autowired
    public StatisticsAppService(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }
    
    /**
     * 获取用户统计概览
     * @param userId 用户ID
     * @return 统计概览数据
     */
    public Map<String, Object> getOverview(Long userId) {
        return statisticsService.getOverview(userId);
    }
    
    /**
     * 获取用户最近账单
     * @param userId 用户ID
     * @return 最近账单列表
     */
    public List<Bill> getRecentBills(Long userId) {
        return statisticsService.getRecentBills(userId);
    }
    
    /**
     * 获取用户月度收支统计
     * @param userId 用户ID
     * @return 月度收支统计
     */
    public Map<String, Map<String, BigDecimal>> getMonthlyStatistics(Long userId) {
        return statisticsService.getMonthlyStatistics(userId);
    }
    
    /**
     * 获取用户支出分类统计
     * @param userId 用户ID
     * @return 支出分类统计
     */
    public Map<String, BigDecimal> getExpenseByAccountType(Long userId) {
        return statisticsService.getExpenseByAccountType(userId);
    }
}
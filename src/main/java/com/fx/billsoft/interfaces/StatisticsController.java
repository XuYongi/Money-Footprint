package com.fx.billsoft.interfaces;

import com.fx.billsoft.application.StatisticsAppService;
import com.fx.billsoft.domain.billnote.entity.Bill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {
    
    private final StatisticsAppService statisticsAppService;
    
    @Autowired
    public StatisticsController(StatisticsAppService statisticsAppService) {
        this.statisticsAppService = statisticsAppService;
    }
    
    /**
     * 获取用户统计概览
     * @param userId 用户ID
     * @return 统计概览数据
     */
    @GetMapping("/overview/{userId}")
    public ResponseEntity<Map<String, Object>> getOverview(@PathVariable Long userId) {
        Map<String, Object> overview = statisticsAppService.getOverview(userId);
        return ResponseEntity.ok(overview);
    }
    
    /**
     * 获取用户最近账单
     * @param userId 用户ID
     * @return 最近账单列表
     */
    @GetMapping("/recent-bills/{userId}")
    public ResponseEntity<List<Bill>> getRecentBills(@PathVariable Long userId) {
        List<Bill> bills = statisticsAppService.getRecentBills(userId);
        return ResponseEntity.ok(bills);
    }
    
    /**
     * 获取用户月度收支统计
     * @param userId 用户ID
     * @return 月度收支统计
     */
    @GetMapping("/monthly/{userId}")
    public ResponseEntity<Map<String, Map<String, BigDecimal>>> getMonthlyStatistics(@PathVariable Long userId) {
        Map<String, Map<String, BigDecimal>> statistics = statisticsAppService.getMonthlyStatistics(userId);
        return ResponseEntity.ok(statistics);
    }
    
    /**
     * 获取用户支出分类统计
     * @param userId 用户ID
     * @return 支出分类统计
     */
    @GetMapping("/expense-by-type/{userId}")
    public ResponseEntity<Map<String, BigDecimal>> getExpenseByAccountType(@PathVariable Long userId) {
        Map<String, BigDecimal> statistics = statisticsAppService.getExpenseByAccountType(userId);
        return ResponseEntity.ok(statistics);
    }
}
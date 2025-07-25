package com.fx.billsoft.domain.billnote.service;

import com.fx.billsoft.domain.billnote.entity.Bill;
import com.fx.billsoft.infrastructure.billnote.repository.BillRepository;
import com.fx.billsoft.infrastructure.billnote.repository.AccountTypeRepository;
import com.fx.billsoft.domain.billnote.entity.AccountType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

@Service
public class StatisticsService {
    
    private final BillRepository billRepository;
    private final AccountTypeRepository accountTypeRepository;
    
    @Autowired
    public StatisticsService(BillRepository billRepository, AccountTypeRepository accountTypeRepository) {
        this.billRepository = billRepository;
        this.accountTypeRepository = accountTypeRepository;
    }
    
    /**
     * 获取用户总收入
     * @param userId 用户ID
     * @return 总收入金额
     */
    public BigDecimal getTotalIncome(Long userId) {
        BigDecimal total = billRepository.getTotalIncomeByUserId(userId);
        return total != null ? total : BigDecimal.ZERO;
    }
    
    /**
     * 获取用户总支出
     * @param userId 用户ID
     * @return 总支出金额
     */
    public BigDecimal getTotalExpense(Long userId) {
        BigDecimal total = billRepository.getTotalExpenseByUserId(userId);
        return total != null ? total : BigDecimal.ZERO;
    }
    
    /**
     * 获取用户账单总数
     * @param userId 用户ID
     * @return 账单总数
     */
    public Long getBillCount(Long userId) {
        return billRepository.getCountByUserId(userId);
    }
    
    /**
     * 获取用户最近的账单
     * @param userId 用户ID
     * @return 最近的账单列表
     */
    public List<Bill> getRecentBills(Long userId) {
        List<Bill> bills = billRepository.findRecentBillsByUserId(userId);
        // 限制返回最近10条记录
        return bills.stream().limit(10).collect(Collectors.toList());
    }
    
    /**
     * 获取用户统计概览
     * @param userId 用户ID
     * @return 统计概览数据
     */
    public Map<String, Object> getOverview(Long userId) {
        Map<String, Object> overview = new HashMap<>();
        BigDecimal totalIncome = getTotalIncome(userId);
        BigDecimal totalExpense = getTotalExpense(userId);
        overview.put("totalIncome", totalIncome);
        overview.put("totalExpense", totalExpense);
        overview.put("balance", totalIncome.subtract(totalExpense));
        overview.put("billCount", getBillCount(userId));
        return overview;
    }
    
    /**
     * 按月份统计用户的收支情况
     * @param userId 用户ID
     * @return 按月份分组的收支数据
     */
    public Map<String, Map<String, BigDecimal>> getMonthlyStatistics(Long userId) {
        List<Bill> bills = billRepository.findByUserId(userId);
        
        // 按月份分组并计算收入和支出
        Map<String, Map<String, BigDecimal>> monthlyStats = bills.stream()
            .collect(Collectors.groupingBy(
                bill -> bill.getDate().getYear() + "-" + String.format("%02d", bill.getDate().getMonthValue()),
                Collectors.groupingBy(bill -> {
                    AccountType accountType = accountTypeRepository.findById(bill.getAccountTypeId()).orElse(null);
                    return (accountType != null && accountType.getCategory() == 1) ? "income" : "expense";
                }, Collectors.reducing(BigDecimal.ZERO, Bill::getAmount, BigDecimal::add))
            ));
        
        return monthlyStats;
    }
    
    /**
     * 按账目类型统计支出
     * @param userId 用户ID
     * @return 按账目类型分组的支出数据
     */
    public Map<String, BigDecimal> getExpenseByAccountType(Long userId) {
        List<Bill> bills = billRepository.findByUserId(userId);
        
        // 过滤出支出账单并按账目类型分组统计
        Map<String, BigDecimal> expenseStats = bills.stream()
            .filter(bill -> {
                AccountType accountType = accountTypeRepository.findById(bill.getAccountTypeId()).orElse(null);
                return accountType != null && accountType.getCategory() == 0; // 支出类型
            })
            .collect(Collectors.groupingBy(
                bill -> {
                    AccountType accountType = accountTypeRepository.findById(bill.getAccountTypeId()).orElse(null);
                    return accountType != null ? accountType.getName() : "未知类型";
                },
                Collectors.reducing(BigDecimal.ZERO, Bill::getAmount, BigDecimal::add)
            ));
        
        return expenseStats;
    }
}
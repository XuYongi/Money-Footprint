package com.fx.billsoft.infrastructure.billnote.repository;

import com.fx.billsoft.domain.billnote.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {
    List<Bill> findByLedgerId(Long ledgerId);
    List<Bill> findByUserId(Long userId);
    List<Bill> findByLedgerIdAndUserId(Long ledgerId, Long userId);
    
    // 统计查询方法
    @Query("SELECT SUM(b.amount) FROM Bill b WHERE b.userId = ?1 AND b.accountTypeId IN (SELECT id FROM AccountType WHERE category = 1)")
    BigDecimal getTotalIncomeByUserId(Long userId);
    
    @Query("SELECT SUM(b.amount) FROM Bill b WHERE b.userId = ?1 AND b.accountTypeId IN (SELECT id FROM AccountType WHERE category = 0)")
    BigDecimal getTotalExpenseByUserId(Long userId);
    
    @Query("SELECT COUNT(b) FROM Bill b WHERE b.userId = ?1")
    Long getCountByUserId(Long userId);
    
    @Query("SELECT SUM(b.amount) FROM Bill b WHERE b.userId = ?1 AND b.accountTypeId = ?2")
    BigDecimal getTotalAmountByUserIdAndAccountType(Long userId, Long accountTypeId);
    
    @Query("SELECT b FROM Bill b WHERE b.userId = ?1 ORDER BY b.date DESC, b.id DESC")
    List<Bill> findRecentBillsByUserId(Long userId);
}
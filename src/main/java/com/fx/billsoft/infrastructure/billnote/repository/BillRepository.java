package com.fx.billsoft.infrastructure.billnote.repository;

import com.fx.billsoft.domain.billnote.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {
    List<Bill> findByLedgerId(Long ledgerId);
    List<Bill> findByUserId(Long userId);
    List<Bill> findByLedgerIdAndUserId(Long ledgerId, Long userId);
}
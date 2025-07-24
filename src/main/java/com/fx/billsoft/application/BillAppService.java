package com.fx.billsoft.application;

import com.fx.billsoft.domain.billnote.entity.Bill;
import com.fx.billsoft.domain.billnote.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class BillAppService {
    private final BillService billService;

    @Autowired
    public BillAppService(BillService billService) {
        this.billService = billService;
    }

    public Bill createBill(Bill bill) {
        return billService.createBill(bill);
    }

    public Optional<Bill> getBillById(Long id) {
        return billService.getBillById(id);
    }

    public Bill updateBill(Bill bill) {
        return billService.updateBill(bill);
    }

    public void deleteBill(Long id) {
        billService.deleteBill(id);
    }

    public List<Bill> getBillsByLedgerId(Long ledgerId) {
        return billService.getBillsByLedgerId(ledgerId);
    }

    public List<Bill> getBillsByUserId(Long userId) {
        return billService.getBillsByUserId(userId);
    }

    public List<Bill> getBillsByLedgerIdAndUserId(Long ledgerId, Long userId) {
        return billService.getBillsByLedgerIdAndUserId(ledgerId, userId);
    }
}
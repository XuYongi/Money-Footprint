package com.fx.billsoft.domain.billnote.service;

import com.fx.billsoft.domain.billnote.entity.Bill;
import com.fx.billsoft.infrastructure.billnote.repository.BillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class BillService {
    private final BillRepository billRepository;

    @Autowired
    public BillService(BillRepository billRepository) {
        this.billRepository = billRepository;
    }

    public Bill createBill(Bill bill) {
        return billRepository.save(bill);
    }

    public Optional<Bill> getBillById(Long id) {
        return billRepository.findById(id);
    }

    public Bill updateBill(Bill bill) {
        return billRepository.save(bill);
    }

    public void deleteBill(Long id) {
        billRepository.deleteById(id);
    }

    public List<Bill> getBillsByLedgerId(Long ledgerId) {
        return billRepository.findByLedgerId(ledgerId);
    }

    public List<Bill> getBillsByUserId(Long userId) {
        return billRepository.findByUserId(userId);
    }

    public List<Bill> getBillsByLedgerIdAndUserId(Long ledgerId, Long userId) {
        return billRepository.findByLedgerIdAndUserId(ledgerId, userId);
    }
}
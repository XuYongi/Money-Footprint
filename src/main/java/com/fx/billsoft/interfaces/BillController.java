package com.fx.billsoft.interfaces;

import com.fx.billsoft.application.BillAppService;
import com.fx.billsoft.domain.billnote.entity.Bill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/bills")
public class BillController {
    private final BillAppService billAppService;

    @Autowired
    public BillController(BillAppService billAppService) {
        this.billAppService = billAppService;
    }

    @PostMapping
    public ResponseEntity<Bill> createBill(@RequestBody Bill bill) {
        Bill createdBill = billAppService.createBill(bill);
        return ResponseEntity.ok(createdBill);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Bill> getBillById(@PathVariable Long id) {
        Optional<Bill> bill = billAppService.getBillById(id);
        return bill.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping
    public ResponseEntity<Bill> updateBill(@RequestBody Bill bill) {
        Bill updatedBill = billAppService.updateBill(bill);
        return ResponseEntity.ok(updatedBill);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBill(@PathVariable Long id) {
        billAppService.deleteBill(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/ledger/{ledgerId}")
    public ResponseEntity<List<Bill>> getBillsByLedgerId(@PathVariable Long ledgerId) {
        List<Bill> bills = billAppService.getBillsByLedgerId(ledgerId);
        return ResponseEntity.ok(bills);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Bill>> getBillsByUserId(@PathVariable Long userId) {
        List<Bill> bills = billAppService.getBillsByUserId(userId);
        return ResponseEntity.ok(bills);
    }

    @GetMapping("/ledger/{ledgerId}/user/{userId}")
    public ResponseEntity<List<Bill>> getBillsByLedgerIdAndUserId(@PathVariable Long ledgerId, @PathVariable Long userId) {
        List<Bill> bills = billAppService.getBillsByLedgerIdAndUserId(ledgerId, userId);
        return ResponseEntity.ok(bills);
    }
}
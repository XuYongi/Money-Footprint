package com.fx.billsoft.interfaces;

import com.fx.billsoft.application.LedgerAppService;
import com.fx.billsoft.domain.billnote.entity.Ledger;
import com.fx.billsoft.domain.billnote.entity.LedgerMember;
import com.fx.billsoft.application.LedgerMemberAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/ledgers")
public class LedgerController {
    private final LedgerAppService ledgerAppService;
    private final LedgerMemberAppService ledgerMemberAppService;

    @Autowired
    public LedgerController(LedgerAppService ledgerAppService, LedgerMemberAppService ledgerMemberAppService) {
        this.ledgerAppService = ledgerAppService;
        this.ledgerMemberAppService = ledgerMemberAppService;
    }

    @PostMapping
    public ResponseEntity<Ledger> createLedger(@RequestBody Ledger ledger) {
        Ledger createdLedger = ledgerAppService.createLedger(ledger);
        return ResponseEntity.ok(createdLedger);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ledger> getLedgerById(@PathVariable Long id) {
        Optional<Ledger> ledger = ledgerAppService.getLedgerById(id);
        return ledger.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Ledger>> getLedgersByUserId(@PathVariable Long userId) {
        List<Ledger> ledgers = ledgerAppService.getLedgersByCreatorId(userId);
        return ResponseEntity.ok(ledgers);
    }

    // 根据成员用户ID获取账本列表
    @GetMapping("/search/findByMembersUserId")
    public ResponseEntity<List<Ledger>> getLedgersByMemberUserId(@RequestParam Long userId) {
        List<Ledger> ledgers = ledgerAppService.getLedgersByMemberUserId(userId);
        return ResponseEntity.ok(ledgers);
    }

    @PostMapping("/{ledgerId}/members")
    public ResponseEntity<LedgerMember> addMemberToLedger(@PathVariable Long ledgerId, @RequestBody LedgerMember ledgerMember) {
        // 确保ledgerId一致
        ledgerMember.setLedgerId(ledgerId);
        LedgerMember addedMember = ledgerMemberAppService.addMember(ledgerMember);
        return ResponseEntity.ok(addedMember);
    }

    @GetMapping("/{ledgerId}/members")
    public ResponseEntity<List<LedgerMember>> getMembersOfLedger(@PathVariable Long ledgerId) {
        List<LedgerMember> members = ledgerMemberAppService.getMembersByLedgerId(ledgerId);
        return ResponseEntity.ok(members);
    }
}
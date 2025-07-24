package com.fx.billsoft.interfaces;

import com.fx.billsoft.application.LedgerMemberAppService;
import com.fx.billsoft.domain.billnote.entity.LedgerMember;
import com.fx.billsoft.domain.billnote.entity.LedgerMemberId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/ledger-members")
public class LedgerMemberController {
    private final LedgerMemberAppService ledgerMemberAppService;

    @Autowired
    public LedgerMemberController(LedgerMemberAppService ledgerMemberAppService) {
        this.ledgerMemberAppService = ledgerMemberAppService;
    }

    @PostMapping
    public ResponseEntity<LedgerMember> addMember(@RequestBody LedgerMember ledgerMember) {
        LedgerMember addedMember = ledgerMemberAppService.addMember(ledgerMember);
        return ResponseEntity.ok(addedMember);
    }

    @GetMapping("/{ledgerId}/{userId}")
    public ResponseEntity<LedgerMember> getLedgerMemberById(@PathVariable Long ledgerId, @PathVariable Long userId) {
        LedgerMemberId id = new LedgerMemberId(ledgerId, userId);
        Optional<LedgerMember> ledgerMember = ledgerMemberAppService.getLedgerMemberById(id);
        return ledgerMember.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{ledgerId}/{userId}")
    public ResponseEntity<Void> removeMember(@PathVariable Long ledgerId, @PathVariable Long userId) {
        LedgerMemberId id = new LedgerMemberId(ledgerId, userId);
        ledgerMemberAppService.removeMember(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/ledger/{ledgerId}")
    public ResponseEntity<List<LedgerMember>> getMembersByLedgerId(@PathVariable Long ledgerId) {
        List<LedgerMember> members = ledgerMemberAppService.getMembersByLedgerId(ledgerId);
        return ResponseEntity.ok(members);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<LedgerMember>> getMembersByUserId(@PathVariable Long userId) {
        List<LedgerMember> members = ledgerMemberAppService.getMembersByUserId(userId);
        return ResponseEntity.ok(members);
    }
}
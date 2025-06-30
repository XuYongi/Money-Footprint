package com.fx.billsoft.interfaces;

import com.fx.billsoft.application.LedgerAppService;
import com.fx.billsoft.domain.billnote.entity.Ledger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/ledgers")
public class LedgerController {
    private final LedgerAppService ledgerAppService;

    @Autowired
    public LedgerController(LedgerAppService ledgerAppService) {
        this.ledgerAppService = ledgerAppService;
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
}
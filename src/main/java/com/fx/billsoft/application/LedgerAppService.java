package com.fx.billsoft.application;

import com.fx.billsoft.domain.billnote.entity.Ledger;
import com.fx.billsoft.domain.billnote.service.LedgerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LedgerAppService {
    private final LedgerService ledgerService;

    @Autowired
    public LedgerAppService(LedgerService ledgerService) {
        this.ledgerService = ledgerService;
    }

    public Ledger createLedger(Ledger ledger) {
        return ledgerService.createLedger(ledger);
    }

    public Optional<Ledger> getLedgerById(Long id) {
        return ledgerService.getLedgerById(id);
    }

    public List<Ledger> getLedgersByCreatorId(Long creatorId) {
        return ledgerService.getLedgersByCreatorId(creatorId);
    }

    public List<Ledger> getLedgersByMemberUserId(Long userId) {
        return ledgerService.getLedgersByMemberUserId(userId);
    }
}
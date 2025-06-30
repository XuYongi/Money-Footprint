package com.fx.billsoft.domain.billnote.service;

import com.fx.billsoft.domain.billnote.entity.Ledger;
import com.fx.billsoft.infrastructure.billnote.repository.LedgerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LedgerService {
    private final LedgerRepository ledgerRepository;

    @Autowired
    public LedgerService(LedgerRepository ledgerRepository) {
        this.ledgerRepository = ledgerRepository;
    }

    public Ledger createLedger(Ledger ledger) {
        return ledgerRepository.save(ledger);
    }

    public Optional<Ledger> getLedgerById(Long id) {
        return ledgerRepository.findById(id);
    }

    public List<Ledger> getLedgersByCreatorId(Long creatorId) {
        return ledgerRepository.findByCreatorId(creatorId);
    }
}
package com.fx.billsoft.domain.billnote.service;

import com.fx.billsoft.domain.billnote.entity.LedgerMember;
import com.fx.billsoft.domain.billnote.entity.LedgerMemberId;
import com.fx.billsoft.infrastructure.billnote.repository.LedgerMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class LedgerMemberService {
    private final LedgerMemberRepository ledgerMemberRepository;

    @Autowired
    public LedgerMemberService(LedgerMemberRepository ledgerMemberRepository) {
        this.ledgerMemberRepository = ledgerMemberRepository;
    }

    public LedgerMember addMember(LedgerMember ledgerMember) {
        return ledgerMemberRepository.save(ledgerMember);
    }

    public Optional<LedgerMember> getLedgerMemberById(LedgerMemberId id) {
        return ledgerMemberRepository.findById(id);
    }

    public void removeMember(LedgerMemberId id) {
        ledgerMemberRepository.deleteById(id);
    }

    public List<LedgerMember> getMembersByLedgerId(Long ledgerId) {
        return ledgerMemberRepository.findByLedgerId(ledgerId);
    }

    public List<LedgerMember> getMembersByUserId(Long userId) {
        return ledgerMemberRepository.findByUserId(userId);
    }
}
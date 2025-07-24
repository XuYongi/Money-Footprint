package com.fx.billsoft.application;

import com.fx.billsoft.domain.billnote.entity.LedgerMember;
import com.fx.billsoft.domain.billnote.entity.LedgerMemberId;
import com.fx.billsoft.domain.billnote.service.LedgerMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class LedgerMemberAppService {
    private final LedgerMemberService ledgerMemberService;

    @Autowired
    public LedgerMemberAppService(LedgerMemberService ledgerMemberService) {
        this.ledgerMemberService = ledgerMemberService;
    }

    public LedgerMember addMember(LedgerMember ledgerMember) {
        return ledgerMemberService.addMember(ledgerMember);
    }

    public Optional<LedgerMember> getLedgerMemberById(LedgerMemberId id) {
        return ledgerMemberService.getLedgerMemberById(id);
    }

    public void removeMember(LedgerMemberId id) {
        ledgerMemberService.removeMember(id);
    }

    public List<LedgerMember> getMembersByLedgerId(Long ledgerId) {
        return ledgerMemberService.getMembersByLedgerId(ledgerId);
    }

    public List<LedgerMember> getMembersByUserId(Long userId) {
        return ledgerMemberService.getMembersByUserId(userId);
    }
}
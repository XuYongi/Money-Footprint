package com.fx.billsoft.infrastructure.billnote.repository;

import com.fx.billsoft.domain.billnote.entity.LedgerMember;
import com.fx.billsoft.domain.billnote.entity.LedgerMemberId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LedgerMemberRepository extends JpaRepository<LedgerMember, LedgerMemberId> {
    List<LedgerMember> findByLedgerId(Long ledgerId);
    List<LedgerMember> findByUserId(Long userId);
}
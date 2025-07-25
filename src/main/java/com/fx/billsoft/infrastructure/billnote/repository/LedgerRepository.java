package com.fx.billsoft.infrastructure.billnote.repository;

import com.fx.billsoft.domain.billnote.entity.Ledger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface LedgerRepository extends JpaRepository<Ledger, Long> {
    Optional<Ledger> findById(Long id);
    List<Ledger> findByCreatorId(Long creatorId);
    
    @Query("SELECT DISTINCT l FROM Ledger l WHERE l.creatorId = :userId")
    List<Ledger> findByMembersUserId(@Param("userId") Long userId);
}
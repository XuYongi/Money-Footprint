// LedgerMember.java
package com.fx.billsoft.domain.billnote.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import lombok.Data;

@Data
@Entity
@IdClass(LedgerMemberId.class)
public class LedgerMember {
    @Id
    @Column(name = "ledger_id")
    private Long ledgerId;
    @Id
    @Column(name = "user_id")
    private Long userId;
}

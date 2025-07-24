// LedgerMember.java
package com.fx.billsoft.domain.billnote.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(LedgerMemberId.class)
@Table(name = "ledger_member_t")
public class LedgerMember {
    @Id
    @Column(name = "ledger_id")
    private Long ledgerId;
    
    @Id
    @Column(name = "user_id")
    private Long userId;
}
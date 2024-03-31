// LedgerMemberId.java
package com.fx.billsoft.domain.billnote.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Embeddable
public class LedgerMemberId implements Serializable {
    private Long ledgerId;
    private Long userId;

    public LedgerMemberId(Long ledgerId, Long userId) {
        this.ledgerId = ledgerId;
        this.userId = userId;
    }
}

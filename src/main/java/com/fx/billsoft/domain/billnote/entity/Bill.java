// Bill.java
package com.fx.billsoft.domain.billnote.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal amount;
    @Column(name = "account_type_id")
    private Long accountTypeId;
    @Column(name = "ledger_id")
    private Long ledgerId;
    @Column(name = "user_id")
    private Long userId;
    private LocalDate date;
    private String remark;
}

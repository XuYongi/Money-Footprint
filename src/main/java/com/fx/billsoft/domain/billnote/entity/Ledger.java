package com.fx.billsoft.domain.billnote.entity; 
 
import lombok.Data; 
 
import javax.persistence.*; 
 
@Data 
@Entity 
@Table(name = "ledger_t") 
public class Ledger { 
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id; 
 
    private String name; 
 
    @Column(name = "creator_id") 
    private Long creatorId; 
}

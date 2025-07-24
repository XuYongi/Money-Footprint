// AccountType.java
package com.fx.billsoft.domain.billnote.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "account_type_t")
public class AccountType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(name = "user_id")
    private Long userId;
    private Integer level;
    @Column(name = "parent_id")
    private Long parentId;
    private Integer category;
}

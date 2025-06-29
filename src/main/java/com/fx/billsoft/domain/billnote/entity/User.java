// User.java
package com.fx.billsoft.domain.billnote.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "user_t")
//@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, length = 50)
    private String password;

    @Column(length = 50)
    private String email;

    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime = LocalDateTime.now();

    @Column(name = "create_user", nullable = false)
    private String createUser = "system";

    @Column(name = "update_time")
    private LocalDateTime updateTime  = LocalDateTime.now();

    @Column(name = "update_user")
    private String updateUser;

//    @Column(columnDefinition = "JSONB")
//    @Type(type = "jsonb")
//    private String accountSetting;

    // Getters and setters...
}
// User.java
package com.fx.billsoft.domain.billnote.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "user_t")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, length = 100)
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

    // 临时字段，用于注册时接收明文密码
    @Transient
    private String plainPassword;

    // 加密密码
    public void encryptPassword() {
        if (this.plainPassword != null && !this.plainPassword.isEmpty()) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            this.password = encoder.encode(this.plainPassword);
        }
    }

//    @Column(columnDefinition = "JSONB")
//    @Type(type = "jsonb")
//    private String accountSetting;

    // Getters and setters...
}
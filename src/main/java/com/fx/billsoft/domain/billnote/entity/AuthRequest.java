package com.fx.billsoft.domain.billnote.entity;

import lombok.Data;

@Data
public class AuthRequest {
    private String username;
    private String password;
}
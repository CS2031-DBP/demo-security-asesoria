package org.demo.security.auth.dto;

import lombok.Data;

@Data
public class AuthLoginRequest {
    public String email;
    public String password;
}

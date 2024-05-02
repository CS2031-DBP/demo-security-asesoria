package org.demo.security.auth.dto;

import lombok.Data;

@Data
public class AuthRegisterRequest {
    String firstName;
    String lastName;
    Integer age;
    String email;
    String password;
    Boolean isAdmin;
}
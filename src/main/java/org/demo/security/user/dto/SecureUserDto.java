package org.demo.security.user.dto;

import lombok.Data;

@Data
public class SecureUserDto {
    private String firstName;
    private String lastName;
    private Integer age;
    private String email;
}

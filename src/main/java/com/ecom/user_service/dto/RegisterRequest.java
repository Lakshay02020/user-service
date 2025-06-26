package com.ecom.user_service.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String name;
    private String password;
    private String email;
}

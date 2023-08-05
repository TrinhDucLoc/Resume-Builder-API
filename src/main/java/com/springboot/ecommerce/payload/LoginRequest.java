package com.springboot.ecommerce.payload;

import lombok.Data;

@Data
public class LoginRequest {
//    private String usernameOrEmail;
    private String email;
    private String password;
}

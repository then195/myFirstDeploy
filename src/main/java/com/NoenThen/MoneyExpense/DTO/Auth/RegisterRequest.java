package com.NoenThen.MoneyExpense.DTO.Auth;

import lombok.Data;

@Data
public class RegisterRequest {
    private String name;
    private String email;
    private String password;
}
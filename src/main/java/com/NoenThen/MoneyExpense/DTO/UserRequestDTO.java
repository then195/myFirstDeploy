package com.NoenThen.MoneyExpense.DTO;

import lombok.Data;

import java.util.UUID;

@Data
public class UserRequestDTO {
    private UUID id;
    private String name;
    private String imageUrl;
    private String email;
    private String currency;
}

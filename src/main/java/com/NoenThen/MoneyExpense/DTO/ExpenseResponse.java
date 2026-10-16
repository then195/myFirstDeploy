package com.NoenThen.MoneyExpense.DTO;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class ExpenseResponse {
    private UUID id;
    private BigDecimal amount;
    private String description;
    private LocalDate expenseDate;

    // យកតែ field ដែលត្រូវការពី Category
    private String categoryName;
    private String categoryIcon;
    private String categoryColor;
    private UUID categoryId;
    private  UUID userId;
    private String location;
    private String currency;
}
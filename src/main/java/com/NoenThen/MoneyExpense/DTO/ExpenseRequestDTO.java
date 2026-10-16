package com.NoenThen.MoneyExpense.DTO;



import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
public class ExpenseRequestDTO {
    private UUID userId;         // FK → users
    private UUID categoryId;     // FK → categories
    private BigDecimal amount;
    private String currency;
    private String description;
    private LocalDate expenseDate;
    private String location;
    private List<String> imageUrls; // optional images
    private  LocalDate StartDate;
    private  LocalDate EndDate;
}
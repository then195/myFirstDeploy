package com.NoenThen.MoneyExpense.DTO;



import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class BudgetRequestDTO {
    private UUID userId;
    private UUID categoryId;   // nullable = ថវិការសរុប
    private BigDecimal amount;
    private String period;     // daily/weekly/monthly/yearly
    private LocalDate startDate;
    private LocalDate endDate;
}
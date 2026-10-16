// BudgetController.java
package com.NoenThen.MoneyExpense.Controller;

import com.NoenThen.MoneyExpense.DTO.BudgetRequestDTO;
import com.NoenThen.MoneyExpense.Model.Budget;
import com.NoenThen.MoneyExpense.Service.BudgetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/budgets")

public class BudgetController {

    private final BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @PostMapping
    public ResponseEntity<Budget> createBudget(@RequestBody BudgetRequestDTO dto) {
        Budget saved = budgetService.createBudget(dto);
        return ResponseEntity.ok(saved);
    }
}
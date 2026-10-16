// ExpenseController.java
package com.NoenThen.MoneyExpense.Controller;

import com.NoenThen.MoneyExpense.DTO.ExpenseRequestDTO;
import com.NoenThen.MoneyExpense.DTO.ExpenseResponse;
import com.NoenThen.MoneyExpense.Service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor

public class ExpenseController {


   private final ExpenseService expenseService;




    @PostMapping
    public ResponseEntity<ExpenseResponse> createExpense(
            @RequestBody ExpenseRequestDTO dto) {
        ExpenseResponse saved = expenseService.createExpense(dto);
        return ResponseEntity.ok(saved);
    }
    //get Expanse by user id
    @GetMapping("/{id}")
    public ResponseEntity<?> getExpensesByUser(
            @PathVariable("id") UUID userId) {

        List<ExpenseResponse> expenses = expenseService.getExpensesByUserId(userId);

        return ResponseEntity.ok(expenses);
    }
     // update expense
    @PutMapping("/{id}")
    public ResponseEntity<ExpenseResponse> updateExpense(
            @PathVariable UUID id,
            @RequestBody ExpenseRequestDTO dto) {
        ExpenseResponse updated = expenseService.UpdateExpense(id,dto);
        return ResponseEntity.ok(updated);
    }
    // delete expense
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable UUID id) {

        expenseService.deleteExpense(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/{id}/today")
    public ResponseEntity<List<ExpenseResponse>> getExpenseByToday(
            @PathVariable UUID id,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {

        List<ExpenseResponse> expenses = expenseService.getExpenseByToday(id, startDate, endDate);
        return ResponseEntity.ok(expenses);
    }
    @GetMapping("/{id}/this-month")
    public ResponseEntity<List<ExpenseResponse>> getExpenseThisMonths(
            @PathVariable UUID id){
        List<ExpenseResponse> expenses = expenseService.getExpenseForThisMont(id);
        return ResponseEntity.ok(expenses);
    }
    @GetMapping("/{id}/date-range")
    public ResponseEntity<List<ExpenseResponse>> getBYDate(
            @PathVariable UUID id,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate){
        List<ExpenseResponse> expenses = expenseService.getExpenseByDate(id,startDate,endDate);
        return ResponseEntity.ok(expenses);
    }
}
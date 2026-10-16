// ExpenseRepository.java
package com.NoenThen.MoneyExpense.Repository;

import com.NoenThen.MoneyExpense.Model.Expense;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ExpenseRepository extends JpaRepository<Expense, UUID> {
List<Expense>findByUserId(UUID userId);

    void deleteByUserId(UUID id);


    List<Expense> findByUserIdAndExpenseDateBetween(UUID id, LocalDate today, LocalDate today1);
}
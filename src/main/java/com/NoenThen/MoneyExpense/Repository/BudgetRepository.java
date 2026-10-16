// BudgetRepository.java
package com.NoenThen.MoneyExpense.Repository;

import com.NoenThen.MoneyExpense.Model.Budget;
import com.NoenThen.MoneyExpense.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface BudgetRepository extends JpaRepository<Budget, UUID> {
    void deleteByUserId(UUID id);
}
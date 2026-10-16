package com.NoenThen.MoneyExpense.Repository;

import com.NoenThen.MoneyExpense.Model.ExpenseImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.UUID;

@Repository
public interface ExpenseImageRepository
        extends JpaRepository<ExpenseImage, UUID> {

    List<ExpenseImage> findByExpenseId(UUID expenseId);
}

// BudgetService.java
package com.NoenThen.MoneyExpense.Service;

import com.NoenThen.MoneyExpense.DTO.BudgetRequestDTO;
import com.NoenThen.MoneyExpense.Model.*;
import com.NoenThen.MoneyExpense.Repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service

public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final UserRepository userRepository;
    private final CategoriesRepository categoriesRepository;

    public BudgetService(BudgetRepository budgetRepository, UserRepository userRepository, CategoriesRepository categoriesRepository) {
        this.budgetRepository = budgetRepository;
        this.userRepository = userRepository;
        this.categoriesRepository = categoriesRepository;
    }

    @Transactional
    public Budget createBudget(BudgetRequestDTO dto) {

        // 1. រក User
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2. រក Category (nullable បាន)
        Categories category = null;
        if (dto.getCategoryId() != null) {
            category = categoriesRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
        }

        // 3. បង្កើត Budget
        Budget budget = new Budget();
        budget.setUser(user);
        budget.setCategory(category); // null = ថវិការសរុប
        budget.setAmount(dto.getAmount());
        budget.setPeriod(dto.getPeriod());
        budget.setStartDate(dto.getStartDate());
        budget.setEndDate(dto.getEndDate());

        return budgetRepository.save(budget);
    }



}
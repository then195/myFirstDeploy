// ExpenseService.java
package com.NoenThen.MoneyExpense.Service;

import com.NoenThen.MoneyExpense.DTO.ExpenseRequestDTO;
import com.NoenThen.MoneyExpense.DTO.ExpenseResponse;
import com.NoenThen.MoneyExpense.Model.Categories;
import com.NoenThen.MoneyExpense.Model.Expense;
import com.NoenThen.MoneyExpense.Model.ExpenseImage;
import com.NoenThen.MoneyExpense.Model.User;
import com.NoenThen.MoneyExpense.Repository.CategoriesRepository;
import com.NoenThen.MoneyExpense.Repository.ExpenseRepository;
import com.NoenThen.MoneyExpense.Repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Service

public class ExpenseService {


    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final CategoriesRepository categoriesRepository;

    public ExpenseService(ExpenseRepository expenseRepository, UserRepository userRepository, CategoriesRepository categoriesRepository) {
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
        this.categoriesRepository = categoriesRepository;
    }

    @Transactional
    public ExpenseResponse createExpense(ExpenseRequestDTO dto) {

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found: " + dto.getUserId()));

        Categories category = categoriesRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found: " + dto.getCategoryId()));

        Expense expense = new Expense();
        expense.setUser(user);

        expense.setAmount(dto.getAmount());
        expense.setCategory(category);
        expense.setCurrency(dto.getCurrency() != null ? dto.getCurrency() : "KHR");
        expense.setDescription(dto.getDescription());
        expense.setExpenseDate(dto.getExpenseDate() != null ? dto.getExpenseDate() : LocalDate.now());
        expense.setLocation(dto.getLocation());

        if (dto.getImageUrls() != null && !dto.getImageUrls().isEmpty()) {
            List<ExpenseImage> images = new ArrayList<>();
            for (String url : dto.getImageUrls()) {
                ExpenseImage image = new ExpenseImage();
                image.setImageUrl(url);
                image.setExpense(expense);
                images.add(image);
            }
            expense.setImages(images);
        }

        Expense saved = expenseRepository.save(expense);

        //  Map to ExpenseResponse so categoryName is included
        ExpenseResponse response = new ExpenseResponse();
        response.setId(saved.getId());
        response.setAmount(saved.getAmount());
        response.setCurrency(saved.getCurrency());
        response.setDescription(saved.getDescription());
        response.setExpenseDate(saved.getExpenseDate());
        response.setLocation(saved.getLocation());
        response.setUserId(saved.getUser().getId());
        response.setCategoryId(saved.getCategory().getId());
        response.setCategoryName(saved.getCategory().getName());
        response.setCategoryIcon(saved.getCategory().getIcon());
        response.setCategoryColor(saved.getCategory().getColor());

        return response;
    }
    //get by user id
    public List<ExpenseResponse> getExpensesByUserId(UUID userId) {
        List<Expense> expenses = expenseRepository.findByUserId(userId);

        return expenses.stream().map(e -> {
            ExpenseResponse dto = new ExpenseResponse();
            dto.setId(e.getId());
            dto.setUserId(e.getUser().getId());
            dto.setCategoryId(e.getCategory().getId());
            dto.setAmount(e.getAmount());
            dto.setDescription(e.getDescription());
            dto.setCurrency(e.getCurrency());
            dto.setExpenseDate(e.getExpenseDate());
            dto.setCategoryName(e.getCategory().getName());
            dto.setCategoryIcon(e.getCategory().getIcon());
            dto.setCategoryColor(e.getCategory().getColor());
            return dto;
        }).toList();
    }

    @Transactional
    public ExpenseResponse UpdateExpense(UUID id, ExpenseRequestDTO requestDTO) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found: " + id));

        if (requestDTO.getAmount() != null)
            expense.setAmount(requestDTO.getAmount());
        if (requestDTO.getDescription() != null && !requestDTO.getDescription().isEmpty())
            expense.setDescription(requestDTO.getDescription());
        if (requestDTO.getExpenseDate() != null)
            expense.setExpenseDate(requestDTO.getExpenseDate());
        if (requestDTO.getLocation() != null && !requestDTO.getLocation().isEmpty())
            expense.setLocation(requestDTO.getLocation());
        if (requestDTO.getCurrency() != null && !requestDTO.getCurrency().isEmpty())
            expense.setCurrency(requestDTO.getCurrency());

        // Update Category
        if (requestDTO.getCategoryId() != null) {
            Categories category = categoriesRepository.findById(requestDTO.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found: " + requestDTO.getCategoryId()));
            expense.setCategory(category);
        }

        Expense updated = expenseRepository.save(expense);
//
//         Convert back to DTO
        ExpenseResponse responseDTO = new ExpenseResponse();
        responseDTO.setUserId(updated.getUser().getId());
        responseDTO.setId(updated.getId());
        responseDTO.setCategoryName(updated.getCategory().getName());
        responseDTO.setCategoryIcon(updated.getCategory().getIcon());
        responseDTO.setCategoryColor(updated.getCategory().getColor());
        responseDTO.setCategoryId(updated.getCategory().getId());
        responseDTO.setAmount(updated.getAmount());
        responseDTO.setCurrency(updated.getCurrency());
        responseDTO.setDescription(updated.getDescription());
        responseDTO.setExpenseDate(updated.getExpenseDate());
        responseDTO.setLocation(updated.getLocation());

        return responseDTO;
    }

    //delete
    public void deleteExpense(UUID id) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found: " + id));
        expenseRepository.delete(expense);
    }
    // get expense by date

    public List<ExpenseResponse> getExpenseByToday(UUID id, LocalDate startDate, LocalDate endDate) {
        userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found " + id));

        LocalDate today = LocalDate.now();

        //  Use LocalDate params directly — no more DTO
        List<Expense> expenses;
        if (startDate != null && endDate != null) {
            expenses = expenseRepository.findByUserIdAndExpenseDateBetween(id, startDate, endDate);
        } else {
            expenses = expenseRepository.findByUserIdAndExpenseDateBetween(id, today, today);
        }

        return expenses.stream().map(e -> {
            ExpenseResponse dto = new ExpenseResponse();
            dto.setId(e.getId());
            dto.setAmount(e.getAmount());
            dto.setDescription(e.getDescription());
            dto.setExpenseDate(e.getExpenseDate());
            dto.setCategoryName(e.getCategory().getName());
            dto.setCategoryIcon(e.getCategory().getIcon());
            dto.setCategoryColor(e.getCategory().getColor());
            dto.setCategoryId(e.getCategory().getId());
            dto.setCurrency(e.getCurrency());
            return dto;
        }).toList();
    }
    public List<ExpenseResponse> getExpenseByDate(UUID id, LocalDate startDate, LocalDate endDate) {
        userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found " + id));



        //  Use LocalDate params directly — no more DTO
        List<Expense> expenses;
        if (startDate != null && endDate != null) {
            expenses = expenseRepository.findByUserIdAndExpenseDateBetween(id, startDate, endDate);
        } else {
            expenses = expenseRepository.findByUserIdAndExpenseDateBetween(id, startDate, endDate);
        }

        return expenses.stream().map(e -> {
            ExpenseResponse dto = new ExpenseResponse();
            dto.setId(e.getId());
            dto.setAmount(e.getAmount());
            dto.setDescription(e.getDescription());
            dto.setExpenseDate(e.getExpenseDate());
            dto.setCategoryName(e.getCategory().getName());
            dto.setCategoryIcon(e.getCategory().getIcon());
            dto.setCategoryColor(e.getCategory().getColor());
            dto.setCategoryId(e.getCategory().getId());
            dto.setCurrency(e.getCurrency());
            return dto;
        }).toList();
    }
    public  List<ExpenseResponse> getExpenseForThisMont(UUID id ){
       getUser(id);
       List<Expense> expenses = List.of();
       LocalDate today = LocalDate.now();
       LocalDate firstDayOfMonth = today.withDayOfMonth(1);
         LocalDate lastDayOfMonth = today.withDayOfMonth(today.lengthOfMonth());

             expenses=expenseRepository.findByUserIdAndExpenseDateBetween(id, firstDayOfMonth, lastDayOfMonth);


         return expenses.stream().map(e->{
                ExpenseResponse dto = new ExpenseResponse();
                dto.setId(e.getId());
                dto.setAmount(e.getAmount());
                dto.setDescription(e.getDescription());
                dto.setExpenseDate(e.getExpenseDate());
                dto.setCategoryName(e.getCategory().getName());
                dto.setCategoryIcon(e.getCategory().getIcon());
                dto.setCategoryColor(e.getCategory().getColor());
                dto.setCategoryId(e.getCategory().getId());
                dto.setCurrency(e.getCurrency());
                return dto;
         }).toList();

    }
    public void getUser(UUID id){
        userRepository.findById(id).orElseThrow(()->new RuntimeException("User not found " + id));

    }

}
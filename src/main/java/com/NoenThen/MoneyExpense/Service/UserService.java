package com.NoenThen.MoneyExpense.Service;

import com.NoenThen.MoneyExpense.DTO.UserRequestDTO;
import com.NoenThen.MoneyExpense.Model.User;
import com.NoenThen.MoneyExpense.Repository.BudgetRepository;
import com.NoenThen.MoneyExpense.Repository.CategoriesRepository;
import com.NoenThen.MoneyExpense.Repository.ExpenseRepository;
import com.NoenThen.MoneyExpense.Repository.UserRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;



@Service
public class UserService {
    @Autowired private UserRepository userRepo;
    @Autowired private CategoriesRepository categoriesRepository;
    @Autowired private ExpenseRepository expenseRepository;
    @Autowired private BudgetRepository budgetRepository;

    public User saveUser(User user) {
        // Implement logic to save user to the database using userRepo
        if (user.getEmail() == null || user.getPasswordHash() == null) {
            throw new IllegalArgumentException("Email and password cannot be null");
        }
        for(User existingUser : userRepo.findAll()) {
            if (existingUser.getEmail().equals(user.getEmail())) {
                throw new IllegalArgumentException("Email already exists");
            }
        }
        boolean isWeak = isIsWeak(user);

        if (isWeak) {
            throw new IllegalArgumentException("Weak password");
        }
            return userRepo.save(user);

        }

    private static boolean isIsWeak(User user) {
        if (user.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        String emailRegex = "^[\\w.-]+@[\\w.-]+\\.[A-Za-z]{2,}$";
        if (!user.getEmail().matches(emailRegex)) {
            throw new IllegalArgumentException("Invalid email format");
        }
        String password = user.getPasswordHash();
        return password.length() < 8 ||
                !password.matches(".*[A-Z].*") ||
                !password.matches(".*[a-z].*") ||
                !password.matches(".*\\d.*") ||
                !password.matches(".*[@$!%*?&].*");
    }

    public User getUserById(UUID id) {
        return userRepo.findById(id)
                .orElseThrow(()
                        -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    // update
    public User editUser(UserRequestDTO dto) {
        // 1. Retrieve the existing user or fail
        User user = userRepo.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("User not found: " + dto.getId()));


        if(!dto.getImageUrl().isEmpty()){
            user.setAvatarUrl(dto.getImageUrl());
        }
       if(!dto.getCurrency().isEmpty()){
           user.setCurrency(dto.getCurrency());
       }
       if(!dto.getName().isEmpty()){
           user.setName(dto.getName());
       }



        return userRepo.save(user);
    }
    //delete
    @Transactional
    public void deleteUser(UUID id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found: " + id));

        // លុប User តែម្ដង — CASCADE លុប Budget/Expense/Category ដោយស្វ័យប្រវត្តិ

     userRepo.delete(user);

    }
    //delete
    @Transactional
    public void deleteUserByManual(UUID id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found: " + id));


        budgetRepository.deleteByUserId(id);
        expenseRepository.deleteByUserId(id);    // ← លុបជាមុន
        categoriesRepository.deleteByUserId(id);

        userRepo.delete(user);

    }


}



package com.NoenThen.MoneyExpense.Controller;


import com.NoenThen.MoneyExpense.DTO.ApiResponse;
import com.NoenThen.MoneyExpense.DTO.UserRequestDTO;
import com.NoenThen.MoneyExpense.Model.User;
import com.NoenThen.MoneyExpense.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {

        @Autowired
        private UserService userService;

    // CREATE user
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.saveUser(user);
    }
    @GetMapping("/{id}")
    public User getUserById(@PathVariable UUID id) {
        return userService.getUserById(id);
    }
    @PutMapping("/{id}")
    public User editUser(@PathVariable UUID id, @RequestBody UserRequestDTO dto) {

        if (!id.equals(dto.getId())) {
            throw new RuntimeException("ID in path does not match ID in body");
        }
        return userService.editUser(dto);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(
            @PathVariable UUID id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok(
                    ApiResponse.success("លុប User ដោយជោគជ័យ!")
            );
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(
                    ApiResponse.fail(e.getMessage())
            );
        }
    }
    @DeleteMapping("/manual/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUserManual(
            @PathVariable UUID id) {
        try {
            userService.deleteUserByManual(id); // ← ឈ្មោះត្រូវ Match!
            return ResponseEntity.ok(
                    ApiResponse.success("លុប User ដោយជោគជ័យ!")
            );
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(
                    ApiResponse.fail("Error: " + e.getMessage())
            );
        }

    }
}

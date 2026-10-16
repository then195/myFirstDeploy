package com.NoenThen.MoneyExpense.Controller;



import com.NoenThen.MoneyExpense.DTO.*;
import com.NoenThen.MoneyExpense.DTO.Auth.AuthResponse;
import com.NoenThen.MoneyExpense.DTO.Auth.LoginRequest;
import com.NoenThen.MoneyExpense.DTO.Auth.RegisterRequest;
import com.NoenThen.MoneyExpense.Service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class Auth {

    @Autowired
    private AuthService authService;

    // POST /api/auth/register
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(
            @RequestBody RegisterRequest req) {
        try {
            AuthResponse res = authService.register(req);
            return ResponseEntity.ok(
                    ApiResponse.success("Register successful!", res)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.fail(e.getMessage())
            );
        }
    }

    // POST /api/auth/login
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @RequestBody LoginRequest req) {
        try {
            AuthResponse res = authService.login(req);
            return ResponseEntity.ok(
                    ApiResponse.success("Login successful!", res)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.fail(e.getMessage())
            );
        }
    }
}
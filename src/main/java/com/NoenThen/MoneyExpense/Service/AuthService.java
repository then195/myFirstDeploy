package com.NoenThen.MoneyExpense.Service;




import com.NoenThen.MoneyExpense.DTO.Auth.AuthResponse;
import com.NoenThen.MoneyExpense.DTO.Auth.LoginRequest;
import com.NoenThen.MoneyExpense.DTO.Auth.RegisterRequest;
import com.NoenThen.MoneyExpense.Model.User;

import com.NoenThen.MoneyExpense.Repository.UserRepository;
import com.NoenThen.MoneyExpense.Util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired private UserRepository userRepo;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtUtil         jwtUtil;

    // ========== REGISTER ==========
    public AuthResponse register(RegisterRequest req) {

        // Check email ស្ទួន
        if (userRepo.findByEmail(req.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists!");
        }

        // បង្កើត User
        User user = new User();
        user.setName(req.getName());
        user.setEmail(req.getEmail());
        user.setPasswordHash(passwordEncoder.encode(req.getPassword()));
        user.setCurrency("KHR");

        User saved = userRepo.save(user);

        // Generate Token
        String token = jwtUtil.generateToken(saved.getEmail());

        return new AuthResponse(token, saved.getName(), saved.getEmail(), saved.getId());
    }

    // ========== LOGIN ==========
    public AuthResponse login(LoginRequest req) {

        // ស្វែងរក User
        User user = userRepo.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("Email not found!"));

        // Check Password
        if (!passwordEncoder.matches(req.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Wrong password!");
        }

        // Generate Token
        String token = jwtUtil.generateToken(user.getEmail());

        return new AuthResponse(token, user.getName(), user.getEmail(), user.getId());
    }
}
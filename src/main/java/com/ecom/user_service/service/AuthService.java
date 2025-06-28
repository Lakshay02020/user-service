package com.ecom.user_service.service;

import com.ecom.user_service.dto.AuthRequest;
import com.ecom.user_service.dto.AuthResponse;
import com.ecom.user_service.dto.RegisterRequest;
import com.ecom.user_service.entity.User;
import com.ecom.user_service.repository.UserRepository;
import com.ecom.user_service.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public Map<String, String> register(RegisterRequest request) {

        // Basic Null or Empty Checks
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }

        // Check if username is already taken
        if (userRepository.findByUsername(request.getName()).isPresent()) {
            throw new IllegalArgumentException("Username is already taken");
        }

        // Check if email is already registered
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email is already registered");
        }

        // Everything is valid - proceed to save user
        User user = new User();
        user.setUsername(request.getName().trim());
        user.setEmail(request.getEmail().trim());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("USER");

        userRepository.save(user);

        Map<String, String> response = new HashMap<>();
        response.put("token", jwtUtil.generateToken(user.getUsername()));
        response.put("userId", user.getUserId());
        // Generate and return token
        return response;
    }


    public AuthResponse authenticate(AuthRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        return new AuthResponse(jwtUtil.generateToken(user.getUsername()), user.getUserId());
    }
}

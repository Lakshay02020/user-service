package com.ecom.user_service.controller;

import com.ecom.user_service.dto.AuthRequest;
import com.ecom.user_service.dto.AuthResponse;
import com.ecom.user_service.dto.RegisterRequest;
import com.ecom.user_service.service.AuthService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        log.info("Request for user registration: {}" ,request);
        authService.register(request);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        log.info("Request for user login: {}" ,request);
        String token = authService.authenticate(request);
        return ResponseEntity.ok(new AuthResponse(token));
    }
}

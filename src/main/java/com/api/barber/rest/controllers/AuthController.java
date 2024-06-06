package com.api.barber.rest.controllers;

import com.api.barber.rest.dtos.request.LoginRequestDto;
import com.api.barber.rest.dtos.request.RegisterRequestDto;
import com.api.barber.rest.dtos.response.LoginResponseDto;
import com.api.barber.services.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@Slf4j
@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDto request) {
        try {
            authService.register(request);
            log.info("User registered successfully: {}", request.getPhone());
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error registering user: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto request) {
        try {
            LoginResponseDto response = authService.login(request);
            log.info("User logged in successfully: {}", request.getPhone());
            return ResponseEntity.ok(response);
        } catch (Exception e){
            log.error("Error logging in: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}

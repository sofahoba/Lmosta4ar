package com.fullDetailed.fullDetailedDemo.controller.auth;

import com.fullDetailed.fullDetailedDemo.domain.dtos.auth.LoginRequestDto;
import com.fullDetailed.fullDetailedDemo.domain.dtos.auth.LoginResponseDto;
import com.fullDetailed.fullDetailedDemo.domain.dtos.auth.RegisterRequestDto;
import com.fullDetailed.fullDetailedDemo.domain.dtos.auth.RegisterResponseDto;
import com.fullDetailed.fullDetailedDemo.services.interfaces.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDto> register(@Valid @RequestBody RegisterRequestDto request) {
        RegisterResponseDto response = authService.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto request) {
        LoginResponseDto response = authService.login(request);
        return ResponseEntity.ok(response);
    }

}

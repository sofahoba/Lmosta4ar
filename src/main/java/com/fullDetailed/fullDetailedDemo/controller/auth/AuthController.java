package com.fullDetailed.fullDetailedDemo.controller.auth;

import com.fullDetailed.fullDetailedDemo.domain.dtos.ApiResponse;
import com.fullDetailed.fullDetailedDemo.domain.dtos.auth.*;
import com.fullDetailed.fullDetailedDemo.services.interfaces.AuthService;
import com.fullDetailed.fullDetailedDemo.util.ResponseHelper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<ApiResponse<RegisterResponseDto>> register(
            @Valid @RequestBody RegisterRequestDto request) {
        RegisterResponseDto response = authService.register(request);
        return ResponseHelper.created(response, "Registration successful. Please verify your email with the OTP sent.");
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDto>> login(
            @Valid @RequestBody LoginRequestDto request) {
        LoginResponseDto response = authService.login(request);
        return ResponseHelper.ok(response, "Login successful");
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<ApiResponse<Void>> resendOtpCode(
            @Valid @RequestBody ResendOtpRequest dto) {
        authService.resendOtp(dto.getEmail());
        return ResponseHelper.ok("OTP code has been resent to your email");
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse<Void>> verifyOtpCode(
            @Valid @RequestBody VerifyOtpRequest dto) {
        authService.sendOtpCode(dto.getEmail(), dto.getOtpCode());
        return ResponseHelper.ok("OTP verified successfully. Your account is now active.");
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<ForgotPasswordResponse>> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest dto) {
        ForgotPasswordResponse response = authService.forgotPassword(dto);
        return ResponseHelper.ok(response, "Password reset instructions sent to your email");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<Void>> resetPassword(
            @Valid @RequestBody ResetPasswordRequest dto) {
        authService.resetPassword(dto);
        return ResponseHelper.ok("Password has been reset successfully");
    }
}
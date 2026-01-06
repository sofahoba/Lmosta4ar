package com.fullDetailed.fullDetailedDemo.services.interfaces;

import com.fullDetailed.fullDetailedDemo.domain.dtos.auth.*;

public interface AuthService {

    RegisterResponseDto register(RegisterRequestDto request);
    LoginResponseDto login(LoginRequestDto request);
    String sendOtpCode(String email,String otpCode);
    String resendOtp(String email);
    ForgotPasswordResponse forgotPassword(ForgotPasswordRequest dto);
    String resetPassword(ResetPasswordRequest dto);

}

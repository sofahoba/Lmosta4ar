package com.fullDetailed.fullDetailedDemo.domain.dtos.auth;

import lombok.Data;

@Data
public class ResetPasswordRequest {
    String email;
    String newPassword;
    String confirmPassword;
    String otpCode;
}

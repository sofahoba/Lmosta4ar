package com.fullDetailed.fullDetailedDemo.domain.dtos.auth;

import lombok.Data;

@Data
public class VerifyOtpRequest {
    private final String email;
    private final String otpCode;
}

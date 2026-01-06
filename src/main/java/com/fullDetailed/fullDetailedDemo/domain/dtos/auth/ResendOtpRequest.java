package com.fullDetailed.fullDetailedDemo.domain.dtos.auth;

import lombok.Data;

@Data
public class ResendOtpRequest {
    private final String email;
}

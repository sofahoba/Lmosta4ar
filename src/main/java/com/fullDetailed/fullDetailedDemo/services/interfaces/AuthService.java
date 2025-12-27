package com.fullDetailed.fullDetailedDemo.services.interfaces;

import com.fullDetailed.fullDetailedDemo.domain.dtos.auth.LoginRequestDto;
import com.fullDetailed.fullDetailedDemo.domain.dtos.auth.LoginResponseDto;
import com.fullDetailed.fullDetailedDemo.domain.dtos.auth.RegisterRequestDto;
import com.fullDetailed.fullDetailedDemo.domain.dtos.auth.RegisterResponseDto;

public interface AuthService {

    RegisterResponseDto register(RegisterRequestDto request);
    LoginResponseDto login(LoginRequestDto request);
}

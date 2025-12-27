package com.fullDetailed.fullDetailedDemo.services.impl;

import com.fullDetailed.fullDetailedDemo.config.securityServices.CustomUserDetails;
import com.fullDetailed.fullDetailedDemo.config.securityServices.JwtUtill;
import com.fullDetailed.fullDetailedDemo.domain.dtos.auth.LoginRequestDto;
import com.fullDetailed.fullDetailedDemo.domain.dtos.auth.LoginResponseDto;
import com.fullDetailed.fullDetailedDemo.domain.dtos.auth.RegisterRequestDto;
import com.fullDetailed.fullDetailedDemo.domain.dtos.auth.RegisterResponseDto;
import com.fullDetailed.fullDetailedDemo.domain.entities.User;
import com.fullDetailed.fullDetailedDemo.exceptions.AlreadyExistsException;
import com.fullDetailed.fullDetailedDemo.exceptions.NotFoundException;
import com.fullDetailed.fullDetailedDemo.mapper.auth.AuthMapper;
import com.fullDetailed.fullDetailedDemo.repository.UserRepo;
import com.fullDetailed.fullDetailedDemo.services.interfaces.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtill jwtUtil;
    private final AuthMapper authMapper;
    private final AuthenticationManager authenticationManager;

    @Override
    public RegisterResponseDto register(RegisterRequestDto request){
        if (userRepo.existsByEmail(request.getEmail())) {
            throw new AlreadyExistsException("Email already exists");
        }
        User user = authMapper.toEntity(request);
        userRepo.save(user);
        return authMapper.toDto(user);
    }

    @Override
    public LoginResponseDto login(LoginRequestDto request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> new NotFoundException("User not found"));
        var accessToken = jwtUtil.generateToken(new CustomUserDetails(user));
        var refreshToken = jwtUtil.generateRefreshToken(new CustomUserDetails(user));
        LoginResponseDto res = new LoginResponseDto();
        res.setAccess_token(accessToken);
        res.setRefresh_token(refreshToken);
        res.setRole(user.getRole());
        res.setId(user.getId());

        return res;
    }

}

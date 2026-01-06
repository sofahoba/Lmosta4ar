package com.fullDetailed.fullDetailedDemo.services.impl;

import com.fullDetailed.fullDetailedDemo.config.securityServices.CustomUserDetails;
import com.fullDetailed.fullDetailedDemo.config.securityServices.JwtUtil;
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
import com.fullDetailed.fullDetailedDemo.services.interfaces.emailSender.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthMapper authMapper;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;

    @Override
    public RegisterResponseDto register(RegisterRequestDto request){
        if (userRepo.existsByEmail(request.getEmail())) {
            throw new AlreadyExistsException("Email already exists");
        }
        User user = authMapper.toEntity(request);
        String otp= emailService.sendOtpEmail(user.getEmail());
        user.setOtpCode(passwordEncoder.encode(otp));
        user.setOtpExpirationTime(LocalDateTime.now().plusMinutes(10));
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

        if(!user.isActive()){
            throw new NotFoundException("Sorry your account is inActive please contact the admin");
        }
        if(user.isDeleted()){
            throw new NotFoundException("the email you entered is incorrect");
        }
        var accessToken = jwtUtil.generateToken(new CustomUserDetails(user));
        var refreshToken = jwtUtil.generateRefreshToken(new CustomUserDetails(user));
        LoginResponseDto res = new LoginResponseDto();
        res.setAccess_token(accessToken);
        res.setRefresh_token(refreshToken);
        res.setRole(user.getRole());
        res.setId(user.getId());

        return res;
    }

    @Override
    public String sendOtpCode(String email, String otpCode) {

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));
        if (user.getOtpCode() == null) {
            throw new NotFoundException("No OTP found for this user");
        }
        if (user.getOtpExpirationTime().isBefore(LocalDateTime.now())) {
            throw new NotFoundException("OTP expired");
        }
        boolean isOtpValid = passwordEncoder.matches(otpCode, user.getOtpCode());
        if (!isOtpValid) {
            throw new NotFoundException("Invalid OTP");
        }
        user.setActive(true);
//        user.setOtpCode(null);
//        user.setOtpExpirationTime(null);
        userRepo.save(user);
        return "account verified successfully you can login now";
    }


    @Override
    public String resendOtp(String email) {

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (user.isActive()) {
            throw new AlreadyExistsException("Account already verified");
        }
        if (user.getOtpExpirationTime() != null &&
                user.getOtpExpirationTime().minusMinutes(9).isAfter(LocalDateTime.now())) {

            throw new AlreadyExistsException("Please wait before requesting a new OTP");
        }
        String newOtp = emailService.sendOtpEmail(user.getEmail());
        user.setOtpCode(passwordEncoder.encode(newOtp));
        user.setOtpExpirationTime(LocalDateTime.now().plusMinutes(10));
        userRepo.save(user);
        return "New OTP sent successfully";
    }


}

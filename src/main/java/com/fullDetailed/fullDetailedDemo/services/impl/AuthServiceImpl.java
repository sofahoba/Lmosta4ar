package com.fullDetailed.fullDetailedDemo.services.impl;

import com.fullDetailed.fullDetailedDemo.config.securityServices.CustomUserDetails;
import com.fullDetailed.fullDetailedDemo.config.securityServices.JwtUtil;
import com.fullDetailed.fullDetailedDemo.domain.dtos.auth.*;
import com.fullDetailed.fullDetailedDemo.domain.entities.User;
import com.fullDetailed.fullDetailedDemo.domain.enums.Role;
import com.fullDetailed.fullDetailedDemo.exceptions.AlreadyExistsException;
import com.fullDetailed.fullDetailedDemo.exceptions.NotFoundException;
import com.fullDetailed.fullDetailedDemo.mapper.auth.AuthMapper;
import com.fullDetailed.fullDetailedDemo.repository.UserRepo;
import com.fullDetailed.fullDetailedDemo.services.interfaces.AuthService;
import com.fullDetailed.fullDetailedDemo.services.interfaces.emailSender.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthMapper authMapper;
    private final EmailService emailService;

    @Override
    public RegisterResponseDto register(RegisterRequestDto request){

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Password and Confirm Password do not match");
        }

        if (userRepo.existsByEmail(request.getEmail())) {
            throw new AlreadyExistsException("Email already exists");
        }

        if (userRepo.existsByNationalId(request.getNationalId())) {
            throw new AlreadyExistsException("National ID already exists");
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
        long start = System.currentTimeMillis();

        // ✅ Single database query - no Spring Security involvement
        User user = userRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> new NotFoundException("Invalid email or password"));
        log.info("📊 DB Query took: {} ms", System.currentTimeMillis() - start);

        // Validate status
        validateUserStatus(user);

        // ✅ Verify password directly (no AuthenticationManager)
        long bcryptStart = System.currentTimeMillis();
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new NotFoundException("Invalid email or password");
        }
        log.info("📊 BCrypt took: {} ms", System.currentTimeMillis() - bcryptStart);

        // Generate tokens
        long tokenStart = System.currentTimeMillis();
        CustomUserDetails userDetails = new CustomUserDetails(user);
        String accessToken = jwtUtil.generateToken(userDetails);
        String refreshToken = jwtUtil.generateRefreshToken(userDetails);
        log.info("📊 Tokens took: {} ms", System.currentTimeMillis() - tokenStart);

        log.info("📊 TOTAL: {} ms", System.currentTimeMillis() - start);

        return LoginResponseDto.builder()
                .access_token(accessToken)
                .refresh_token(refreshToken)
                .role(user.getRole())
                .id(user.getId())
                .build();
    }

    private void validateUserStatus(User user) {
        if (user.isDeleted()) {
            throw new NotFoundException("Invalid email or password");
        }
        if (!user.isActive()) {
            throw new IllegalArgumentException("Your account is inactive");
        }
        if (user.getRole() == Role.JUDGE && !user.isPasswordReseted()) {
            throw new IllegalArgumentException("Please reset your password");
        }
        if (user.getRole() == Role.LAWYER && !user.isApproved()) {
            throw new IllegalArgumentException("Account pending approval");
        }
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

    @Override
    public ForgotPasswordResponse forgotPassword(ForgotPasswordRequest dto) {
        User user = userRepo.findByEmail(dto.getEmail())
                .orElseThrow(() -> new NotFoundException("User not found"));
        String otp= emailService.sendOtpEmail(user.getEmail());
        user.setOtpCode(passwordEncoder.encode(otp));
        user.setOtpExpirationTime(LocalDateTime.now().plusMinutes(10));
        userRepo.save(user);
        ForgotPasswordResponse response = new ForgotPasswordResponse();
        response.setMessage("OTP sent to email if exists");
        return response;
    }

    @Override
    public String resetPassword(ResetPasswordRequest dto) {
        User user = userRepo.findByEmail(dto.getEmail())
                .orElseThrow(() -> new NotFoundException("User not found"));
        if (user.getOtpCode() == null) {
            throw new NotFoundException("No OTP found for this user");
        }
        if (user.getOtpExpirationTime().isBefore(LocalDateTime.now())) {
            throw new NotFoundException("OTP expired");
        }
        boolean isOtpValid = passwordEncoder.matches(dto.getOtpCode(), user.getOtpCode());
        if (!isOtpValid) {
            throw new NotFoundException("Invalid OTP");
        }
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        if(!user.isPasswordReseted()){
            user.setPasswordReseted(true);
        }
        userRepo.save(user);
        return "Password reset successfully";
    }


}

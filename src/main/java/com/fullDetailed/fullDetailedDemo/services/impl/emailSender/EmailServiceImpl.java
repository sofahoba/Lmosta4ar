package com.fullDetailed.fullDetailedDemo.services.impl.emailSender;

import com.fullDetailed.fullDetailedDemo.services.interfaces.emailSender.EmailService;
import com.fullDetailed.fullDetailedDemo.util.OtpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    @Async
    public void sendOtpEmail(String email,String otpCode) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Your OTP code for Lmosta4ar application");
            message.setText("Your OTP Code is: " + otpCode + "\nThis OTP is valid for 10 minutes.");
            mailSender.send(message);
            System.out.println("Async Email Sent to " + email);
        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }
}

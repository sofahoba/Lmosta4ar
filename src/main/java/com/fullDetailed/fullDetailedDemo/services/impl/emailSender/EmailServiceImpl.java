package com.fullDetailed.fullDetailedDemo.services.impl.emailSender;

import com.fullDetailed.fullDetailedDemo.services.interfaces.emailSender.EmailService;
import com.fullDetailed.fullDetailedDemo.util.OtpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public String sendOtpEmail(String email) {
        String otp = OtpUtil.generateOtp();
        SimpleMailMessage message=new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("your otp code for Lmosta4ar application");
        message.setText("Your OTP Code is : " + otp + "\nThis OTP is valid for 10 minutes.");
        mailSender.send(message);
        return otp;
    }
}

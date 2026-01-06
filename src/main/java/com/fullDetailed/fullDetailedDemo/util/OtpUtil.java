package com.fullDetailed.fullDetailedDemo.util;

import java.security.SecureRandom;

public class OtpUtil {
    private static final SecureRandom securedRandom = new SecureRandom();

    public static String generateOtp() {
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int digit = securedRandom.nextInt(10);
            otp.append(digit);
        }
        return otp.toString();
    }
}

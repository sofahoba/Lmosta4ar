package com.fullDetailed.fullDetailedDemo.domain.dtos.auth;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterRequestDto {

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "National ID is required")
    @Size(min = 10, max = 20, message = "National ID must be valid")
    private String nationalId;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotBlank(message = "Confirm Password is required")
    private String confirmPassword;

    @Min(value = 25, message = "Age must be at least 25")
    @Max(value = 70, message = "Age must be at most 70")
    private int age;
}

package com.fullDetailed.fullDetailedDemo.domain.dtos.judge;

import com.fullDetailed.fullDetailedDemo.domain.enums.Role;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateUserDto {

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @Min(value = 18, message = "Age must be at least 18")
    @Max(value = 100, message = "Age must be at most 100")
    private int age;

    @NotBlank(message = "National ID is required")
    @Size(min = 10, max = 20, message = "National ID must be valid")
    private String nationalId;

    @NotNull(message = "Role is required")
    private Role role;

    private String court;
}
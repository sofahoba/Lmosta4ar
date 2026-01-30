package com.fullDetailed.fullDetailedDemo.domain.dtos.lawyer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class LawyerDto {
    private UUID id;
    private String firstName;

    private String lastName;

    @Email(message = "Email should be valid")
    private String email;

    @Min(value = 25, message = "Age must be at least 25")
    @Max(value = 70, message = "Age must be at most 70")
    private int age;

    private Boolean isActive;

    private Boolean isApproved;

    private int assignedCasesCount;
}

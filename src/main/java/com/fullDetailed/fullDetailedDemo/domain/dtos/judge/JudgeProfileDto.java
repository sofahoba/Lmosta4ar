package com.fullDetailed.fullDetailedDemo.domain.dtos.judge;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.UUID;

@Data
public class JudgeProfileDto {
    private UUID id;
    private String firstName;

    private String lastName;

    @Email(message = "Email should be valid")
    private String email;

    private Integer age;

    private String court;

    private Boolean isActive;

    private int assignedCasesCount;
}

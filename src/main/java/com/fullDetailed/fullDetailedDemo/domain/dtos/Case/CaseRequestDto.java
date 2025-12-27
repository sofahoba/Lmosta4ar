package com.fullDetailed.fullDetailedDemo.domain.dtos.Case;

import com.fullDetailed.fullDetailedDemo.domain.enums.CaseStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Data
public class CaseRequestDto {

    @NotBlank(message = "case number is required")
    private String caseNumber;

    @NotBlank(message = "title is required")
    @Size(min = 5, max = 200, message = "title must be between 5 and 200 characters")
    private String title;

    @NotBlank(message = "description is required")
    @Size(min = 10, message = "description must be at least 10 characters")
    private String description;

    @NotNull(message = "status is required")
    private CaseStatus status;

    @NotNull(message = "judge id is required")
    private UUID judgeId;
}


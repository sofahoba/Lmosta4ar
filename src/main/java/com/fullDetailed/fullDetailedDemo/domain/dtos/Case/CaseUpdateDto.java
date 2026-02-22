package com.fullDetailed.fullDetailedDemo.domain.dtos.Case;

import com.fullDetailed.fullDetailedDemo.domain.enums.CaseStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Data
public class CaseUpdateDto {
    private String caseNumber;
    private String title;
    private String description;
    private CaseStatus status;
    private UUID judgeId;
    private UUID lawyerId;
    private String courtRuling;
}

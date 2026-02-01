package com.fullDetailed.fullDetailedDemo.domain.dtos.Case;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class CaseRequestResponseDto {
    private UUID requestId;
    private UUID lawyerId;
    private String lawyerName;
    private UUID caseId;
    private String caseNumber;
    private String status;
    private LocalDateTime requestedAt;
}
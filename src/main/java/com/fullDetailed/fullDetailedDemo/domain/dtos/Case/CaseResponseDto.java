package com.fullDetailed.fullDetailedDemo.domain.dtos.Case;

import com.fullDetailed.fullDetailedDemo.domain.enums.CaseStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CaseResponseDto {
    private UUID id;
    private String caseNumber;
    private String title;
    private String description;
    private CaseStatus status;
    private UUID judgeId;
    private String judgeName;
    private UUID lawyerId;
    private String lawyerName;
    private String courtRuling;
    private UUID assignedById;
    private String assignedByName;
    private LocalDateTime createdAt;
    private List<CaseFileDto> caseFiles;
    private List<CaseFileDto> defenseFiles;
    private List<CaseFileDto> fileNames;
}

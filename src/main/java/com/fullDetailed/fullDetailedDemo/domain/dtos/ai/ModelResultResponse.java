package com.fullDetailed.fullDetailedDemo.domain.dtos.ai;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class ModelResultResponse {

    private UUID id;
    private String caseNumber;
    private String caseTitle;
    private String summary;
    private String court;
    private String courtLevel;
    private String jurisdiction;
    private String prosecutorName;
    private SuggestedVerdictDto suggestedVerdict;
    private String completedAgents;
    private String processingErrors;
    private boolean hasProceduralViolations;
    private int defendantCount;
    private int chargeCount;
    private double confidenceScore;
    private LocalDateTime createdAt;

    private List<DefendantDto> defendants;
    private List<ChargeDto> charges;
    private List<IncidentDto> incidents;
    private List<EvidenceDto> evidences;
    private List<WitnessStatementDto> witnessStatements;
    private List<ConfessionDto> confessions;
    private List<LabReportDto> labReports;
    private List<CriminalProceedingDto> criminalProceedings;
    private List<DefenseDocumentDto> defenseDocuments;
    private ProceduralAuditDto proceduralAudit;
}
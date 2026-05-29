package com.fullDetailed.fullDetailedDemo.domain.dtos.ai;


import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class CaseAnalysisResponse {

    private UUID resultId;
    private String caseNumber;
    private String message;
    private boolean success;

    // Case overview
    private CaseSummary caseSummary;

    // Structured sections
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

    // Processing info
    private List<String> completedAgents;
    private List<String> processingErrors;
    private String processedAt;

    @Data
    @Builder
    public static class CaseSummary {
        private String caseId;
        private String court;
        private String courtLevel;
        private String jurisdiction;
        private String filingDate;
        private String prosecutorName;
        private String suggestedVerdict;
        private int defendantCount;
        private int chargeCount;
        private boolean hasProceduralViolations;
    }
}
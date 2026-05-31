package com.fullDetailed.fullDetailedDemo.domain.dtos.ai;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AiResultDto {

    @JsonProperty("case_id")
    private String caseId;

    @JsonProperty("court")
    private String court;

    @JsonProperty("court_level")
    private String courtLevel;

    @JsonProperty("jurisdiction")
    private String jurisdiction;

    @JsonProperty("filing_date")
    private String filingDate;

    @JsonProperty("prosecutor_name")
    private String prosecutorName;

    @JsonProperty("defendants")
    private List<DefendantDto> defendants;

    @JsonProperty("charges")
    private List<ChargeDto> charges;

    @JsonProperty("incidents")
    private List<IncidentDto> incidents;

    @JsonProperty("evidences")
    private List<EvidenceDto> evidences;

    @JsonProperty("witness_statements")
    private List<WitnessStatementDto> witnessStatements;

    @JsonProperty("confessions")
    private List<ConfessionDto> confessions;

    @JsonProperty("lab_reports")
    private List<LabReportDto> labReports;

    @JsonProperty("criminal_proceedings")
    private List<CriminalProceedingDto> criminalProceedings;

    @JsonProperty("defense_documents")
    private List<DefenseDocumentDto> defenseDocuments;

    @JsonProperty("procedural_audit")
    private ProceduralAuditDto proceduralAudit;

    @JsonProperty("completed_agents")
    private List<String> completedAgents;

    @JsonProperty("errors")
    private List<String> errors;

    @JsonProperty("suggested_verdict")
    private SuggestedVerdictDto suggestedVerdict;

    @JsonProperty("last_updated")
    private String lastUpdated;
}
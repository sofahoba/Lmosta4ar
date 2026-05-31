package com.fullDetailed.fullDetailedDemo.domain.entities;

import com.fullDetailed.fullDetailedDemo.domain.dtos.ai.*;
import com.fullDetailed.fullDetailedDemo.util.JsonConverters.ChargeListConverter;
import com.fullDetailed.fullDetailedDemo.util.JsonConverters.ConfessionListConverter;
import com.fullDetailed.fullDetailedDemo.util.JsonConverters.CriminalProceedingListConverter;
import com.fullDetailed.fullDetailedDemo.util.JsonConverters.DefendantListConverter;
import com.fullDetailed.fullDetailedDemo.util.JsonConverters.DefenseDocumentListConverter;
import com.fullDetailed.fullDetailedDemo.util.JsonConverters.EvidenceListConverter;
import com.fullDetailed.fullDetailedDemo.util.JsonConverters.IncidentListConverter;
import com.fullDetailed.fullDetailedDemo.util.JsonConverters.LabReportListConverter;
import com.fullDetailed.fullDetailedDemo.util.JsonConverters.ProceduralAuditConverter;
import com.fullDetailed.fullDetailedDemo.util.JsonConverters.SuggestedVerdictConverter;
import com.fullDetailed.fullDetailedDemo.util.JsonConverters.WitnessListConverter;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "model_results")
public class ModelResult {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "case_id", nullable = false, unique = true)
    private Case caseEntity;

    @Column(columnDefinition = "TEXT")
    private String summary;

    @Column(columnDefinition = "TEXT")
    private String rawResponse;

    @Convert(converter = DefendantListConverter.class)
    @Column(columnDefinition = "TEXT")
    private List<DefendantDto> defendants;

    @Convert(converter = ChargeListConverter.class)
    @Column(columnDefinition = "TEXT")
    private List<ChargeDto> charges;

    @Convert(converter = IncidentListConverter.class)
    @Column(columnDefinition = "TEXT")
    private List<IncidentDto> incidents;

    @Convert(converter = EvidenceListConverter.class)
    @Column(columnDefinition = "TEXT")
    private List<EvidenceDto> evidences;

    @Convert(converter = WitnessListConverter.class)
    @Column(columnDefinition = "TEXT")
    private List<WitnessStatementDto> witnessStatements;

    @Convert(converter = ConfessionListConverter.class)
    @Column(columnDefinition = "TEXT")
    private List<ConfessionDto> confessions;

    @Convert(converter = LabReportListConverter.class)
    @Column(columnDefinition = "TEXT")
    private List<LabReportDto> labReports;

    @Convert(converter = CriminalProceedingListConverter.class)
    @Column(columnDefinition = "TEXT")
    private List<CriminalProceedingDto> criminalProceedings;

    @Convert(converter = DefenseDocumentListConverter.class)
    @Column(columnDefinition = "TEXT")
    private List<DefenseDocumentDto> defenseDocuments;

    @Convert(converter = ProceduralAuditConverter.class)
    @Column(columnDefinition = "TEXT")
    private ProceduralAuditDto proceduralAudit;

    @Convert(converter = SuggestedVerdictConverter.class)
    @Column(columnDefinition = "TEXT")
    private SuggestedVerdictDto suggestedVerdict;

    @Column(columnDefinition = "TEXT")
    private String court;

    @Column(columnDefinition = "TEXT")
    private String courtLevel;

    @Column(columnDefinition = "TEXT")
    private String jurisdiction;

    @Column(columnDefinition = "TEXT")
    private String prosecutorName;

    @Column(columnDefinition = "TEXT")
    private String completedAgents;

    @Column(columnDefinition = "TEXT")
    private String processingErrors;

    private boolean hasProceduralViolations;
    private int defendantCount;
    private int chargeCount;
    private double confidenceScore;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
package com.fullDetailed.fullDetailedDemo.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "case_id", nullable = false)
    private Case caseEntity;

    private String summary;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String rawResponse;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String defendantsJson;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String chargesJson;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String incidentsJson;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String evidencesJson;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String witnessStatementsJson;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String confessionsJson;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String labReportsJson;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String proceduralAuditJson;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String defenseDocumentsJson;

    private String court;
    private String courtLevel;
    private String jurisdiction;
    private String prosecutorName;
    private String suggestedVerdict;

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
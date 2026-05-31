package com.fullDetailed.fullDetailedDemo.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import com.fullDetailed.fullDetailedDemo.domain.dtos.ai.SuggestedVerdictDto;

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

    @Column(columnDefinition = "TEXT")
    private String rawResponse;

    @Column(columnDefinition = "TEXT")
    private String defendantsJson;

    @Column(columnDefinition = "TEXT")
    private String chargesJson;

    @Column(columnDefinition = "TEXT")
    private String incidentsJson;

    @Column(columnDefinition = "TEXT")
    private String evidencesJson;

    @Column(columnDefinition = "TEXT")
    private String witnessStatementsJson;

    @Column(columnDefinition = "TEXT")
    private String confessionsJson;

    @Column(columnDefinition = "TEXT")
    private String labReportsJson;

    @Column(columnDefinition = "TEXT")
    private String proceduralAuditJson;

    @Column(columnDefinition = "TEXT")
    private String defenseDocumentsJson;

    private String court;
    private String courtLevel;
    private String jurisdiction;
    private String prosecutorName;
    @Column(columnDefinition = "TEXT")
    private String suggestedVerdict;  // stores JSON string, not the DTO object

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
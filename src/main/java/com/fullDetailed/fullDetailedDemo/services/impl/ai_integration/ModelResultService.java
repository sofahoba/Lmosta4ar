package com.fullDetailed.fullDetailedDemo.services.impl.ai_integration;

import com.fullDetailed.fullDetailedDemo.domain.dtos.ai.ModelResultResponse;
import com.fullDetailed.fullDetailedDemo.domain.entities.Case;
import com.fullDetailed.fullDetailedDemo.domain.entities.ModelResult;
import com.fullDetailed.fullDetailedDemo.exceptions.NotFoundException;
import com.fullDetailed.fullDetailedDemo.repository.CaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ModelResultService {

    private final CaseRepository caseRepository;

    public ModelResultResponse getResultByCaseId(UUID caseId) {

        Case caseEntity = caseRepository.findByIdAndIsDeletedFalse(caseId)
                .orElseThrow(() -> new NotFoundException("Case not found: " + caseId));

        ModelResult m = caseEntity.getModelResult();

        if (m == null) {
            throw new NotFoundException("No AI result found for case: " + caseEntity.getCaseNumber());
        }

        return ModelResultResponse.builder()
                .id(m.getId())
                .caseNumber(caseEntity.getCaseNumber())
                .caseTitle(caseEntity.getTitle())
                .summary(m.getSummary())
                .court(m.getCourt())
                .courtLevel(m.getCourtLevel())
                .jurisdiction(m.getJurisdiction())
                .prosecutorName(m.getProsecutorName())
                .suggestedVerdict(m.getSuggestedVerdict())
                .completedAgents(m.getCompletedAgents())
                .processingErrors(m.getProcessingErrors())
                .hasProceduralViolations(m.isHasProceduralViolations())
                .defendantCount(m.getDefendantCount())
                .chargeCount(m.getChargeCount())
                .confidenceScore(m.getConfidenceScore())
                .createdAt(m.getCreatedAt())
                .defendants(m.getDefendants())
                .charges(m.getCharges())
                .incidents(m.getIncidents())
                .evidences(m.getEvidences())
                .witnessStatements(m.getWitnessStatements())
                .confessions(m.getConfessions())
                .labReports(m.getLabReports())
                .criminalProceedings(m.getCriminalProceedings())
                .defenseDocuments(m.getDefenseDocuments())
                .proceduralAudit(m.getProceduralAudit())
                .build();
    }
}
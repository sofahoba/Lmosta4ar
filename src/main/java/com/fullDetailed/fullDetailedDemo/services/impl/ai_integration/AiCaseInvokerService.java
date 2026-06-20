package com.fullDetailed.fullDetailedDemo.services.impl.ai_integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fullDetailed.fullDetailedDemo.domain.dtos.ai.*;
import com.fullDetailed.fullDetailedDemo.domain.entities.Case;
import com.fullDetailed.fullDetailedDemo.domain.entities.CaseFile;
import com.fullDetailed.fullDetailedDemo.domain.entities.ModelResult;
import com.fullDetailed.fullDetailedDemo.domain.enums.CaseStatus;
import com.fullDetailed.fullDetailedDemo.exceptions.CaseAlreadyProcessedException;
import com.fullDetailed.fullDetailedDemo.exceptions.NotFoundException;
import com.fullDetailed.fullDetailedDemo.repository.CaseRepository;
import com.fullDetailed.fullDetailedDemo.repository.ModelResultRepository;
import com.fullDetailed.fullDetailedDemo.services.impl.FileStorageService;
import com.fullDetailed.fullDetailedDemo.util.CustomMultipartFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiCaseInvokerService {

    private final CaseRepository caseRepository;
    private final FileStorageService fileStorageService;
    private final AiIntegration aiIntegration;
    private final ModelResultRepository modelResultRepository;
    private final ObjectMapper objectMapper;

    @Value("${file.upload-dir:/app/uploads/case-files}")
    private String uploadDir;

    @CacheEvict(value = "cases", allEntries = true)
    public CaseAnalysisResponse invokeCase(UUID caseId) {

        Case caseEntity = caseRepository.findById(caseId)
                .orElseThrow(() -> new NotFoundException("Case Not Found: " + caseId));

        if (modelResultRepository.existsByCaseEntity_Id(caseId)) {
            throw new CaseAlreadyProcessedException(
                    "This case already has an AI result and cannot be processed again."
            );
        }

        List<MultipartFile> filesToSend = new ArrayList<>();
        addFiles(caseEntity.getFiles(), filesToSend, "Case Files");

        if (filesToSend.isEmpty()) {
            throw new RuntimeException("No valid files found for case: " + caseId);
        }

        AiInvokeRequest request = AiInvokeRequest.builder()
                .caseId(caseId.toString())
                .files(filesToSend)
                .build();

        AiResponse aiResponse = aiIntegration.invokeCase(request);
        AiResultDto result = aiResponse.getResult();

        ModelResult modelResult = persistResult(caseEntity, aiResponse, result);

        return buildCleanResponse(caseId.toString(), aiResponse, result, modelResult);
    }

    @Transactional
    public void deleteResultById(UUID resultId) {

        ModelResult result = modelResultRepository.findById(resultId)
                .orElseThrow(() ->
                        new NotFoundException("Result not found: " + resultId));

        Case caseEntity = result.getCaseEntity();

        modelResultRepository.delete(result);

        caseEntity.setStatus(CaseStatus.PENDING);
        caseRepository.save(caseEntity);

        log.info("Deleted AI result {} and reset case {} to PENDING",
                resultId,
                caseEntity.getId());
    }

    private ModelResult persistResult(Case caseEntity, AiResponse aiResponse, AiResultDto result) {
        try {
            ProceduralAuditDto audit = result.getProceduralAudit();
            boolean hasViolations = audit != null
                    && audit.getViolations() != null
                    && !audit.getViolations().isEmpty();

            ModelResult modelResult = ModelResult.builder()
                    .caseEntity(caseEntity)
                    .summary(aiResponse.getMessage())
                    .rawResponse(objectMapper.writeValueAsString(result))
                    .defendants(result.getDefendants())
                    .charges(result.getCharges())
                    .incidents(result.getIncidents())
                    .evidences(result.getEvidences())
                    .witnessStatements(result.getWitnessStatements())
                    .confessions(result.getConfessions())
                    .labReports(result.getLabReports())
                    .criminalProceedings(result.getCriminalProceedings())
                    .defenseDocuments(result.getDefenseDocuments())
                    .proceduralAudit(audit)
                    .suggestedVerdict(result.getSuggestedVerdict())
                    .court(result.getCourt())
                    .courtLevel(result.getCourtLevel())
                    .jurisdiction(result.getJurisdiction())
                    .prosecutorName(result.getProsecutorName())
                    .completedAgents(joinList(result.getCompletedAgents()))
                    .processingErrors(joinList(result.getErrors()))
                    .hasProceduralViolations(hasViolations)
                    .defendantCount(size(result.getDefendants()))
                    .chargeCount(size(result.getCharges()))
                    .confidenceScore(0.0)
                    .build();

            Case savedCase = caseRepository.findById(caseEntity.getId()).orElseThrow(()-> new NotFoundException("Case Not Found"));
            savedCase.setStatus(CaseStatus.COMPLETED);
            caseRepository.save(savedCase);
            return modelResultRepository.save(modelResult);

        } catch (Exception e) {
            log.error("Failed to persist AI result for case: {}", caseEntity.getCaseNumber(), e);
            throw new RuntimeException("AI response persistence failed", e);
        }
    }

    private CaseAnalysisResponse buildCleanResponse(String caseNumber,
                                                     AiResponse aiResponse,
                                                     AiResultDto result,
                                                     ModelResult saved) {
        ProceduralAuditDto audit = result.getProceduralAudit();
        boolean hasViolations = audit != null
                && audit.getViolations() != null
                && !audit.getViolations().isEmpty();

        CaseAnalysisResponse.CaseSummary summary = CaseAnalysisResponse.CaseSummary.builder()
                .caseId(result.getCaseId())
                .court(result.getCourt())
                .courtLevel(result.getCourtLevel())
                .jurisdiction(result.getJurisdiction())
                .filingDate(result.getFilingDate())
                .prosecutorName(result.getProsecutorName())
                .suggestedVerdict(result.getSuggestedVerdict())
                .defendantCount(size(result.getDefendants()))
                .chargeCount(size(result.getCharges()))
                .hasProceduralViolations(hasViolations)
                .build();

        return CaseAnalysisResponse.builder()
                .resultId(saved.getId())
                .caseNumber(caseNumber)
                .message(aiResponse.getMessage())
                .success(aiResponse.isSuccess())
                .caseSummary(summary)
                .defendants(result.getDefendants())
                .charges(result.getCharges())
                .incidents(result.getIncidents())
                .evidences(result.getEvidences())
                .witnessStatements(result.getWitnessStatements())
                .confessions(result.getConfessions())
                .labReports(result.getLabReports())
                .criminalProceedings(result.getCriminalProceedings())
                .defenseDocuments(result.getDefenseDocuments())
                .proceduralAudit(audit)
                .completedAgents(result.getCompletedAgents())
                .processingErrors(result.getErrors())
                .processedAt(result.getLastUpdated())
                .build();
    }

    private String joinList(List<String> list) {
        if (list == null || list.isEmpty()) return null;
        return String.join(" | ", list);
    }

    private int size(List<?> list) {
        return list == null ? 0 : list.size();
    }

    private void addFiles(List<CaseFile> fileList, List<MultipartFile> filesToSend, String fileType) {
        if (fileList == null || fileList.isEmpty()) {
            log.info("No {} available", fileType);
            return;
        }
        for (CaseFile cf : fileList) {
            try {
                filesToSend.add(convertToMultipartFile(cf));
                log.info("Prepared {}: {}", fileType, cf.getFileName());
            } catch (Exception e) {
                log.error("Failed loading file {}: {}", cf.getFileName(), e.getMessage());
            }
        }
    }

    private MultipartFile convertToMultipartFile(CaseFile caseFile) {

    File file = fileStorageService.getFile(
            caseFile.getFileName()
    );

    return new CustomMultipartFile(
            file,
            caseFile.getOriginalFileName()
    );
}
}

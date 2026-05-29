package com.fullDetailed.fullDetailedDemo.services.impl.ai_integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fullDetailed.fullDetailedDemo.domain.dtos.ai.*;
import com.fullDetailed.fullDetailedDemo.domain.entities.Case;
import com.fullDetailed.fullDetailedDemo.domain.entities.CaseFile;
import com.fullDetailed.fullDetailedDemo.domain.entities.ModelResult;
import com.fullDetailed.fullDetailedDemo.exceptions.NotFoundException;
import com.fullDetailed.fullDetailedDemo.repository.CaseRepository;
import com.fullDetailed.fullDetailedDemo.repository.ModelResultRepository;
import com.fullDetailed.fullDetailedDemo.util.CustomMultipartFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiCaseInvokerService {

    private final CaseRepository caseRepository;
    private final AiIntegration aiIntegration;
    private final ModelResultRepository modelResultRepository;
    private final ObjectMapper objectMapper;

    @Value("${file.upload-dir:/app/uploads/case-files}")
    private String uploadDir;

    public CaseAnalysisResponse invokeCase(String caseNumber) {

        Case caseEntity = caseRepository.findByCaseNumberAndIsDeletedFalse(caseNumber)
                .orElseThrow(() -> new NotFoundException("Case Not Found: " + caseNumber));

        List<MultipartFile> filesToSend = new ArrayList<>();
        addFiles(caseEntity.getFiles(), filesToSend, "Case Files");

        if (filesToSend.isEmpty()) {
            throw new RuntimeException("No valid files found for case: " + caseNumber);
        }

        AiInvokeRequest request = AiInvokeRequest.builder()
                .caseId(caseNumber)
                .files(filesToSend)
                .build();

        AiResponse aiResponse = aiIntegration.invokeCase(request);
        AiResultDto result = aiResponse.getResult();

        ModelResult modelResult = persistResult(caseEntity, aiResponse, result);

        return buildCleanResponse(caseNumber, aiResponse, result, modelResult);
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
                    .defendantsJson(toJson(result.getDefendants()))
                    .chargesJson(toJson(result.getCharges()))
                    .incidentsJson(toJson(result.getIncidents()))
                    .evidencesJson(toJson(result.getEvidences()))
                    .witnessStatementsJson(toJson(result.getWitnessStatements()))
                    .confessionsJson(toJson(result.getConfessions()))
                    .labReportsJson(toJson(result.getLabReports()))
                    .proceduralAuditJson(toJson(audit))
                    .defenseDocumentsJson(toJson(result.getDefenseDocuments()))
                    .court(result.getCourt())
                    .courtLevel(result.getCourtLevel())
                    .jurisdiction(result.getJurisdiction())
                    .prosecutorName(result.getProsecutorName())
                    .suggestedVerdict(result.getSuggestedVerdict())
                    .completedAgents(joinList(result.getCompletedAgents()))
                    .processingErrors(joinList(result.getErrors()))
                    .hasProceduralViolations(hasViolations)
                    .defendantCount(size(result.getDefendants()))
                    .chargeCount(size(result.getCharges()))
                    .confidenceScore(0.95)
                    .build();

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

    // ── helpers ──────────────────────────────────────────────────────────────

    private String toJson(Object obj) {
        if (obj == null) return null;
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            log.warn("Could not serialize object to JSON", e);
            return null;
        }
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
        String caseId = caseFile.getCaseEntity().getId().toString();
        String fileName = caseFile.getFileName();

        String[] possiblePaths = {
                uploadDir + "/" + caseId + "/" + fileName,
                uploadDir + "/" + fileName,
                "/app/uploads/case-files/" + caseId + "/" + fileName,
                "/app/uploads/case-files/" + fileName,
                uploadDir + "/cases/" + caseId + "/" + fileName
        };

        for (String path : possiblePaths) {
            File file = new File(path);
            if (file.exists() && file.isFile()) {
                log.info("File found: {}", file.getAbsolutePath());
                return new CustomMultipartFile(file, fileName);
            }
        }
        throw new RuntimeException("File not found on disk: " + fileName);
    }
}
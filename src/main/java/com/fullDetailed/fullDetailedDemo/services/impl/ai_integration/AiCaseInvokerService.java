package com.fullDetailed.fullDetailedDemo.services.impl.ai_integration;

import com.fullDetailed.fullDetailedDemo.domain.dtos.ai.AiInvokeRequest;
import com.fullDetailed.fullDetailedDemo.domain.entities.Case;
import com.fullDetailed.fullDetailedDemo.domain.entities.CaseFile;
import com.fullDetailed.fullDetailedDemo.exceptions.NotFoundException;
import com.fullDetailed.fullDetailedDemo.repository.CaseRepository;
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

    @Value("${file.upload-dir:/app/uploads/case-files}")
    private String uploadDir;

    public String invokeCase(String caseNumber) {
        Case caseEntity = caseRepository.findByCaseNumberAndIsDeletedFalse(caseNumber)
                .orElseThrow(() -> new NotFoundException("Case Not Found: " + caseNumber));

        log.info("=== AI Invoke Started for Case: {} ===", caseNumber);
        log.info("Case ID: {}", caseEntity.getId());
        log.info("Upload Directory: {}", uploadDir);

        List<MultipartFile> filesToSend = new ArrayList<>();

        addFiles(caseEntity.getFiles(), filesToSend, "Case Files");

        if (filesToSend.isEmpty()) {
            throw new RuntimeException("No files found for case: " + caseNumber);
        }

        log.info("Total files prepared to send to AI service: {}", filesToSend.size());

        AiInvokeRequest request = AiInvokeRequest.builder()
                .caseId(caseNumber)
                .files(filesToSend)
                .build();

        return aiIntegration.invokeCase(request);
    }

    private void addFiles(List<CaseFile> fileList, List<MultipartFile> filesToSend, String fileType) {
        if (fileList == null || fileList.isEmpty()) {
            log.info("No {} available", fileType);
            return;
        }

        log.info("Found {} {}", fileList.size(), fileType);

        for (CaseFile cf : fileList) {
            try {
                MultipartFile mpFile = convertToMultipartFile(cf);
                filesToSend.add(mpFile);
                log.info(" Prepared [{}]: {}", fileType, cf.getFileName());
            } catch (Exception e) {
                log.error("❌ Failed to prepare [{}] {}: {}", fileType, cf.getFileName(), e.getMessage());
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
            log.debug("Trying path: {}", file.getAbsolutePath());

            if (file.exists() && file.isFile()) {
                log.info(" File FOUND at: {}", file.getAbsolutePath());
                return new CustomMultipartFile(file, fileName);
            }
        }

        throw new RuntimeException("File not found on disk: " + fileName);
    }
}
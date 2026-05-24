package com.fullDetailed.fullDetailedDemo.services.impl.ai_integration;

import com.fullDetailed.fullDetailedDemo.domain.dtos.ai.AiInvokeRequest;
import com.fullDetailed.fullDetailedDemo.domain.entities.Case;
import com.fullDetailed.fullDetailedDemo.exceptions.NotFoundException;
import com.fullDetailed.fullDetailedDemo.repository.CaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AiCaseInvokerService {

    private final CaseRepository caseRepository;
    private final AiIntegration aiIntegration;

    public String invokeCase(String caseNumber) {

        Case caseEntity = caseRepository.findByCaseNumberAndIsDeletedFalse(caseNumber)
                .orElseThrow(() -> new NotFoundException("Case Not Found"));

        List<String> sourceDocuments = new ArrayList<>();

        if (caseEntity.getFiles() != null && !caseEntity.getFiles().isEmpty()) {
            sourceDocuments.addAll(
                    caseEntity.getFiles()
                            .stream()
                            .map(file -> file.getFileUrl())
                            .toList()
            );
        }

        AiInvokeRequest request = AiInvokeRequest.builder()
                .caseId(caseNumber)
                .sourceDocuments(sourceDocuments)
                .build();

        return aiIntegration.invokeCase(request);
    }
}
package com.fullDetailed.fullDetailedDemo.controller.aiController;

import com.fullDetailed.fullDetailedDemo.domain.dtos.ai.AiResponse;
import com.fullDetailed.fullDetailedDemo.domain.dtos.ai.CaseAnalysisResponse;
import com.fullDetailed.fullDetailedDemo.services.impl.ai_integration.AiCaseInvokerService;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiIntegrationController {

    private final AiCaseInvokerService aiCaseInvokerService;

    @PostMapping("/invoke/{caseId}")
    public ResponseEntity<CaseAnalysisResponse> invokeCase(@PathVariable UUID caseId) {
        CaseAnalysisResponse result = aiCaseInvokerService.invokeCase(caseId);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/results/{resultId}")
    public ResponseEntity<String> deleteResult(
            @PathVariable UUID resultId
    ) {

        aiCaseInvokerService.deleteResultByCaseId(resultId);

        return ResponseEntity.ok(
                "AI result deleted successfully and case status updated to PENDING"
        );
    }
}

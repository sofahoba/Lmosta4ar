package com.fullDetailed.fullDetailedDemo.controller.aiController;

import com.fullDetailed.fullDetailedDemo.domain.dtos.ai.AiInvokeRequest;
import com.fullDetailed.fullDetailedDemo.services.impl.ai_integration.AiIntegration;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiIntegrationController {

    private final AiIntegration aiIntegration;

    @PostMapping("/invoke-case")
    public ResponseEntity<String> invokeCase(@RequestBody AiInvokeRequest caseDto) {
        String response = aiIntegration.invokeCase(caseDto);
        return ResponseEntity.ok(response);
    }
}
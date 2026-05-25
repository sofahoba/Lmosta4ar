package com.fullDetailed.fullDetailedDemo.controller.aiController;

import com.fullDetailed.fullDetailedDemo.services.impl.ai_integration.AiCaseInvokerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiIntegrationController {

    private final AiCaseInvokerService aiCaseInvokerService;

    @PostMapping("/invoke/{caseNumber}")
    public ResponseEntity<String> invokeCase(@PathVariable String caseNumber) {
        String result = aiCaseInvokerService.invokeCase(caseNumber);
        return ResponseEntity.ok(result);
    }
}
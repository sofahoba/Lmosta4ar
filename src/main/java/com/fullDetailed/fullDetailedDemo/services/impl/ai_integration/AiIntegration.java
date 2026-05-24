package com.fullDetailed.fullDetailedDemo.services.impl.ai_integration;

import com.fullDetailed.fullDetailedDemo.domain.dtos.ai.AiInvokeRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class AiIntegration {

    private final WebClient webClient;

    public String invokeCase(AiInvokeRequest request) {

        return webClient.post()
                .uri("/cases/invoke_case")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
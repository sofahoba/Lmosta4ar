package com.fullDetailed.fullDetailedDemo.services.impl.ai_integration;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fullDetailed.fullDetailedDemo.domain.dtos.Case.CaseFileDto;
import com.fullDetailed.fullDetailedDemo.domain.dtos.Case.CaseResponseDto;
import com.fullDetailed.fullDetailedDemo.domain.dtos.ai.AiInvokeRequest;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AiIntegration {
  private final WebClient webClient;


  public String invokeCase(AiInvokeRequest request) {
    return webClient.post()
        .uri("/cases/invoke_case")
        .bodyValue(request)
        .exchangeToMono(response -> {
            if (response.statusCode().is2xxSuccessful()) {
                return response.bodyToMono(String.class);
            } else {
                return response.bodyToMono(String.class)
                        .flatMap(body -> {
                            throw new RuntimeException(
                                "Status: " + response.statusCode() + " Body: " + body
                            );
                        });
            }
        })
        .block();
}
}

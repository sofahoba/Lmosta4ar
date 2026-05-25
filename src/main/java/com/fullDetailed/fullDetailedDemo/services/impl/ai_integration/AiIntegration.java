package com.fullDetailed.fullDetailedDemo.services.impl.ai_integration;

import com.fullDetailed.fullDetailedDemo.domain.dtos.ai.AiInvokeRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AiIntegration {

    private final WebClient webClient;

    public String invokeCase(AiInvokeRequest request) {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();

        // Add case_id as form field
        builder.part("case_id", request.getCaseId());

        // Add files
        if (request.getFiles() != null) {
            for (MultipartFile file : request.getFiles()) {
                builder.part("files", file.getResource())
                        .filename(file.getOriginalFilename())
                        .contentType(MediaType.APPLICATION_OCTET_STREAM);
            }
        }

        return webClient.post()
                .uri("/cases/invoke_case")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(builder.build()))
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
package com.fullDetailed.fullDetailedDemo.domain.dtos.ai;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AiInvokeRequest {

    @JsonProperty("case_id")
    private String caseId;

    @JsonProperty("source_documents")
    private List<String> sourceDocuments;
}
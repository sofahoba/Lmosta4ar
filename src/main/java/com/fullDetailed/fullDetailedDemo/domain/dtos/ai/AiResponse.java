package com.fullDetailed.fullDetailedDemo.domain.dtos.ai;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AiResponse {

    @JsonProperty("case_id")
    private String caseId;

    @JsonProperty("files_received")
    private Integer filesReceived;

    private String message;

    private AiResultDto result;

    private boolean success;
}
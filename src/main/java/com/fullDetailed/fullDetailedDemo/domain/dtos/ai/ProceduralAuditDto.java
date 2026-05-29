package com.fullDetailed.fullDetailedDemo.domain.dtos.ai;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProceduralAuditDto {
    private List<ViolationDto> violations;
    @JsonProperty("overall_assessment")
    private String overallAssessment;
    @JsonProperty("critical_nullities")
    private List<String> criticalNullities;
    @JsonProperty("kg_articles_used")
    private List<String> kgArticlesUsed;
}
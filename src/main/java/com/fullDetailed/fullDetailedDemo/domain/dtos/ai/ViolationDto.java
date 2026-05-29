package com.fullDetailed.fullDetailedDemo.domain.dtos.ai;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

// ViolationDto.java
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ViolationDto {
    @JsonProperty("procedure_type")
    private String procedureType;
    @JsonProperty("issue_description")
    private String issueDescription;
    @JsonProperty("nullity_type")
    private String nullityType;
    @JsonProperty("nullity_effect")
    private String nullityEffect;
    @JsonProperty("article_basis")
    private String articleBasis;
    @JsonProperty("conducting_officer")
    private String conductingOfficer;
}
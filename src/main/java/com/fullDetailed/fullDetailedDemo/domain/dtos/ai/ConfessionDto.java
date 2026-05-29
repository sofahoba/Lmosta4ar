package com.fullDetailed.fullDetailedDemo.domain.dtos.ai;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConfessionDto {
    @JsonProperty("defendant_name")
    private String defendantName;
    private String text;
    @JsonProperty("confession_date")
    private String confessionDate;
    @JsonProperty("confession_stage")
    private String confessionStage;
    @JsonProperty("legal_counsel_present")
    private Boolean legalCounselPresent;
    @JsonProperty("coercion_claimed")
    private Boolean coercionClaimed;
    @JsonProperty("key_admissions")
    private List<String> keyAdmissions;
}
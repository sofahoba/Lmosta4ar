package com.fullDetailed.fullDetailedDemo.domain.dtos.ai;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

// ChargeDto.java
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChargeDto {
    @JsonProperty("law_code")
    private String lawCode;
    @JsonProperty("article_number")
    private String articleNumber;
    private String description;
    @JsonProperty("incident_type")
    private String incidentType;
    @JsonProperty("charge_classification")
    private String chargeClassification;
    @JsonProperty("attempt_flag")
    private Boolean attemptFlag;
    @JsonProperty("charge_date")
    private String chargeDate;
    @JsonProperty("charge_location")
    private String chargeLocation;
    @JsonProperty("linked_defendant_names")
    private List<String> linkedDefendantNames;
}
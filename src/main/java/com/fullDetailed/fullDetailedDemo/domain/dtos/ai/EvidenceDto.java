package com.fullDetailed.fullDetailedDemo.domain.dtos.ai;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EvidenceDto {
    @JsonProperty("evidence_type")
    private String evidenceType;
    private String description;
    @JsonProperty("detailed_text")
    private String detailedText;
    @JsonProperty("seizure_date")
    private String seizureDate;
    @JsonProperty("seizure_location")
    private String seizureLocation;
    @JsonProperty("seized_by")
    private String seizedBy;
    @JsonProperty("seizure_warrant_present")
    private Boolean seizureWarrantPresent;
    @JsonProperty("linked_defendant_name")
    private String linkedDefendantName;
}
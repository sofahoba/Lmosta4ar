package com.fullDetailed.fullDetailedDemo.domain.dtos.ai;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CriminalProceedingDto {
    @JsonProperty("procedure_type")
    private String procedureType;
    private String description;
    @JsonProperty("warrant_present")
    private Boolean warrantPresent;
    @JsonProperty("conducting_officer")
    private String conductingOfficer;
}
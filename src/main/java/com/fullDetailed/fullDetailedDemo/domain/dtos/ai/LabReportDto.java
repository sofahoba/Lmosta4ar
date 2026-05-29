package com.fullDetailed.fullDetailedDemo.domain.dtos.ai;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LabReportDto {
    @JsonProperty("report_type")
    private String reportType;
    @JsonProperty("report_number")
    private String reportNumber;
    @JsonProperty("examination_date")
    private String examinationDate;
    @JsonProperty("examiner_name")
    private String examinerName;
    private String result;
    @JsonProperty("linked_defendant_name")
    private String linkedDefendantName;
}
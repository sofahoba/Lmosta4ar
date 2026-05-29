package com.fullDetailed.fullDetailedDemo.domain.dtos.ai;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DefenseDocumentDto {
    @JsonProperty("submitted_by")
    private String submittedBy;
    @JsonProperty("defendant_name")
    private String defendantName;
    @JsonProperty("formal_defenses")
    private List<String> formalDefenses;
    @JsonProperty("substantive_defenses")
    private List<String> substantiveDefenses;
    @JsonProperty("alibi_claimed")
    private Boolean alibiClaimed;
    @JsonProperty("alibi_description")
    private String alibiDescription;
}
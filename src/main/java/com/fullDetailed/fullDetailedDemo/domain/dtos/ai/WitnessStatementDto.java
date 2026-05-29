package com.fullDetailed.fullDetailedDemo.domain.dtos.ai;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WitnessStatementDto {
    @JsonProperty("witness_name")
    private String witnessName;
    @JsonProperty("witness_type")
    private String witnessType;
    private String occupation;
    @JsonProperty("relation_to_defendant")
    private String relationToDefendant;
    @JsonProperty("statement_summary")
    private String statementSummary;
    @JsonProperty("was_sworn_in")
    private Boolean wasSwornIn;
    @JsonProperty("presence_at_scene")
    private Boolean presenceAtScene;
}
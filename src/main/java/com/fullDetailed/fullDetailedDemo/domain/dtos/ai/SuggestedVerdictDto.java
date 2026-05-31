package com.fullDetailed.fullDetailedDemo.domain.dtos.ai;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SuggestedVerdictDto {

    private String verdict;

    @JsonProperty("recommended_penalty")
    private String recommendedPenalty;

    @JsonProperty("per_charge_rulings")
    private List<PerChargeRulingDto> perChargeRulings;

    @JsonProperty("operative_text")
    private String operativeText;

    @JsonProperty("confidence_score")
    private Double confidenceScore;
}
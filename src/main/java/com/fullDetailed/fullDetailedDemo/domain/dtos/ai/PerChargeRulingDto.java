package com.fullDetailed.fullDetailedDemo.domain.dtos.ai;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PerChargeRulingDto {

    @JsonProperty("charge_description")
    private String chargeDescription;

    private String verdict;
    private String penalty;
    private String reasoning;
}
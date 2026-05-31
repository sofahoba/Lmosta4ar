package com.fullDetailed.fullDetailedDemo.domain.dtos.ai;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExcludedDefenseClaimDto {
    private String claim;
    private String reason;
}
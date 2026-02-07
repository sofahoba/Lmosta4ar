package com.fullDetailed.fullDetailedDemo.domain.dtos.Case;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CaseCsvDto {

    private String caseNumber;
    private String title;
    private String description;
    private String status;
    private UUID judgeId;
    private UUID lawyerId;
    private UUID assignedById;
    private String courtRuling;

}

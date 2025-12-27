package com.fullDetailed.fullDetailedDemo.services.interfaces.Case;

import com.fullDetailed.fullDetailedDemo.domain.dtos.Case.CaseRequestDto;
import com.fullDetailed.fullDetailedDemo.domain.dtos.Case.CaseResponseDto;
import java.util.List;
import java.util.UUID;

public interface CaseService {

    CaseResponseDto createCase(CaseRequestDto caseRequestDto);
    CaseResponseDto updateCase(UUID caseId, CaseRequestDto caseRequestDto);
    List<CaseResponseDto> getAllCases();
    CaseResponseDto getCaseById(UUID caseId);
    void deleteCase(UUID caseId);
}


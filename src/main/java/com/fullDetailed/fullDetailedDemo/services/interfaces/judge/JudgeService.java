package com.fullDetailed.fullDetailedDemo.services.interfaces.judge;

import com.fullDetailed.fullDetailedDemo.domain.dtos.Case.CaseResponseDto;
import com.fullDetailed.fullDetailedDemo.domain.dtos.judge.JudgeProfileDto;
import com.fullDetailed.fullDetailedDemo.domain.enums.CaseStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.UUID;

public interface JudgeService {
    JudgeProfileDto getJudgeProfile();
    JudgeProfileDto updateJudgeProfile(JudgeProfileDto dto);
    Page<CaseResponseDto> getJudgeCases(Pageable pageable);
    CaseResponseDto getCaseById(UUID caseId);
    Page<CaseResponseDto> getCasesByStatus(CaseStatus status,Pageable pageable);
    Page<CaseResponseDto> getMyCasesByDateRange(LocalDate fromDate, LocalDate toDate, Pageable pageable);
}

package com.fullDetailed.fullDetailedDemo.services.interfaces.judge;

import com.fullDetailed.fullDetailedDemo.domain.dtos.Case.CaseResponseDto;
import com.fullDetailed.fullDetailedDemo.domain.dtos.judge.JudgeProfileDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface JudgeService {
    JudgeProfileDto getJudgeProfile();
    JudgeProfileDto updateJudgeProfile(JudgeProfileDto dto);
    Page<CaseResponseDto> getJudgeCases(Pageable pageable);
    CaseResponseDto getCaseById(UUID caseId);
}

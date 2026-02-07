package com.fullDetailed.fullDetailedDemo.config.batch;

import com.fullDetailed.fullDetailedDemo.domain.dtos.Case.CaseCsvDto;
import com.fullDetailed.fullDetailedDemo.domain.entities.Case;
import com.fullDetailed.fullDetailedDemo.domain.entities.User;
import com.fullDetailed.fullDetailedDemo.domain.enums.CaseStatus;
import com.fullDetailed.fullDetailedDemo.exceptions.NotFoundException;
import com.fullDetailed.fullDetailedDemo.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CaseEntityItemProcessor implements ItemProcessor<CaseCsvDto,Case> {

    private final UserRepo userRepo;

    @Override
    public Case process(CaseCsvDto dto) throws Exception {
        User judge=userRepo.findById(dto.getJudgeId()).orElseThrow(()->new NotFoundException("judge not found"));
        User lawyer =userRepo.findById(dto.getLawyerId()).orElseThrow(()->new NotFoundException("lawyer not found"));
        User assignedBy =userRepo.findById(dto.getAssignedById()).orElseThrow(()->new NotFoundException("user not found"));
        return Case.builder()
                .caseNumber(dto.getCaseNumber())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .status(CaseStatus.valueOf(dto.getStatus().toUpperCase()))
                .judge(judge)
                .lawyer(lawyer)
                .assignedBy(assignedBy)
                .courtRuling(dto.getCourtRuling())
                .isDeleted(false)
                .build();
    }
}

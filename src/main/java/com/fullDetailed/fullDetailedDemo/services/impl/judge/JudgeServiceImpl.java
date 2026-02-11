package com.fullDetailed.fullDetailedDemo.services.impl.judge;

import com.fullDetailed.fullDetailedDemo.domain.dtos.Case.CaseRequestDto;
import com.fullDetailed.fullDetailedDemo.domain.dtos.Case.CaseResponseDto;
import com.fullDetailed.fullDetailedDemo.domain.dtos.Case.CaseRulingDto;
import com.fullDetailed.fullDetailedDemo.domain.dtos.judge.JudgeProfileDto;
import com.fullDetailed.fullDetailedDemo.domain.entities.Case;
import com.fullDetailed.fullDetailedDemo.domain.entities.User;
import com.fullDetailed.fullDetailedDemo.domain.enums.CaseStatus;
import com.fullDetailed.fullDetailedDemo.exceptions.NotFoundException;
import com.fullDetailed.fullDetailedDemo.mapper.cases.CaseMapper;
import com.fullDetailed.fullDetailedDemo.mapper.users.judge.JudgeMapper;
import com.fullDetailed.fullDetailedDemo.repository.CaseRepository;
import com.fullDetailed.fullDetailedDemo.repository.UserRepo;
import com.fullDetailed.fullDetailedDemo.services.interfaces.judge.JudgeService;
import com.fullDetailed.fullDetailedDemo.util.PagenationHandler;
import com.fullDetailed.fullDetailedDemo.util.UserContextService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JudgeServiceImpl implements JudgeService {

    private final UserRepo userRepo;
    private final CaseRepository caseRepository;
    private final UserContextService userContextService;

    @Override
    public JudgeProfileDto getJudgeProfile() {
        User user = userContextService.getCurrentUser();
        if(user.isDeleted() || !user.isActive()){
            throw new NotFoundException("User not fount");
        }
        return JudgeMapper.toDto(user);
    }

    @Override
    @Transactional
    public JudgeProfileDto updateJudgeProfile(JudgeProfileDto dto) {
        User currentUser=userContextService.getCurrentUser();
        if(currentUser.isDeleted() || !currentUser.isActive() || currentUser.isDeleted()){
            throw new NotFoundException("User not fount");
        }
        JudgeMapper.updateEntity(currentUser,dto);
        return JudgeMapper.toDto(currentUser);
    }

    @Override
    public Page<CaseResponseDto> getJudgeCases(Pageable pageable) {
        User user=userContextService.getCurrentUser();
        if(user.isDeleted() || !user.isActive()){
            throw new NotFoundException("User not fount");
        }
        Page<Case> c=caseRepository.findByJudge(user,PagenationHandler.createCleanPageable(pageable));
        return c.map(CaseMapper::toDto);
    }

    @Override
    public CaseResponseDto getCaseById(UUID caseId) {
        Case c =caseRepository.findById(caseId).orElseThrow(()->new NotFoundException("Case not found"));
        User user=userContextService.getCurrentUser();
        if(user.isDeleted() || !user.isActive()){
            throw new NotFoundException("User not fount");
        }
        if(!c.getJudge().getId().equals(user.getId())) {
            throw new NotFoundException("case you are trying to access is not assigned to you");
        }
        return CaseMapper.toDto(c);
    }

    @Override
    public Page<CaseResponseDto> getCasesByStatus(CaseStatus status,Pageable pageable) {

        User judge = userContextService.getCurrentUser();
        return caseRepository.findByJudgeAndStatusAndIsDeletedFalse(
                judge, status, PagenationHandler.createCleanPageable(pageable)
        ).map(CaseMapper::toDto);
    }

    @Override
    public Page<CaseResponseDto> getMyCasesByDateRange(LocalDate fromDate, LocalDate toDate, Pageable pageable) {

        User judge = userContextService.getCurrentUser();

        LocalDateTime startDateTime = fromDate.atStartOfDay();
        LocalDateTime endDateTime = toDate.atTime(LocalTime.MAX);

        if (startDateTime.isAfter(endDateTime)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }
        Page<Case> cases = caseRepository.findByJudgeAndCreatedAtBetweenAndIsDeletedFalse(
                judge,
                startDateTime,
                endDateTime,
                PagenationHandler.createCleanPageable(pageable)
        );
        return cases.map(CaseMapper::toDto);
    }

    @Override
    public Page<CaseResponseDto> getAllCasesLast30Days(Pageable pageable) {

        User judge=userContextService.getCurrentUser();
        if(judge.isDeleted() || !judge.isActive()){
            throw new NotFoundException("User not found or inactive");
        }

        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusDays(30);
        Page<Case> recentCases = caseRepository.findByJudgeAndCreatedAtBetweenAndIsDeletedFalse(
                judge,
                startDate,
                endDate,
                PagenationHandler.createCleanPageable(pageable)
        );
        return recentCases.map(CaseMapper::toDto);

    }

    @Override
    @Transactional
    public CaseResponseDto updateCaseRuling(UUID caseId, CaseRulingDto dto) {
        User judge = userContextService.getCurrentUser();
        if(judge.isDeleted() || !judge.isActive() || !judge.isPasswordReseted()){
            throw new NotFoundException("User not found or inactive");
        }

        Case caseEntity = caseRepository.findById(caseId)
                .orElseThrow(() -> new NotFoundException("Case not found"));

        if (caseEntity.getJudge() == null || !caseEntity.getJudge().getId().equals(judge.getId())) {
            throw new NotFoundException("You are not authorized to rule on this case");
        }

        if (dto.getCourtRuling() != null) {
            caseEntity.setCourtRuling(dto.getCourtRuling());
        } else {
            throw new IllegalArgumentException("Court Ruling content must be provided.");
        }
        return CaseMapper.toDto(caseEntity);
    }
}

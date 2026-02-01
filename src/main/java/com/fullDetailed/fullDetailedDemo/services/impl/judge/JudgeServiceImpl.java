package com.fullDetailed.fullDetailedDemo.services.impl.judge;

import com.fullDetailed.fullDetailedDemo.domain.dtos.Case.CaseResponseDto;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JudgeServiceImpl implements JudgeService {

    private final UserRepo userRepo;
    private final CaseRepository caseRepository;
    private final UserContextService userContextService;

    @Override
    public JudgeProfileDto getJudgeProfile() {
        UUID userId=userContextService.getCurrentUserId();
        User user=userRepo.findById(userId).orElseThrow(()->new NotFoundException("User not fount"));
        if(user.isDeleted() || !user.isActive()){
            throw new NotFoundException("User not fount");
        }
        return JudgeMapper.toDto(user);
    }

    @Override
    public JudgeProfileDto updateJudgeProfile(JudgeProfileDto dto) {
        User currentUser=userContextService.getCurrentUser();
        if(currentUser.isDeleted() || !currentUser.isActive() || currentUser.isDeleted()){
            throw new NotFoundException("User not fount");
        }
        JudgeMapper.updateEntity(currentUser,dto);
        User updatedUser=userRepo.save(currentUser);
        return JudgeMapper.toDto(updatedUser);
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
        Page<Case> cases = caseRepository.findByJudgeAndStatusAndIsDeletedFalse(
                judge,
                status,
                PagenationHandler.createCleanPageable(pageable)
        );
        return cases.map(CaseMapper::toDto);
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
}

package com.fullDetailed.fullDetailedDemo.services.impl.judge;

import com.fullDetailed.fullDetailedDemo.domain.dtos.Case.CaseResponseDto;
import com.fullDetailed.fullDetailedDemo.domain.dtos.judge.JudgeProfileDto;
import com.fullDetailed.fullDetailedDemo.domain.entities.Case;
import com.fullDetailed.fullDetailedDemo.domain.entities.User;
import com.fullDetailed.fullDetailedDemo.exceptions.NotFoundException;
import com.fullDetailed.fullDetailedDemo.mapper.cases.CaseMapper;
import com.fullDetailed.fullDetailedDemo.mapper.users.judge.JudgeMapper;
import com.fullDetailed.fullDetailedDemo.repository.CaseRepository;
import com.fullDetailed.fullDetailedDemo.repository.UserRepo;
import com.fullDetailed.fullDetailedDemo.services.interfaces.judge.JudgeService;
import com.fullDetailed.fullDetailedDemo.util.UserContextService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
        return JudgeMapper.toDto(user);
    }

    @Override
    public JudgeProfileDto updateJudgeProfile(JudgeProfileDto dto) {
        User currentUser=userContextService.getCurrentUser();
        JudgeMapper.updateEntity(currentUser,dto);
        User updatedUser=userRepo.save(currentUser);
        return JudgeMapper.toDto(updatedUser);
    }

    @Override
    public Page<CaseResponseDto> getJudgeCases(Pageable pageable) {
        User user=userContextService.getCurrentUser();
        Page<Case> c=caseRepository.findByJudge(user,pageable);
        return c.map(CaseMapper::toDto);
    }

    @Override
    public CaseResponseDto getCaseById(UUID caseId) {
        Case c =caseRepository.findById(caseId).orElseThrow(()->new NotFoundException("Case not found"));
        User user=userContextService.getCurrentUser();
        if(!c.getJudge().getId().equals(user.getId())) {
            throw new NotFoundException("case you are trying to access is not assigned to you");
        }
        return CaseMapper.toDto(c);
    }
}

package com.fullDetailed.fullDetailedDemo.services.impl.cases;

import com.fullDetailed.fullDetailedDemo.domain.dtos.Case.CaseRequestDto;
import com.fullDetailed.fullDetailedDemo.domain.dtos.Case.CaseResponseDto;
import com.fullDetailed.fullDetailedDemo.domain.entities.Case;
import com.fullDetailed.fullDetailedDemo.domain.entities.User;
import com.fullDetailed.fullDetailedDemo.domain.enums.Role;
import com.fullDetailed.fullDetailedDemo.exceptions.NotFoundException;
import com.fullDetailed.fullDetailedDemo.mapper.cases.CaseMapper;
import com.fullDetailed.fullDetailedDemo.repository.CaseRepository;
import com.fullDetailed.fullDetailedDemo.repository.UserRepo;
import com.fullDetailed.fullDetailedDemo.services.interfaces.Case.CaseService;
import com.fullDetailed.fullDetailedDemo.util.UserContextService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CaseServiceImpl implements CaseService {

    private final CaseRepository caseRepository;
    private final UserRepo userRepository;
    private final UserContextService userContextService;

    @Override
    public CaseResponseDto createCase(CaseRequestDto dto) {
        User judge = null;
        if (dto.getJudgeId() != null) {
            judge = userRepository.findById(dto.getJudgeId())
                    .orElseThrow(() -> new NotFoundException("Judge not found"));
        }

        User assignedBy = userContextService.getCurrentUser();

        Case c = CaseMapper.toEntity(dto, judge, assignedBy);
        return CaseMapper.toDto(caseRepository.save(c));
    }

    @Override
    public CaseResponseDto updateCase(UUID caseId, CaseRequestDto dto) {
        Case existing = caseRepository.findById(caseId)
                .orElseThrow(() -> new NotFoundException("Case not found"));
        User judge = null;
        if (dto.getJudgeId() != null) {
            judge = userRepository.findById(dto.getJudgeId())
                    .orElseThrow(() -> new NotFoundException("Judge not found"));
        }
        User assignedBy = null;
        if (dto.getCaseNumber() != null || dto.getTitle() != null ||
                dto.getDescription() != null || dto.getStatus() != null || dto.getJudgeId() != null) {
            assignedBy = userContextService.getCurrentUser();
        }

        CaseMapper.updateEntity(existing, dto, judge, assignedBy);
        return CaseMapper.toDto(existing);
    }

    @Override
    public void deleteCase(UUID caseId) {
        Case existing = caseRepository.findById(caseId)
                .orElseThrow(() -> new NotFoundException("Case not found"));
        User user= userContextService.getCurrentUser();
        if(user.getRole()!=Role.ADMIN){
            throw new NotFoundException("the case u searching for is not accessible");
        }
        caseRepository.delete(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CaseResponseDto> getAllCases() {
        User user= userContextService.getCurrentUser();
        if(user.getRole()!=Role.ADMIN){
            throw new NotFoundException("the case u searching for is not accessible");
        }
        return caseRepository.findAll().stream().map(CaseMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CaseResponseDto getCaseById(UUID caseId) {
        Case existing = caseRepository.findById(caseId)
                .orElseThrow(() -> new NotFoundException("Case not found"));
        User user= userContextService.getCurrentUser();
        UUID userId=user.getId();
        if(existing.getJudge().getId()==userId || user.getRole()== Role.ADMIN) {
            return CaseMapper.toDto(existing);
        }
        throw new NotFoundException("the case u searching for is not accessible");
    }
}
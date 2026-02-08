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
import org.springframework.util.StringUtils;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class CaseEntityItemProcessor implements ItemProcessor<CaseCsvDto,Case> {

    private final UserRepo userRepo;

    @Override
    public Case process(CaseCsvDto dto) throws Exception {
        log.info("📝 Processing case: {}", dto.getCaseNumber());

        validateRequired(dto.getCaseNumber(), "caseNumber");
        validateRequired(dto.getTitle(), "title");
        validateRequired(dto.getStatus(), "status");
        validateRequired(dto.getAssignedById(), "assignedById");

        User judge = null;
        if (hasValue(dto.getJudgeId())) {
            UUID judgeUuid = UUID.fromString(dto.getJudgeId().trim());
            judge = userRepo.findById(judgeUuid)
                    .orElseThrow(() -> new NotFoundException("Judge not found: " + judgeUuid));
        } else {
            log.info("No judge assigned for case: {}", dto.getCaseNumber());
        }

        User lawyer = null;
        if (hasValue(dto.getLawyerId())) {
            UUID lawyerUuid = UUID.fromString(dto.getLawyerId().trim());
            lawyer = userRepo.findById(lawyerUuid)
                    .orElseThrow(() -> new NotFoundException("Lawyer not found: " + lawyerUuid));
        } else {
            log.info("🟢🟢🟢🟢🟢 No lawyer assigned for case: {}", dto.getCaseNumber());
        }

        UUID assignedByUuid = UUID.fromString(dto.getAssignedById().trim());
        User assignedBy = userRepo.findById(assignedByUuid)
                .orElseThrow(() -> new NotFoundException("AssignedBy user not found: " + assignedByUuid));

        Case caseEntity = Case.builder()
                .caseNumber(dto.getCaseNumber().trim())
                .title(dto.getTitle().trim())
                .description(getOptionalValue(dto.getDescription()))
                .status(CaseStatus.valueOf(dto.getStatus().toUpperCase().trim()))
                .judge(judge)
                .lawyer(lawyer)
                .assignedBy(assignedBy)
                .courtRuling(getOptionalValue(dto.getCourtRuling()))
                .isDeleted(false)
                .build();

        log.info("🟢🟢🟢🟢🟢processed case: {}", caseEntity.getCaseNumber());
        return caseEntity;
    }


    private boolean hasValue(String value) {
        return StringUtils.hasText(value);
    }


    private String getOptionalValue(String value) {
        return hasValue(value) ? value.trim() : null;
    }


    private void validateRequired(String value, String fieldName) {
        if (!hasValue(value)) {
            throw new IllegalArgumentException("Field '" + fieldName + "' is required and cannot be empty");
        }
    }
}

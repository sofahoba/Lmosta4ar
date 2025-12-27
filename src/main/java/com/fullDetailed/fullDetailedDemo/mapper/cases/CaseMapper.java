package com.fullDetailed.fullDetailedDemo.mapper.cases;

import com.fullDetailed.fullDetailedDemo.domain.dtos.Case.CaseRequestDto;
import com.fullDetailed.fullDetailedDemo.domain.dtos.Case.CaseResponseDto;
import com.fullDetailed.fullDetailedDemo.domain.entities.Case;
import com.fullDetailed.fullDetailedDemo.domain.entities.User;

public class CaseMapper {

    public static CaseResponseDto toDto(Case caseEntity) {
        if (caseEntity == null) return null;

        CaseResponseDto dto = new CaseResponseDto();
        dto.setId(caseEntity.getId());
        dto.setCaseNumber(caseEntity.getCaseNumber());
        dto.setTitle(caseEntity.getTitle());
        dto.setDescription(caseEntity.getDescription());
        dto.setStatus(caseEntity.getStatus());
        dto.setCreatedAt(caseEntity.getCreatedAt());
        dto.setCreatedAt(caseEntity.getCreatedAt());

        if (caseEntity.getJudge() != null) {
            dto.setJudgeId(caseEntity.getJudge().getId());
            dto.setJudgeName(caseEntity.getJudge().getFirstName() + " " + caseEntity.getJudge().getLastName());
        }

        if (caseEntity.getAssignedBy() != null) {
            dto.setAssignedById(caseEntity.getAssignedBy().getId());
            dto.setAssignedByName(caseEntity.getAssignedBy().getFirstName() + " " + caseEntity.getAssignedBy().getLastName());
        }

        return dto;
    }

    public static Case toEntity(CaseRequestDto dto, User judge, User assignedBy) {
        Case c = new Case();
        c.setCaseNumber(dto.getCaseNumber());
        c.setTitle(dto.getTitle());
        c.setDescription(dto.getDescription());
        c.setStatus(dto.getStatus());
        c.setJudge(judge);
        c.setAssignedBy(assignedBy);
        return c;
    }

    public static void updateEntity(Case entity, CaseRequestDto dto, User judge, User assignedBy) {
        if (dto.getCaseNumber() != null) {
            entity.setCaseNumber(dto.getCaseNumber());
        }
        if (dto.getTitle() != null) {
            entity.setTitle(dto.getTitle());
        }
        if (dto.getDescription() != null) {
            entity.setDescription(dto.getDescription());
        }
        if (dto.getStatus() != null) {
            entity.setStatus(dto.getStatus());
        }
        if (judge != null) {
            entity.setJudge(judge);
        }
        if (assignedBy != null) {
            entity.setAssignedBy(assignedBy);
        }
    }
}


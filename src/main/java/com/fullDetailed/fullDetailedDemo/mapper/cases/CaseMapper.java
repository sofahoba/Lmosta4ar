package com.fullDetailed.fullDetailedDemo.mapper.cases;


import com.fullDetailed.fullDetailedDemo.domain.dtos.Case.CaseFileDto;
import com.fullDetailed.fullDetailedDemo.domain.dtos.Case.CaseRequestDto;
import com.fullDetailed.fullDetailedDemo.domain.dtos.Case.CaseResponseDto;
import com.fullDetailed.fullDetailedDemo.domain.entities.Case;
import com.fullDetailed.fullDetailedDemo.domain.entities.CaseFile;
import com.fullDetailed.fullDetailedDemo.domain.entities.User;
import com.fullDetailed.fullDetailedDemo.domain.enums.Role;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        dto.setCourtRuling(caseEntity.getCourtRuling());
        if (caseEntity.getJudge() != null) {
            dto.setJudgeId(caseEntity.getJudge().getId());
            dto.setJudgeName(caseEntity.getJudge().getFirstName() + " " + caseEntity.getJudge().getLastName());
        }

        if (caseEntity.getLawyer() != null) {
            dto.setLawyerId(caseEntity.getLawyer().getId());
            dto.setLawyerName(caseEntity.getLawyer().getFirstName() + " " + caseEntity.getLawyer().getLastName());
        }

        if (caseEntity.getAssignedBy() != null) {
            dto.setAssignedById(caseEntity.getAssignedBy().getId());
            dto.setAssignedByName(caseEntity.getAssignedBy().getFirstName() + " " + caseEntity.getAssignedBy().getLastName());
        }

        List<CaseFileDto> generalCaseFiles = new ArrayList<>();
        List<CaseFileDto> defenseFiles = new ArrayList<>();

        if (caseEntity.getFiles() != null && !caseEntity.getFiles().isEmpty()) {

            for (CaseFile fileEntity : caseEntity.getFiles()) {
                CaseFileDto fileDto = mapFileToDto(fileEntity);

                if (fileEntity.getUploadedBy() != null) {
                    Role uploaderRole = fileEntity.getUploadedBy().getRole();

                    if (uploaderRole == Role.LAWYER) {
                        defenseFiles.add(fileDto);
                    } else {
                        generalCaseFiles.add(fileDto);
                    }
                } else {
                    generalCaseFiles.add(fileDto);
                }
            }
        }

        dto.setCaseFiles(generalCaseFiles);
        dto.setDefenseFiles(defenseFiles);

        return dto;
    }

    public static Case toEntity(CaseRequestDto dto, User judge, User assignedBy, User lawyer) {
        Case c = new Case();
        c.setCaseNumber(dto.getCaseNumber());
        c.setTitle(dto.getTitle());
        c.setDescription(dto.getDescription());
        c.setStatus(dto.getStatus());
        c.setJudge(judge);
        c.setLawyer(lawyer);
        c.setAssignedBy(assignedBy);
        c.setCourtRuling(dto.getCourtRuling());
        return c;
    }

    public static void updateEntity(Case entity, CaseRequestDto dto, User judge,User lawyer) {
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
        if (lawyer != null) {
            entity.setLawyer(lawyer);
        }
        if (dto.getCourtRuling() != null) {
            entity.setCourtRuling(dto.getCourtRuling());
        }
    }

    private static CaseFileDto mapFileToDto(CaseFile fileEntity) {
        CaseFileDto fileDto = new CaseFileDto();
        fileDto.setId(fileEntity.getId());
        fileDto.setFileUrl(fileEntity.getFileUrl());
        fileDto.setFileType(fileEntity.getFileType());
        fileDto.setUploadedAt(fileEntity.getUploadedAt());

        if (fileEntity.getUploadedBy() != null) {
            fileDto.setUploadedByName(
                    fileEntity.getUploadedBy().getFirstName() + " " + fileEntity.getUploadedBy().getLastName()
            );
        }
        return fileDto;
    }
}


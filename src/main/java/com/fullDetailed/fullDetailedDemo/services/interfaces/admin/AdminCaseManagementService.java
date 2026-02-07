package com.fullDetailed.fullDetailedDemo.services.interfaces.admin;

import com.fullDetailed.fullDetailedDemo.domain.dtos.Case.CaseFileDownloadDto;
import com.fullDetailed.fullDetailedDemo.domain.dtos.Case.CaseRequestDto;
import com.fullDetailed.fullDetailedDemo.domain.dtos.Case.CaseResponseDto;
import com.fullDetailed.fullDetailedDemo.domain.enums.CaseStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;

import java.util.List;
import java.util.UUID;

public interface AdminCaseManagementService {

    void assignCaseToJudge(UUID judgeId,UUID caseId);
    CaseResponseDto createCase(CaseRequestDto request);
    void updateCase(UUID caseId,CaseRequestDto request);
    void deleteCase(UUID caseId);
    Page<CaseResponseDto>getAllCases(Pageable pageable);
    CaseResponseDto getCaseById(UUID caseId);
    Page<CaseResponseDto> getCasesByStatus(CaseStatus status,Pageable pageable);
    List<String> uploadCaseFiles(UUID caseId, List<MultipartFile> files);
    Page<CaseResponseDto> getAllDeletedCases(Pageable pageable);
    Page<CaseResponseDto> getAllFullyAssignedCases(Pageable pageable);
    void importCasesFromCsv(MultipartFile file);
}

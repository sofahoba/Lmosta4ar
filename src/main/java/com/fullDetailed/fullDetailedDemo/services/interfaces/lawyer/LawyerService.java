package com.fullDetailed.fullDetailedDemo.services.interfaces.lawyer;

import com.fullDetailed.fullDetailedDemo.domain.dtos.ApiResponse;
import com.fullDetailed.fullDetailedDemo.domain.dtos.Case.CaseRequestResponseDto;
import com.fullDetailed.fullDetailedDemo.domain.dtos.Case.CaseResponseDto;
import com.fullDetailed.fullDetailedDemo.domain.dtos.Case.RequestCaseDto;
import com.fullDetailed.fullDetailedDemo.domain.dtos.lawyer.LawyerDto;
import com.fullDetailed.fullDetailedDemo.domain.enums.RequestStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface LawyerService {

    LawyerDto getLawyerProfile();
    LawyerDto updateProfile(LawyerDto dto);
    Page<CaseResponseDto>getAllCases(Pageable pageable);
    void reqeustAccessOnCaseByCaseNumber(RequestCaseDto requestDto);
    List<String> uploadCaseFiles(UUID caseId, List<MultipartFile> files);
    CaseResponseDto getCaseById(UUID caseId);
    ApiResponse<Void> deleteFile(UUID fileId);
    Page<CaseRequestResponseDto> getAllCaseRequestsByStatus(RequestStatus status, Pageable pageable);
    Page<CaseRequestResponseDto>getAllCaseRequests(Pageable pageable);
}

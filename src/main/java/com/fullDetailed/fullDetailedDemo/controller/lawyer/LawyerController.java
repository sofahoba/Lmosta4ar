package com.fullDetailed.fullDetailedDemo.controller.lawyer;

import com.fullDetailed.fullDetailedDemo.domain.dtos.ApiResponse;
import com.fullDetailed.fullDetailedDemo.domain.dtos.Case.CaseFileDownloadDto;
import com.fullDetailed.fullDetailedDemo.domain.dtos.Case.CaseRequestResponseDto;
import com.fullDetailed.fullDetailedDemo.domain.dtos.Case.CaseResponseDto;
import com.fullDetailed.fullDetailedDemo.domain.dtos.Case.RequestCaseDto;
import com.fullDetailed.fullDetailedDemo.domain.dtos.lawyer.LawyerDto;
import com.fullDetailed.fullDetailedDemo.domain.enums.RequestStatus;
import com.fullDetailed.fullDetailedDemo.services.impl.cassefiles.FilesServices;
import com.fullDetailed.fullDetailedDemo.services.interfaces.lawyer.LawyerService;
import com.fullDetailed.fullDetailedDemo.util.ResponseHelper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/lawyer")
@RequiredArgsConstructor
public class LawyerController {

    private final LawyerService lawyerService;
    private final FilesServices filesServices;

    // ==================== PROFILE ====================

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<LawyerDto>> getLawyerProfile() {
        LawyerDto profile = lawyerService.getLawyerProfile();
        return ResponseHelper.ok(profile, "Lawyer profile retrieved successfully");
    }

    @PatchMapping("/profile")
    public ResponseEntity<ApiResponse<LawyerDto>> updateProfile(
            @Valid @RequestBody LawyerDto dto) {
        LawyerDto updatedProfile = lawyerService.updateProfile(dto);
        return ResponseHelper.ok(updatedProfile, "Lawyer profile updated successfully");
    }

    // ==================== CASES ====================

    @GetMapping("/cases")
    public ResponseEntity<ApiResponse<List<CaseResponseDto>>> getAllCases(@ParameterObject Pageable pageable) {
        Page<CaseResponseDto> cases = lawyerService.getAllCases(pageable);
        return ResponseHelper.ok(cases, "Cases retrieved successfully");
    }

    @GetMapping("/cases/{caseId}")
    public ResponseEntity<ApiResponse<CaseResponseDto>> getCaseById(@PathVariable UUID caseId) {
        CaseResponseDto caseDetails = lawyerService.getCaseById(caseId);
        return ResponseHelper.ok(caseDetails, "Case retrieved successfully");
    }

    // ==================== CASE ACCESS REQUEST ====================

    @PostMapping("/cases/request-access")
    public ResponseEntity<ApiResponse<Void>> requestAccessToCase(
            @Valid @RequestBody RequestCaseDto requestDto) {
        lawyerService.reqeustAccessOnCaseByCaseNumber(requestDto);
        return ResponseHelper.ok("Case access request sent successfully to the Administrator");
    }

    // ==================== FILE UPLOAD ====================

    @PostMapping(value = "/cases/{caseId}/files", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<List<String>>> uploadDefenseFiles(
            @PathVariable UUID caseId,
            @RequestParam("files") List<MultipartFile> files) {
        List<String> uploadedUrls = lawyerService.uploadCaseFiles(caseId, files);
        return ResponseHelper.ok(uploadedUrls, files.size() + " defense file(s) uploaded successfully");
    }

    @GetMapping("/{caseId}/files/{filename}")
    public ResponseEntity<Resource> openCaseFile(
            @PathVariable UUID caseId,
            @PathVariable String filename) {

        CaseFileDownloadDto fileDto = filesServices.getCaseFile(caseId, filename);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(fileDto.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDto.getResource().getFilename() + "\"")
                .body(fileDto.getResource());
    }

    @DeleteMapping("/case-file/{fileId}")
    public ResponseEntity<Void> deleteFile(@PathVariable UUID fileId) {
        lawyerService.deleteFile(fileId);
        return ResponseEntity.noContent().build();
    }

    // ==================== CASE REQUESTS ====================

    @GetMapping("/request-status")
    public ResponseEntity<ApiResponse<List<CaseRequestResponseDto>>> getAllCaseRequests(
            @ParameterObject Pageable pageable) {

        Page<CaseRequestResponseDto> requests =
                lawyerService.getAllCaseRequests(pageable);

        return ResponseHelper.ok(
                requests,
                "Case requests retrieved successfully"
        );
    }

    @GetMapping("/case-requests")
    public ResponseEntity<ApiResponse<List<CaseRequestResponseDto>>> getCaseRequestsByStatus(
            @RequestParam RequestStatus status,
            @ParameterObject Pageable pageable) {

        Page<CaseRequestResponseDto> requests =
                lawyerService.getAllCaseRequestsByStatus(status, pageable);

        return ResponseHelper.ok(
                requests,
                "Case requests retrieved successfully"
        );
    }
}

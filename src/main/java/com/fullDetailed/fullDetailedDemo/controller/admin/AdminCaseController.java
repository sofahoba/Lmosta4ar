package com.fullDetailed.fullDetailedDemo.controller.admin;

import com.fullDetailed.fullDetailedDemo.domain.dtos.ApiResponse;
import com.fullDetailed.fullDetailedDemo.domain.dtos.Case.CaseFileDownloadDto;
import com.fullDetailed.fullDetailedDemo.domain.dtos.Case.CaseRequestDto;
import com.fullDetailed.fullDetailedDemo.domain.dtos.Case.CaseResponseDto;
import com.fullDetailed.fullDetailedDemo.domain.enums.CaseStatus;
import com.fullDetailed.fullDetailedDemo.services.impl.cassefiles.FilesServices;
import com.fullDetailed.fullDetailedDemo.services.interfaces.admin.AdminCaseManagementService;
import com.fullDetailed.fullDetailedDemo.util.ResponseHelper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/api/v1/admin/cases")
@RequiredArgsConstructor
public class AdminCaseController {

    private final AdminCaseManagementService caseService;
    private final FilesServices filesServices;

    // ==================== CASE CRUD ====================

    @PostMapping
    public ResponseEntity<ApiResponse<CaseResponseDto>> createCase(
            @Valid @RequestBody CaseRequestDto request) {
        CaseResponseDto createdCase = caseService.createCase(request);
        return ResponseHelper.created(createdCase, "Case created successfully");
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CaseResponseDto>>> getAllCases(Pageable pageable) {
        Page<CaseResponseDto> cases = caseService.getAllCases(pageable);
        return ResponseHelper.ok(cases, "Cases retrieved successfully");
    }

    @GetMapping("/{caseId}")
    public ResponseEntity<ApiResponse<CaseResponseDto>> getCaseById(@PathVariable UUID caseId) {
        CaseResponseDto caseDto = caseService.getCaseById(caseId);
        return ResponseHelper.ok(caseDto, "Case retrieved successfully");
    }

    @PutMapping("/{caseId}")
    public ResponseEntity<ApiResponse<Void>> updateCase(
            @PathVariable UUID caseId,
            @Valid @RequestBody CaseRequestDto request) {
        caseService.updateCase(caseId, request);
        return ResponseHelper.ok("Case updated successfully");
    }

    @DeleteMapping("/{caseId}")
    public ResponseEntity<ApiResponse<Void>> deleteCase(@PathVariable UUID caseId) {
        caseService.deleteCase(caseId);
        return ResponseHelper.ok("Case deleted successfully");
    }

    // ==================== CASE FILTERING ====================

    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<CaseResponseDto>>> getCasesByStatus(
            @PathVariable CaseStatus status,
            Pageable pageable) {
        Page<CaseResponseDto> cases = caseService.getCasesByStatus(status, pageable);
        return ResponseHelper.ok(cases, "Cases with status '" + status + "' retrieved successfully");
    }

    @GetMapping("/deleted")
    public ResponseEntity<ApiResponse<List<CaseResponseDto>>> getDeletedCases(Pageable pageable) {
        Page<CaseResponseDto> cases = caseService.getAllDeletedCases(pageable);
        return ResponseHelper.ok(cases, "Deleted cases retrieved successfully");
    }

    @GetMapping("/fully-assigned")
    public ResponseEntity<ApiResponse<List<CaseResponseDto>>> getAllFullyAssignedCases(Pageable pageable) {
        Page<CaseResponseDto> cases = caseService.getAllFullyAssignedCases(pageable);
        return ResponseHelper.ok(cases, "Fully assigned cases retrieved successfully");
    }

    // ==================== JUDGE ASSIGNMENT ====================

    @PutMapping("/{caseId}/assign/{judgeId}")
    public ResponseEntity<ApiResponse<Void>> assignJudge(
            @PathVariable UUID caseId,
            @PathVariable UUID judgeId) {
        caseService.assignCaseToJudge(judgeId, caseId);
        return ResponseHelper.ok("Judge assigned to case successfully");
    }

    // ==================== FILE UPLOAD ====================

    @PostMapping(value = "/{caseId}/files", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<List<String>>> uploadCaseFiles(
            @PathVariable UUID caseId,
            @RequestParam("files") List<MultipartFile> files) {
        List<String> fileUrls = caseService.uploadCaseFiles(caseId, files);
        return ResponseHelper.ok(fileUrls, files.size() + " file(s) uploaded successfully");
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
}
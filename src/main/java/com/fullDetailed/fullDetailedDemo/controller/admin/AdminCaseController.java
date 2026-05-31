package com.fullDetailed.fullDetailedDemo.controller.admin;

import com.fullDetailed.fullDetailedDemo.domain.dtos.ApiResponse;
import com.fullDetailed.fullDetailedDemo.domain.dtos.Case.CaseFileDownloadDto;
import com.fullDetailed.fullDetailedDemo.domain.dtos.Case.CaseRequestDto;
import com.fullDetailed.fullDetailedDemo.domain.dtos.Case.CaseResponseDto;
import com.fullDetailed.fullDetailedDemo.domain.dtos.Case.CaseUpdateDto;
import com.fullDetailed.fullDetailedDemo.domain.dtos.ai.ModelResultResponse;
import com.fullDetailed.fullDetailedDemo.domain.enums.AssignStatus;
import com.fullDetailed.fullDetailedDemo.domain.enums.CaseStatus;
import com.fullDetailed.fullDetailedDemo.services.impl.ai_integration.ModelResultService;
import com.fullDetailed.fullDetailedDemo.services.impl.cassefiles.FilesServices;
import com.fullDetailed.fullDetailedDemo.services.interfaces.admin.AdminCaseManagementService;
import com.fullDetailed.fullDetailedDemo.util.ResponseHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/cases")
@RequiredArgsConstructor
@Slf4j
public class AdminCaseController {

    private final AdminCaseManagementService caseService;
    private final FilesServices filesServices;
    private final ModelResultService modelResultService;

    // ==================== CASE CRUD ====================

    @PostMapping
    public ResponseEntity<ApiResponse<CaseResponseDto>> createCase(
            @Valid @RequestBody CaseRequestDto request) {
        CaseResponseDto createdCase = caseService.createCase(request);
        return ResponseHelper.created(createdCase, "Case created successfully");
    }

    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Import cases from CSV file",
            description = "Upload a CSV file to bulk import cases. The job runs asynchronously."
    )
    public ResponseEntity<Map<String, String>> importCasesFromCsv(
            @Parameter(
                    description = "CSV file containing case data",
                    required = true,
                    content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)
            )
            @RequestParam("file") MultipartFile file
    ) {
        log.info("fileeees recievedddd",
                file.getOriginalFilename(),
                file.getSize());

        caseService.importCasesFromCsv(file);

        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(Map.of(
                        "status", "ACCEPTED",
                        "message", "Import job started successfully",
                        "fileName", file.getOriginalFilename()
                ));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CaseResponseDto>>> getAllCases(@ParameterObject Pageable pageable) {
        Page<CaseResponseDto> cases = caseService.getAllCases(pageable);
        return ResponseHelper.ok(cases, "Cases retrieved successfully");
    }

    @GetMapping("/{caseId}")
    public ResponseEntity<ApiResponse<CaseResponseDto>> getCaseById(@PathVariable UUID caseId) {
        CaseResponseDto caseDto = caseService.getCaseById(caseId);
        return ResponseHelper.ok(caseDto, "Case retrieved successfully");
    }

    @PatchMapping("/{caseId}")
    public ResponseEntity<ApiResponse<Void>> updateCase(
            @PathVariable UUID caseId,
            @Valid @RequestBody CaseUpdateDto request) {
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
            @ParameterObject Pageable pageable) {
        Page<CaseResponseDto> cases = caseService.getCasesByStatus(status, pageable);
        return ResponseHelper.ok(cases, "Cases with status '" + status + "' retrieved successfully");
    }

    @GetMapping("/deleted")
    public ResponseEntity<ApiResponse<List<CaseResponseDto>>> getDeletedCases(@ParameterObject Pageable pageable) {
        Page<CaseResponseDto> cases = caseService.getAllDeletedCases(pageable);
        return ResponseHelper.ok(cases, "Deleted cases retrieved successfully");
    }

    @GetMapping("/fully-assigned")
    public ResponseEntity<ApiResponse<List<CaseResponseDto>>> getAllFullyAssignedCases(@ParameterObject Pageable pageable) {
        Page<CaseResponseDto> cases = caseService.getAllFullyAssignedCases(pageable);
        return ResponseHelper.ok(cases, "Fully assigned cases retrieved successfully");
    }

    // ==================== JUDGE ASSIGNMENT ====================

    @PatchMapping("/{caseId}/assign/{judgeId}")
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


    @GetMapping("/cases-with-status")
    public ResponseEntity<Page<CaseResponseDto>> getCasesByAssignedStatus(@RequestParam AssignStatus status,@ParameterObject Pageable pageable) {
        Page<CaseResponseDto> cases = caseService.getCasesByAssignedStatus(pageable, status);
        return ResponseEntity.ok(cases);
    }

    @GetMapping("/count-case-status")
    public ResponseEntity<Long> getCasesCountByAssignedStatus(
            @RequestParam AssignStatus status) {
        long count = caseService.getCasesCountByAssignedStatus(status);
        return ResponseEntity.ok(count);
    }

    @DeleteMapping("/case-file/{fileId}")
    public ResponseEntity<Void> deleteFile(@PathVariable UUID fileId) {
        caseService.deleteFile(fileId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/cases/{caseId}/result")
    public ResponseEntity<ApiResponse<ModelResultResponse>> getCaseResult(@PathVariable UUID caseId) {
        ModelResultResponse result = modelResultService.getResultByCaseId(caseId);
        return ResponseHelper.ok(result, "Model result retrieved successfully");
    }

}
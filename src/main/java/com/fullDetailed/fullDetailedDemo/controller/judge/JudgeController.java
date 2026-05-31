package com.fullDetailed.fullDetailedDemo.controller.judge;

import com.fullDetailed.fullDetailedDemo.domain.dtos.ApiResponse;
import com.fullDetailed.fullDetailedDemo.domain.dtos.Case.CaseFileDownloadDto;
import com.fullDetailed.fullDetailedDemo.domain.dtos.Case.CaseResponseDto;
import com.fullDetailed.fullDetailedDemo.domain.dtos.Case.CaseRulingDto;
import com.fullDetailed.fullDetailedDemo.domain.dtos.ai.ModelResultResponse;
import com.fullDetailed.fullDetailedDemo.domain.dtos.judge.JudgeProfileDto;
import com.fullDetailed.fullDetailedDemo.domain.enums.CaseStatus;
import com.fullDetailed.fullDetailedDemo.services.impl.ai_integration.ModelResultService;
import com.fullDetailed.fullDetailedDemo.services.impl.cassefiles.FilesServices;
import com.fullDetailed.fullDetailedDemo.services.interfaces.judge.JudgeService;
import com.fullDetailed.fullDetailedDemo.util.ResponseHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/judges")
@RequiredArgsConstructor
public class JudgeController {

    private final JudgeService judgeService;
    private final FilesServices filesServices;
    private final ModelResultService modelResultService;

    // ==================== PROFILE ====================

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<JudgeProfileDto>> getJudgeProfile() {
        JudgeProfileDto judgeProfileDto = judgeService.getJudgeProfile();
        return ResponseHelper.ok(judgeProfileDto, "Judge profile retrieved successfully");
    }

    @PatchMapping("/profile/update")
    public ResponseEntity<ApiResponse<JudgeProfileDto>> updateJudgeProfile(
            @Valid @RequestBody JudgeProfileDto dto) {
        JudgeProfileDto updatedDto = judgeService.updateJudgeProfile(dto);
        return ResponseHelper.ok(updatedDto, "Judge profile updated successfully");
    }

    // ==================== CASES ====================

    @GetMapping("/all-cases")
    public ResponseEntity<ApiResponse<Page<CaseResponseDto>>> getJudgeCases(@ParameterObject Pageable pageable) {
        Page<CaseResponseDto> cases = judgeService.getJudgeCases(pageable);
        return ResponseHelper.okPage(cases, "Judge cases retrieved successfully");
    }

    @GetMapping("/case/{caseId}")
    public ResponseEntity<ApiResponse<CaseResponseDto>> getCaseById(@PathVariable UUID caseId) {
        CaseResponseDto caseResponseDto = judgeService.getCaseById(caseId);
        return ResponseHelper.ok(caseResponseDto, "Case retrieved successfully");
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<Page<CaseResponseDto>>> getCasesByStatus(
            @PathVariable CaseStatus status,
            @ParameterObject Pageable pageable) {
        Page<CaseResponseDto> cases = judgeService.getCasesByStatus(status, pageable);
        return ResponseHelper.okPage(cases, "Cases with status '" + status + "' retrieved successfully");
    }

    @GetMapping("/search-date")
    @Operation(
            summary = "Search cases by date range",
            description = "Retrieves cases within a specified date range. Date format: **yyyy-MM-dd** (e.g., 2026-01-15)"
    )
    public ResponseEntity<ApiResponse<Page<CaseResponseDto>>> getCasesByDateRange(
            @RequestParam("from")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @Parameter(
                    description = "Start date (format: yyyy-MM-dd)",
                    example = "2026-01-01",
                    required = true
            )
            LocalDate fromDate,

            @RequestParam("to")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @Parameter(
                    description = "End date (format: yyyy-MM-dd)",
                    example = "2026-12-31",
                    required = true
            )
            LocalDate toDate,

            @ParameterObject Pageable pageable) {
        Page<CaseResponseDto> cases = judgeService.getMyCasesByDateRange(fromDate, toDate, pageable);
        return ResponseHelper.okPage(cases, "Cases from " + fromDate + " to " + toDate + " retrieved successfully");
    }

    @GetMapping("/cases/recent")
    public ResponseEntity<ApiResponse<Page<CaseResponseDto>>> getRecentCases(@ParameterObject Pageable pageable) {
        Page<CaseResponseDto> cases = judgeService.getAllCasesLast30Days(pageable);
        return ResponseHelper.okPage(cases, "Recent cases (last 30 days) retrieved successfully");
    }

    // ==================== RULING ====================

    @PatchMapping("/cases/{caseId}/ruling")
    public ResponseEntity<ApiResponse<CaseResponseDto>> updateCaseRuling(
            @PathVariable UUID caseId,
            @Valid @RequestBody CaseRulingDto dto) {
        CaseResponseDto updatedCase = judgeService.updateCaseRuling(caseId, dto);
        return ResponseHelper.ok(updatedCase, "Case ruling updated successfully");
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

    @GetMapping("/cases/{caseId}/result")
    public ResponseEntity<ApiResponse<ModelResultResponse>> getCaseResult(@PathVariable UUID caseId) {
        ModelResultResponse result = modelResultService.getResultByCaseId(caseId);
        return ResponseHelper.ok(result, "Model result retrieved successfully");
    }
}
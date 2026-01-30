package com.fullDetailed.fullDetailedDemo.controller.admin;

import com.fullDetailed.fullDetailedDemo.domain.dtos.Case.CaseRequestDto;
import com.fullDetailed.fullDetailedDemo.domain.dtos.Case.CaseResponseDto;
import com.fullDetailed.fullDetailedDemo.domain.enums.CaseStatus;
import com.fullDetailed.fullDetailedDemo.services.interfaces.admin.AdminCaseManagementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
public class AdminCaseController {

    private final AdminCaseManagementService caseService;

    @PostMapping
    public ResponseEntity<CaseResponseDto> createCase(@Valid @RequestBody CaseRequestDto request) {
        CaseResponseDto createdCase = caseService.createCase(request);
        return new ResponseEntity<>(createdCase, HttpStatus.CREATED);
    }

    @PutMapping("/{caseId}")
    public ResponseEntity<Map<String, String>> updateCase(
            @PathVariable UUID caseId,
            @Valid @RequestBody CaseRequestDto request
    ) {
        caseService.updateCase(caseId, request);
        return ResponseEntity.ok(Map.of("message", "Case updated successfully"));
    }

    @PutMapping("/{caseId}/assign/{judgeId}")
    public ResponseEntity<Map<String, String>> assignJudge(
            @PathVariable UUID caseId,
            @PathVariable UUID judgeId
    ) {
        caseService.assignCaseToJudge(judgeId, caseId);
        return ResponseEntity.ok(Map.of("message", "Judge assigned successfully"));
    }

    @DeleteMapping("/{caseId}")
    public ResponseEntity<Map<String, String>> deleteCase(@PathVariable UUID caseId) {
        caseService.deleteCase(caseId);
        return ResponseEntity.ok(Map.of("message", "Case deleted successfully"));
    }

    @GetMapping
    public ResponseEntity<Page<CaseResponseDto>> getAllCases(Pageable pageable) {
        return ResponseEntity.ok(caseService.getAllCases(pageable));
    }

    @GetMapping("/{caseId}")
    public ResponseEntity<CaseResponseDto> getCaseById(@PathVariable UUID caseId) {
        return ResponseEntity.ok(caseService.getCaseById(caseId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<Page<CaseResponseDto>> getCasesByStatus(
            @PathVariable CaseStatus status,
            Pageable pageable
    ) {
        return ResponseEntity.ok(caseService.getCasesByStatus(status, pageable));
    }

    @PostMapping(value = "/{caseId}/files", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> uploadCaseFiles(
            @PathVariable UUID caseId,
            @RequestParam("files") List<MultipartFile> files
    ) {
        List<String> fileUrls = caseService.uploadCaseFiles(caseId, files);

        return ResponseEntity.ok(Map.of(
                "message", files.size() + " files uploaded successfully",
                "fileUrls", fileUrls
        ));
    }

    @GetMapping("/deleted")
    public ResponseEntity<Page<CaseResponseDto>> getDeletedCases(Pageable pageable) {
        return ResponseEntity.ok(caseService.getAllDeletedCases(pageable));
    }

    @GetMapping("/fully-assigned")
    public ResponseEntity<Page<CaseResponseDto>> getAllFullyAssignedCases(Pageable pageable) {
        return ResponseEntity.ok(caseService.getAllFullyAssignedCases(pageable));
    }
}

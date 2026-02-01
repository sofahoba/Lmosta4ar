package com.fullDetailed.fullDetailedDemo.controller.lawyer;

import com.fullDetailed.fullDetailedDemo.domain.dtos.Case.CaseResponseDto;
import com.fullDetailed.fullDetailedDemo.domain.dtos.Case.RequestCaseDto;
import com.fullDetailed.fullDetailedDemo.domain.dtos.lawyer.LawyerDto;
import com.fullDetailed.fullDetailedDemo.services.interfaces.lawyer.LawyerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @PutMapping("/profile")
    public ResponseEntity<LawyerDto> updateProfile(@RequestBody LawyerDto dto) {
        LawyerDto updatedProfile = lawyerService.updateProfile(dto);
        return ResponseEntity.ok(updatedProfile);
    }

    @GetMapping("/profile")
    public ResponseEntity<LawyerDto>getLawyerProfile(){
        return ResponseEntity.ok(lawyerService.getLawyerProfile());
    }

    @GetMapping("/cases")
    public ResponseEntity<Page<CaseResponseDto>> getAllCases(Pageable pageable) {
        Page<CaseResponseDto> cases = lawyerService.getAllCases(pageable);
        return ResponseEntity.ok(cases);
    }

    @GetMapping("/cases/{caseId}")
    public ResponseEntity<CaseResponseDto> getCaseById(@PathVariable UUID caseId) {
        CaseResponseDto caseDetails = lawyerService.getCaseById(caseId);
        return ResponseEntity.ok(caseDetails);
    }

    @PostMapping("/cases/request-access")
    public ResponseEntity<String> requestAccessToCase(@RequestBody RequestCaseDto requestDto) {
        lawyerService.reqeustAccessOnCaseByCaseNumber(requestDto);
        return ResponseEntity.ok("Request sent successfully to the Administrator.");
    }

    @PostMapping(value = "/cases/{caseId}/files", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<String>> uploadDefenseFiles(
            @PathVariable UUID caseId,
            @RequestParam("files") List<MultipartFile> files
    ) {
        List<String> uploadedUrls = lawyerService.uploadCaseFiles(caseId, files);
        return ResponseEntity.ok(uploadedUrls);
    }
}
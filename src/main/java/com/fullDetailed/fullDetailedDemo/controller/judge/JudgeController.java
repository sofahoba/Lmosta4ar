package com.fullDetailed.fullDetailedDemo.controller.judge;

import com.fullDetailed.fullDetailedDemo.domain.dtos.Case.CaseRequestDto;
import com.fullDetailed.fullDetailedDemo.domain.dtos.Case.CaseResponseDto;
import com.fullDetailed.fullDetailedDemo.domain.dtos.judge.JudgeProfileDto;
import com.fullDetailed.fullDetailedDemo.domain.enums.CaseStatus;
import com.fullDetailed.fullDetailedDemo.services.interfaces.judge.JudgeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/judges")
@RequiredArgsConstructor
public class JudgeController {
    private final JudgeService judgeService;

    @GetMapping("/profile")
    public ResponseEntity<JudgeProfileDto>getJudgeProfile(){
        JudgeProfileDto judgeProfileDto=judgeService.getJudgeProfile();
        return ResponseEntity.ok(judgeProfileDto);
    }

    @GetMapping("/all-cases")
    public ResponseEntity<Map<String, Object>>getJudgeCases(Pageable pageable){
        Page<CaseResponseDto>cases=judgeService.getJudgeCases(pageable);
        Map<String, Object> response = new HashMap<>();
        response.put("cases", cases.getContent());
        response.put("currentPage", cases.getNumber());
        response.put("totalItems", cases.getTotalElements());
        response.put("totalPages", cases.getTotalPages());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/case/{caseId}")
    public ResponseEntity<CaseResponseDto>getCaseById(@PathVariable UUID caseId){
        CaseResponseDto caseResponseDto=judgeService.getCaseById(caseId);
        return ResponseEntity.ok(caseResponseDto);
    }

    @PutMapping("/profile/update")
    public ResponseEntity<JudgeProfileDto>updateJudgeProfile(@RequestBody @Valid JudgeProfileDto dto){
        JudgeProfileDto updatedDto=judgeService.updateJudgeProfile(dto);
        return ResponseEntity.ok(updatedDto);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<Page<CaseResponseDto>> getCasesByStatus(@PathVariable CaseStatus status, Pageable pageable) {
        return ResponseEntity.ok(judgeService.getCasesByStatus(status, pageable));
    }

    @GetMapping("/search-date")
    public ResponseEntity<Page<CaseResponseDto>> getCasesByDateRange(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            Pageable pageable
    ) {
        return ResponseEntity.ok(judgeService.getMyCasesByDateRange(fromDate, toDate, pageable));
    }


    @GetMapping("/cases/recent")
    public ResponseEntity<Page<CaseResponseDto>> getRecentCases(Pageable pageable) {
        Page<CaseResponseDto> cases = judgeService.getAllCasesLast30Days(pageable);
        return ResponseEntity.ok(cases);
    }

    @PatchMapping("/cases/{caseId}/ruling")
    public ResponseEntity<CaseResponseDto> updateCaseRuling(@PathVariable UUID caseId, @RequestBody CaseRequestDto dto) {
        CaseResponseDto updatedCase = judgeService.updateCaseRuling(caseId, dto);
        return ResponseEntity.ok(updatedCase);
    }


}

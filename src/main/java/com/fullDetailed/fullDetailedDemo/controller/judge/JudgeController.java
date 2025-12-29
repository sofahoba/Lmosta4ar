package com.fullDetailed.fullDetailedDemo.controller.judge;

import com.fullDetailed.fullDetailedDemo.domain.dtos.Case.CaseResponseDto;
import com.fullDetailed.fullDetailedDemo.domain.dtos.judge.JudgeProfileDto;
import com.fullDetailed.fullDetailedDemo.services.interfaces.judge.JudgeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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


}

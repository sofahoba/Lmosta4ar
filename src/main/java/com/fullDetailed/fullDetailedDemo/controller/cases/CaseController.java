package com.fullDetailed.fullDetailedDemo.controller.cases;

import com.fullDetailed.fullDetailedDemo.domain.dtos.Case.CaseRequestDto;
import com.fullDetailed.fullDetailedDemo.domain.dtos.Case.CaseResponseDto;
import com.fullDetailed.fullDetailedDemo.services.interfaces.Case.CaseService;
import com.fullDetailed.fullDetailedDemo.util.UserContextService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cases")
@RequiredArgsConstructor
public class CaseController {

    private final CaseService caseService;
    private final UserContextService userContextService;

    @PostMapping
    public ResponseEntity<CaseResponseDto> createCase(@RequestBody CaseRequestDto dto) {
        CaseResponseDto created = caseService.createCase(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CaseResponseDto> updateCase(@PathVariable("id") UUID id,
                                                      @RequestBody CaseRequestDto dto) {
        CaseResponseDto updated = caseService.updateCase(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCase(@PathVariable("id") UUID id) {
        caseService.deleteCase(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<CaseResponseDto>> getAllCases() {
        List<CaseResponseDto> list = caseService.getAllCases();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CaseResponseDto> getCaseById(@PathVariable("id") UUID id) {
        CaseResponseDto response = caseService.getCaseById(id);
        return ResponseEntity.ok(response);
    }

}
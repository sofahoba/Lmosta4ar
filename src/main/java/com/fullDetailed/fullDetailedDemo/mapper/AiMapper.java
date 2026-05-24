package com.fullDetailed.fullDetailedDemo.mapper;

import java.util.List;

import com.fullDetailed.fullDetailedDemo.domain.dtos.Case.CaseFileDto;
import com.fullDetailed.fullDetailedDemo.domain.dtos.Case.CaseResponseDto;
import com.fullDetailed.fullDetailedDemo.domain.dtos.ai.AiInvokeRequest;

public class AiMapper {
  
  public AiInvokeRequest buildAiRequest(CaseResponseDto dto){

    List<String> documents = dto.getCaseFiles()
            .stream()
            .map(CaseFileDto::getFileUrl)
            .toList();

    return AiInvokeRequest.builder()
            .caseId(dto.getCaseNumber())
            .sourceDocuments(documents)
            .build();
}
}

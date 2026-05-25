package com.fullDetailed.fullDetailedDemo.domain.dtos.ai;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiInvokeRequest {

    private String caseId;
    private List<MultipartFile> files;
}
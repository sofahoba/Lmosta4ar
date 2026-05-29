package com.fullDetailed.fullDetailedDemo.domain.dtos.ai;


import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Builder
public class AiInvokeRequest {

    private String caseId;
    private List<MultipartFile> files;
}

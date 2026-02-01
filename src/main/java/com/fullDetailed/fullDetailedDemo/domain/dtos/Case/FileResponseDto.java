package com.fullDetailed.fullDetailedDemo.domain.dtos.Case;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileResponseDto {
    private String fileName;
    private String fileUrl;
    private String fileType;
    private String uploadedBy;
}
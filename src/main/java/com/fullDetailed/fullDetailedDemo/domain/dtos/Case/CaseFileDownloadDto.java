package com.fullDetailed.fullDetailedDemo.domain.dtos.Case;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CaseFileDownloadDto {
    private Resource resource;
    private String contentType;
}

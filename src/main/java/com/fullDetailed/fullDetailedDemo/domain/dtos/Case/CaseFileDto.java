package com.fullDetailed.fullDetailedDemo.domain.dtos.Case;

import com.fullDetailed.fullDetailedDemo.domain.enums.FileType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class CaseFileDto {
    private UUID id;
    private String fileName;
    private String fileUrl;
    private FileType fileType;
    private String uploadedByName;
    private LocalDateTime uploadedAt;
}

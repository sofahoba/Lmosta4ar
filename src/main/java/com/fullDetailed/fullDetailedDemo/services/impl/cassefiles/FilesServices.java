package com.fullDetailed.fullDetailedDemo.services.impl.cassefiles;

import com.fullDetailed.fullDetailedDemo.domain.dtos.Case.CaseFileDownloadDto;
import com.fullDetailed.fullDetailedDemo.services.impl.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FilesServices {
    private final FileStorageService fileStorageService;

    public CaseFileDownloadDto getCaseFile(UUID caseId, String filename) {
        Resource resource = fileStorageService.loadFileAsResource(filename);
        String contentTyp;
        try{
            contentTyp= Files.probeContentType(resource.getFile().toPath());
        }catch (IOException ex){contentTyp="application/octet-stream";}

        if(contentTyp==null){
            contentTyp="application/octet-stream";
        }
        return CaseFileDownloadDto.builder()
                .resource(resource)
                .contentType(contentTyp)
                .build();
    }
}

package com.fullDetailed.fullDetailedDemo.services.impl.lawyer;

import com.fullDetailed.fullDetailedDemo.domain.dtos.ApiResponse;
import com.fullDetailed.fullDetailedDemo.domain.dtos.Case.CaseResponseDto;
import com.fullDetailed.fullDetailedDemo.domain.dtos.Case.RequestCaseDto;
import com.fullDetailed.fullDetailedDemo.domain.dtos.lawyer.LawyerDto;
import com.fullDetailed.fullDetailedDemo.domain.entities.Case;
import com.fullDetailed.fullDetailedDemo.domain.entities.CaseFile;
import com.fullDetailed.fullDetailedDemo.domain.entities.CaseRequests;
import com.fullDetailed.fullDetailedDemo.domain.entities.User;
import com.fullDetailed.fullDetailedDemo.domain.enums.FileType;
import com.fullDetailed.fullDetailedDemo.domain.enums.RequestStatus;
import com.fullDetailed.fullDetailedDemo.domain.event.NotificationEvent;
import com.fullDetailed.fullDetailedDemo.exceptions.DuplicateResourceException;
import com.fullDetailed.fullDetailedDemo.exceptions.NotFoundException;
import com.fullDetailed.fullDetailedDemo.mapper.cases.CaseMapper;
import com.fullDetailed.fullDetailedDemo.mapper.users.lawyer.LawyerMapper;
import com.fullDetailed.fullDetailedDemo.repository.CaseFileRepository;
import com.fullDetailed.fullDetailedDemo.repository.CaseRepository;
import com.fullDetailed.fullDetailedDemo.repository.CaseRequestRepository;
import com.fullDetailed.fullDetailedDemo.repository.UserRepo;
import com.fullDetailed.fullDetailedDemo.services.impl.FileStorageService;
import com.fullDetailed.fullDetailedDemo.services.interfaces.lawyer.LawyerService;
import com.fullDetailed.fullDetailedDemo.util.PagenationHandler;
import com.fullDetailed.fullDetailedDemo.util.UserContextService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LawyerServiceImpl implements LawyerService {

    private final UserContextService contextService;
    private final UserRepo userRepo;
    private final ApplicationEventPublisher eventPublisher;
    private final CaseRepository caseRepository;
    private final CaseRequestRepository reqRepo;
    private final FileStorageService fileStorageService;
    private final CaseFileRepository caseFileRepository;

    @Value("${app.admin.notification-email}")
    private String adminEmail;

    @Override
    public LawyerDto getLawyerProfile() {
        User currLawyer=contextService.getCurrentUser();
        return userRepo.findById(currLawyer.getId()).map(LawyerMapper::toDto).orElseThrow(()->new NotFoundException("not found user"));
    }

    @Override
    @Transactional
    public LawyerDto updateProfile(LawyerDto dto) {
        User currLawyer=contextService.getCurrentUser();
        if(!currLawyer.isActive() || !currLawyer.isApproved() || currLawyer.isDeleted()){
            throw new NotFoundException("Lawyer not found");
        }
        LawyerMapper.updateEntity(currLawyer,dto);
        return LawyerMapper.toDto(currLawyer);
    }

    @Override
    public Page<CaseResponseDto> getAllCases(Pageable pageable) {
        User currLawyer=contextService.getCurrentUser();
        return caseRepository.findByLawyer(currLawyer, PagenationHandler.createCleanPageable(pageable)).map(CaseMapper::toDto);
    }

    @Override
    @Transactional
    public void reqeustAccessOnCaseByCaseNumber(RequestCaseDto requestDto) {

        User currLawyer=contextService.getCurrentUser();
        Case c=caseRepository.findByCaseNumber(requestDto.getCaseNumber()).orElseThrow(()->new NotFoundException("Case Not found"));
        if (c.getLawyer() != null && c.getLawyer().getId().equals(currLawyer.getId())) {
            throw new IllegalArgumentException("You already have access to this case");
        }
        boolean alreadyRequested=reqRepo.existsByLawyerAndLegalCase(currLawyer,c);
        if(alreadyRequested){
            throw new DuplicateResourceException("You have already requested access to this case");        }
        CaseRequests req=CaseRequests.builder()
                .lawyer(currLawyer)
                .legalCase(c)
                .status(RequestStatus.PENDING)
                .build();
        reqRepo.save(req);

        User admin = userRepo.findByEmail(adminEmail)
                .orElseThrow(() -> new NotFoundException("Admin email not found"));

        String notificationMessage = String.format(
                "Lawyer %s %s has requested access to Case Number: %s",
                currLawyer.getFirstName(),
                currLawyer.getLastName(),
                c.getCaseNumber()
        );

        eventPublisher.publishEvent(new NotificationEvent(
                admin.getId(),
                "New Case Access Request",
                notificationMessage
        ));
    }

    @Override
    @Transactional
    public List<String> uploadCaseFiles(UUID caseId, List<MultipartFile> files) {
        Case caseEntity = caseRepository.findById(caseId)
                .orElseThrow(() -> new NotFoundException("Case not found with ID: " + caseId));

        User currLawyer = contextService.getCurrentUser();

        if (caseEntity.getLawyer() == null || !caseEntity.getLawyer().getId().equals(currLawyer.getId())) {
            throw new NotFoundException("Case not found or access denied");
        }

        List<String> fileUrls = new ArrayList<>();
        List<CaseFile> filesToSave = new ArrayList<>();

        for (MultipartFile file : files) {
            if (file.isEmpty()) continue;

            String fileName = fileStorageService.storeFile(file);
            String fileDownloadUrl = "/api/v1/files/download/" + fileName;

            CaseFile caseFile = CaseFile.builder()
                    .caseEntity(caseEntity)
                    .fileName(fileName)
                    .originalFileName(file.getOriginalFilename())
                    .fileUrl(fileDownloadUrl)
                    .fileType(determineFileType(file.getContentType()))
                    .uploadedBy(currLawyer)
                    .build();

            filesToSave.add(caseFile);
            fileUrls.add(fileDownloadUrl);
        }
        if (!filesToSave.isEmpty()) {
            caseFileRepository.saveAll(filesToSave);
        }
        return fileUrls;
    }

    @Override
    @Transactional
    @CacheEvict(value = "cases", allEntries = true)
    public ApiResponse<Void> deleteFile(UUID fileId) {
        CaseFile file = caseFileRepository.findById(fileId)
                .orElseThrow(() -> new NotFoundException("File not found with ID: " + fileId));
        
        Case c = file.getCaseEntity();
        User currUser=contextService.getCurrentUser();
        if(!c.getLawyer().equals(currUser) && !file.getUploadedBy().equals(currUser)){
            throw new NotFoundException("Case not Exist");
        }
        caseFileRepository.delete(file);
        return ApiResponse.success("File deleted successfully");
    }

    @Override
    public CaseResponseDto getCaseById(UUID caseId) {
        Case c = caseRepository.findById(caseId).orElseThrow(()->new NotFoundException("case not found"));
        User currUser=contextService.getCurrentUser();
        if(!c.getLawyer().equals(currUser) || c.getLawyer()==null){
            throw new NotFoundException("the case id u entered doesn't exist");
        }
        return CaseMapper.toDto(c);
    }

    private FileType determineFileType(String contentType) {
        if (contentType == null) return FileType.OTHER;
        if (contentType.contains("pdf")) return FileType.PDF;
        if (contentType.contains("image")) return FileType.IMAGE;
        return FileType.DOCUMENT;
    }
}

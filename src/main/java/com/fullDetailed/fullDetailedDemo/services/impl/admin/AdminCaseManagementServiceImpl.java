package com.fullDetailed.fullDetailedDemo.services.impl.admin;

import com.fullDetailed.fullDetailedDemo.domain.dtos.Case.CaseRequestDto;
import com.fullDetailed.fullDetailedDemo.domain.dtos.Case.CaseResponseDto;
import com.fullDetailed.fullDetailedDemo.domain.entities.Case;
import com.fullDetailed.fullDetailedDemo.domain.entities.CaseFile;
import com.fullDetailed.fullDetailedDemo.domain.entities.User;
import com.fullDetailed.fullDetailedDemo.domain.enums.CaseStatus;
import com.fullDetailed.fullDetailedDemo.domain.enums.FileType;
import com.fullDetailed.fullDetailedDemo.domain.enums.Role;
import com.fullDetailed.fullDetailedDemo.exceptions.DuplicateResourceException;
import com.fullDetailed.fullDetailedDemo.exceptions.NotFoundException;
import com.fullDetailed.fullDetailedDemo.mapper.cases.CaseMapper;
import com.fullDetailed.fullDetailedDemo.repository.CaseFileRepository;
import com.fullDetailed.fullDetailedDemo.repository.CaseRepository;
import com.fullDetailed.fullDetailedDemo.repository.UserRepo;
import com.fullDetailed.fullDetailedDemo.services.impl.FileStorageService;
import com.fullDetailed.fullDetailedDemo.services.interfaces.admin.AdminCaseManagementService;
import com.fullDetailed.fullDetailedDemo.services.interfaces.notification.NotificationService;
import com.fullDetailed.fullDetailedDemo.util.PagenationHandler;
import com.fullDetailed.fullDetailedDemo.util.UserContextService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.job.parameters.JobParametersBuilder;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminCaseManagementServiceImpl implements AdminCaseManagementService {

    private final UserRepo userRepo;
    private final CaseRepository caseRepository;
    private final UserContextService userContextService;
    private final FileStorageService fileStorageService;
    private final CaseFileRepository caseFileRepo;
    private final NotificationService notificationService;
    private final JobOperator jobOperator;
    private final Job importCaseJob;

    @Transactional
    @Override
    @CacheEvict(value = "cases", allEntries = true)
    public void assignCaseToJudge(UUID judgeId, UUID caseId) {
        User judge = userRepo.findById(judgeId).orElseThrow(() -> new NotFoundException("User Not found"));
        if (judge.getRole() != Role.JUDGE) {
            throw new NotFoundException("Please assign only judges to this case");
        }

        Case caseEntity = caseRepository.findById(caseId).orElseThrow(() -> new NotFoundException("Case Not found"));
        User currentUser = userContextService.getCurrentUser();
        if(caseEntity.getJudge()!=null){
            throw new IllegalArgumentException("the case is already assigned to an existing judge");
        }
        caseEntity.setJudge(judge);
        caseEntity.setAssignedBy(currentUser);
        judge.setAssignedCasesCount(judge.getAssignedCasesCount() + 1);

        notificationService.createAndSend(
                judge,
                "New Case Assigned",
                "You have been assigned to case number: " + caseEntity.getCaseNumber() + "\nCase ID: " + caseEntity.getId()
        );

    }

    @Override
    @Transactional
    @CacheEvict(value = "cases", allEntries = true)
    public CaseResponseDto createCase(CaseRequestDto request) {
        if (caseRepository.existsByCaseNumber(request.getCaseNumber())) {
            throw new DuplicateResourceException("Case with number " + request.getCaseNumber() + " already exists");
        }

        User adminUser = userContextService.getCurrentUser();

        User judgeUser = null;
        if (request.getJudgeId() != null) {
            judgeUser = userRepo.findById(request.getJudgeId())
                    .orElseThrow(() -> new NotFoundException("Judge not found with ID: " + request.getJudgeId()));

            if (judgeUser.getRole() != Role.JUDGE) {
                throw new IllegalArgumentException("User with ID " + request.getJudgeId() + " is not a Judge");
            }
        }

        User lawyerUser = null;
        if (request.getLawyerId() != null) {
            lawyerUser = userRepo.findById(request.getLawyerId())
                    .orElseThrow(() -> new NotFoundException("lawyer not found with ID: " + request.getLawyerId()));

            if (lawyerUser.getRole() != Role.LAWYER) {
                throw new IllegalArgumentException("User with ID " + request.getLawyerId() + " is not a Lawyer");
            }
        }

        Case caseEntity = CaseMapper.toEntity(request, judgeUser, adminUser,lawyerUser);

        caseEntity.setStatus(CaseStatus.PENDING);
        caseEntity.setDeleted(false);

        Case savedCase = caseRepository.save(caseEntity);

        if (savedCase.getJudge() != null) {
            notificationService.createAndSend(
                    judgeUser,
                    "New Case Assigned",
                    "You have been assigned to case number: " + caseEntity.getCaseNumber() + "\nCase ID: " + caseEntity.getId()
            );
            judgeUser.setAssignedCasesCount(judgeUser.getAssignedCasesCount() + 1);
            userRepo.save(judgeUser);
        }

        return CaseMapper.toDto(savedCase);
    }

    @Override
    @Transactional
    @CacheEvict(value = "cases", allEntries = true)
    public void updateCase(UUID caseId, CaseRequestDto request) {
        Case existingCase = caseRepository.findById(caseId)
                .orElseThrow(() -> new NotFoundException("Case not found with ID: " + caseId));

        if (existingCase.isDeleted()) {
            throw new NotFoundException("Case not found (deleted)");
        }

        if (request.getCaseNumber() != null &&
                !request.getCaseNumber().equals(existingCase.getCaseNumber())) {

            if (caseRepository.existsByCaseNumber(request.getCaseNumber())) {
                throw new DuplicateResourceException("Case number " + request.getCaseNumber() + " already exists");
            }
        }

        User newJudge = null;
        if (request.getJudgeId() != null) {
            newJudge = userRepo.findById(request.getJudgeId())
                    .orElseThrow(() -> new NotFoundException("Judge not found with ID: " + request.getJudgeId()));

            if (newJudge.getRole() != Role.JUDGE) {
                throw new IllegalArgumentException("User is not a Judge");
            }
        }

        User lawyerUser = null;
        if (request.getLawyerId() != null) {
            lawyerUser = userRepo.findById(request.getLawyerId())
                    .orElseThrow(() -> new NotFoundException("lawyer not found with ID: " + request.getLawyerId()));

            if (lawyerUser.getRole() != Role.LAWYER) {
                throw new IllegalArgumentException("User with ID " + request.getLawyerId() + " is not a Lawyer");
            }
        }


        CaseMapper.updateEntity(existingCase, request, newJudge, lawyerUser);

    }

    @Override
    @Transactional
    @CacheEvict(value = "cases", allEntries = true)
    public void deleteCase(UUID caseId) {
        Case caseEntity = caseRepository.findById(caseId).orElseThrow(()->new NotFoundException("Case Not Found"));
        caseEntity.setDeleted(true);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "cases", key = "'all:'+#pageable.pageNumber+':'+ #pageable.pageSize")
    public Page<CaseResponseDto> getAllCases(Pageable pageable) {
        return caseRepository.findByIsDeletedFalse(PagenationHandler.createCleanPageable(pageable)).map(CaseMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "cases", key = "'id:' + #caseId")
    public CaseResponseDto getCaseById(UUID caseId) {
        Case caseEntity=caseRepository.findById(caseId).orElseThrow(()->new NotFoundException("Case Nott Found"));
        return CaseMapper.toDto(caseEntity);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(
            value = "cases",
            key = "'status:' + #status + ':' + #pageable.pageNumber + ':' + #pageable.pageSize"
    )
    public Page<CaseResponseDto> getCasesByStatus(CaseStatus status, Pageable pageable) {
        return caseRepository.findByStatusAndIsDeletedFalse(status,PagenationHandler.createCleanPageable(pageable)).map(CaseMapper::toDto);
    }

    @Override
    @Transactional
    @CacheEvict(value = "cases", allEntries = true)
    public List<String> uploadCaseFiles(UUID caseId, List<MultipartFile> files) {
        Case caseEntity = caseRepository.findById(caseId)
                .orElseThrow(() -> new NotFoundException("Case not found with ID: " + caseId));

        User adminUser = userContextService.getCurrentUser();

        List<String> fileUrls = new ArrayList<>();

        for (MultipartFile file : files) {
            if (file.isEmpty()) continue;

            String fileName = fileStorageService.storeFile(file);

            String fileDownloadUrl = String.format("/api/v1/admin/cases/%s/files/%s",
                    caseId.toString(),
                    fileName);

            CaseFile caseFile = CaseFile.builder()
                    .caseEntity(caseEntity)
                    .fileName(fileName)
                    .fileUrl(fileDownloadUrl)
                    .fileType(determineFileType(file.getContentType()))
                    .uploadedBy(adminUser)
                    .build();

            caseFileRepo.save(caseFile);
            fileUrls.add(fileDownloadUrl);
        }

        return fileUrls;
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "cases", key = "'deleted:' + #pageable.pageNumber")
    public Page<CaseResponseDto> getAllDeletedCases(Pageable pageable) {
        return caseRepository.findByIsDeletedTrue(PagenationHandler.createCleanPageable(pageable)).map(CaseMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "cases", key = "'fullyAssigned:' + #pageable.pageNumber")
    public Page<CaseResponseDto> getAllFullyAssignedCases(Pageable pageable) {
        return caseRepository.findByJudgeIsNotNullAndLawyerIsNotNullAndIsDeletedFalse(
                PagenationHandler.createCleanPageable(pageable)
        ).map(CaseMapper::toDto);
    }

    @Override
    @CacheEvict(value = "cases", allEntries = true)
    public void importCasesFromCsv(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new IllegalArgumentException("File is empty");
            }

            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || !originalFilename.toLowerCase().endsWith(".csv")) {
                throw new IllegalArgumentException("Please upload a valid csv file");
            }

            Path uploadDir = Path.of("/app/uploads/case-files");
            Files.createDirectories(uploadDir);

            Path tempFile = uploadDir.resolve("import-" + System.currentTimeMillis() + ".csv");
            file.transferTo(tempFile);

            log.info("File saved to: {}", tempFile.toAbsolutePath());

            JobParameters params = new JobParametersBuilder()
                    .addLong("startAt", System.currentTimeMillis())
                    .addString("filePath", tempFile.toAbsolutePath().toString())
                    .toJobParameters();

            jobOperator.start(importCaseJob, params);

            log.info("Batch job completed");

        } catch (Exception e) {
            log.error("Failed to import cases from CSV", e);
            throw new IllegalArgumentException("Failed to import cases: " + e.getMessage(), e);
        }
    }

    private FileType determineFileType(String contentType) {
        if (contentType == null) return FileType.OTHER;
        if (contentType.contains("pdf")) return FileType.PDF;
        if (contentType.contains("image")) return FileType.IMAGE;
        return FileType.DOCUMENT;
    }

}

package com.fullDetailed.fullDetailedDemo.services.impl.admin;

import com.fullDetailed.fullDetailedDemo.domain.dtos.Case.CaseRequestResponseDto;
import com.fullDetailed.fullDetailedDemo.domain.dtos.UserProfileResponseDto;
import com.fullDetailed.fullDetailedDemo.domain.dtos.judge.CreateUserDto;
import com.fullDetailed.fullDetailedDemo.domain.dtos.judge.JudgeProfileDto;
import com.fullDetailed.fullDetailedDemo.domain.dtos.judge.UserResponseDto;
import com.fullDetailed.fullDetailedDemo.domain.dtos.lawyer.LawyerDto;
import com.fullDetailed.fullDetailedDemo.domain.entities.Case;
import com.fullDetailed.fullDetailedDemo.domain.entities.CaseRequests;
import com.fullDetailed.fullDetailedDemo.domain.entities.User;
import com.fullDetailed.fullDetailedDemo.domain.enums.RequestStatus;
import com.fullDetailed.fullDetailedDemo.domain.enums.Role;
import com.fullDetailed.fullDetailedDemo.exceptions.DuplicateResourceException;
import com.fullDetailed.fullDetailedDemo.exceptions.NotFoundException;
import com.fullDetailed.fullDetailedDemo.mapper.UserMapper;
import com.fullDetailed.fullDetailedDemo.mapper.users.judge.JudgeMapper;
import com.fullDetailed.fullDetailedDemo.mapper.users.lawyer.LawyerMapper;
import com.fullDetailed.fullDetailedDemo.repository.CaseRepository;
import com.fullDetailed.fullDetailedDemo.repository.CaseRequestRepository;
import com.fullDetailed.fullDetailedDemo.repository.UserRepo;
import com.fullDetailed.fullDetailedDemo.services.interfaces.admin.AdminUserManagementService;
import com.fullDetailed.fullDetailedDemo.services.interfaces.notification.NotificationService;
import com.fullDetailed.fullDetailedDemo.util.HelperDtoConverter;
import com.fullDetailed.fullDetailedDemo.util.PagenationHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminUserManagementServiceImpl implements AdminUserManagementService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final CaseRequestRepository caseRequestRepository;
    private final CaseRepository caseRepository;
    private final NotificationService notificationService;


    @Override
    @Caching(evict = {
            @CacheEvict(value = "user_profile", key = "#userId"),
            @CacheEvict(value = "users_list", allEntries = true)
    })
    public void acceptLawyerApprovalById(UUID userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));

        if (user.getRole() != Role.LAWYER) {
            throw new IllegalArgumentException("User is not a lawyer");
        }

        if (user.isDeleted()) {
            throw new NotFoundException("User not found with id: " + userId);
        }

        user.setApproved(true);
        userRepo.save(user);
        notificationService.createAndSend(
                user,
                "Account Approved",
                "Congratulations! Your lawyer account has been approved by the administration. You can now access the system."
        );
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "user_profile", key = "#userId"),
            @CacheEvict(value = "users_list", allEntries = true)
    })
    public void rejectLawyerApprovalById(UUID userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));

        if (user.getRole() != Role.LAWYER) {
            throw new IllegalArgumentException("User is not a lawyer");
        }

        if (user.isDeleted()) {
            throw new NotFoundException("User not found with id: " + userId);
        }

        user.setApproved(false);
        userRepo.save(user);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "user_profile", key = "#userId"),
            @CacheEvict(value = "users_list", allEntries = true)
    })
    public void deActivateUserById(UUID userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        user.setActive(false);
        userRepo.save(user);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "user_profile", key = "#userId"),
            @CacheEvict(value = "users_list", allEntries = true)
    })
    public void activateUserById(UUID userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setActive(true);
        userRepo.save(user);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "user_profile", key = "#userId"),
            @CacheEvict(value = "users_list", allEntries = true)
    })
    public void deleteUserById(UUID userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        user.setDeleted(true);
        userRepo.save(user);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "user_profile", key = "#judgeId"),
            @CacheEvict(value = "users_list", allEntries = true)
    })
    public void updateJudgeProfile(UUID judgeId, JudgeProfileDto judgeProfileDto) {
        User user = userRepo.findById(judgeId)
                .orElseThrow(() -> new NotFoundException("Judge not found"));
        if(user.isDeleted()){
            throw new NotFoundException("Judge not found");
        }
        JudgeMapper.updateEntity(user, judgeProfileDto);
        userRepo.save(user);

        notificationService.createAndSend(
                user,
                "Profile Updated",
                "Your judge profile details have been updated by the administrator."
        );
    }

    /*
        kol L gets L me7tagenha ll Admin

        - Kol L judges
        - kol L active judges
        - kol L deactivated judges
     */

    @Override
    @Cacheable(value = "users_list", key = "{#root.methodName, #pageable}")
    public Page<JudgeProfileDto> getAllJudgesProfile(Pageable pageable) {
        return userRepo.findByRoleAndIsDeletedFalse(Role.JUDGE, PagenationHandler.createCleanPageable(pageable))
                .map(JudgeMapper::toDto);
    }

    @Override
    @Cacheable(value = "users_list", key = "{#root.methodName, #pageable}")
    public Page<LawyerDto> getAllLawyerProfile(Pageable pageable) {
        return userRepo.findByRoleAndIsDeletedFalse(Role.LAWYER, PagenationHandler.createCleanPageable(pageable))
                .map(LawyerMapper::toDto);
    }

    @Override
    @Cacheable(value = "users_list", key = "{#root.methodName, #pageable}")
    public Page<JudgeProfileDto> getAllDeactivatedJudges(Pageable pageable) {
        return userRepo.findByRoleAndIsActiveFalseAndIsDeletedFalse(Role.JUDGE, PagenationHandler.createCleanPageable(pageable))
                .map(JudgeMapper::toDto);
    }

    @Override
    @Cacheable(value = "users_list", key = "{#root.methodName, #pageable}")
    public Page<LawyerDto> getAllDeactivatedLawyers(Pageable pageable) {
        return userRepo.findByRoleAndIsActiveFalseAndIsDeletedFalse(Role.LAWYER, PagenationHandler.createCleanPageable(pageable))
                .map(LawyerMapper::toDto);
    }

    @Override
    @Cacheable(value = "users_list", key = "{#root.methodName, #pageable}")
    public Page<JudgeProfileDto> getAllActivatedJudges(Pageable pageable) {
        return userRepo.findByRoleAndIsActiveTrueAndIsDeletedFalse(Role.JUDGE, PagenationHandler.createCleanPageable(pageable))
                .map(JudgeMapper::toDto);
    }

    @Override
    @Cacheable(value = "users_list", key = "{#root.methodName, #pageable}")
    public Page<LawyerDto> getAllActivatedLawyers(Pageable pageable) {
        return userRepo.findByRoleAndIsActiveTrueAndIsDeletedFalse(Role.LAWYER, PagenationHandler.createCleanPageable(pageable))
                .map(LawyerMapper::toDto);
    }



    @Override
    @CacheEvict(value = "users_list", allEntries = true)
    public UserResponseDto createUser(CreateUserDto createUserDto) {
        if (userRepo.existsByEmail(createUserDto.getEmail())) {
            throw new DuplicateResourceException("User with email " + createUserDto.getEmail() + " already exists");
        }

        if (createUserDto.getRole() == Role.JUDGE &&
                (createUserDto.getCourt() == null || createUserDto.getCourt().isBlank())) {
            throw new IllegalArgumentException("Court is required for judges");
        }

        User user = User.builder()
                .firstName(createUserDto.getFirstName())
                .lastName(createUserDto.getLastName())
                .email(createUserDto.getEmail())
                .password(passwordEncoder.encode(createUserDto.getPassword()))
                .age(createUserDto.getAge())
                .role(createUserDto.getRole())
                .court(createUserDto.getCourt())
                .nationalId(createUserDto.getNationalId())
                .isActive(true)
                .isDeleted(false)
                .isPasswordReseted(false)
                .otpCode("")
                .assignedCasesCount(0)
                .build();

        User savedUser = userRepo.save(user);
        return HelperDtoConverter.mapToUserResponseDto(savedUser);
    }

    @Override
    @Cacheable(value = "users_list", key = "{#root.methodName, #pageable}")
    public Page<LawyerDto> getAllApprovedLawyers(Pageable pageable) {
        return userRepo.findByRoleAndIsApprovedTrueAndIsDeletedFalse(Role.LAWYER, PagenationHandler.createCleanPageable(pageable))
                .map(LawyerMapper::toDto);
    }

    @Override
    @Cacheable(value = "users_list", key = "{#root.methodName, #pageable}")
    public Page<LawyerDto> getAllRejectedLawyers(Pageable pageable) {
        return userRepo.findByRoleAndIsApprovedFalseAndIsDeletedFalse(Role.LAWYER, PagenationHandler.createCleanPageable(pageable))
                .map(LawyerMapper::toDto);
    }

    @Override
    @Cacheable(value = "user_profile", key = "#userId")
    public UserProfileResponseDto getUserById(UUID userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (user.isDeleted()) {
            throw new NotFoundException("User not found");
        }

        return UserMapper.toDto(user);
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "case_requests", allEntries = true),
            @CacheEvict(value = "users_list", allEntries = true),
            @CacheEvict(value = "user_profile", allEntries = true)
    })
    public void approveCaseAccessRequest(UUID requestId) {
        CaseRequests request = caseRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Request not found with id: " + requestId));

        request.setStatus(RequestStatus.APPROVED);
        caseRequestRepository.save(request);


        Case legalCase = request.getLegalCase();

        if(legalCase.getLawyer()!=null){
            throw new IllegalArgumentException("the case already has lawyer");
        }
        legalCase.setLawyer(request.getLawyer());

        User lawyer = request.getLawyer();
        lawyer.setAssignedCasesCount(lawyer.getAssignedCasesCount() + 1);
        userRepo.save(lawyer);

        caseRepository.save(legalCase);

        String caseInfo = (legalCase.getCaseNumber() != null) ? legalCase.getCaseNumber() : "the requested case";

        notificationService.createAndSend(
                lawyer,
                "Case Access Granted",
                "Your request to access case '" + caseInfo + "' has been APPROVED. You are now assigned to this case."
        );
    }

    @Override
    @CacheEvict(value = "case_requests", allEntries = true)
    public void rejectCaseAccessRequest(UUID requestId) {
        CaseRequests request = caseRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Request not found with id: " + requestId));

        request.setStatus(RequestStatus.REJECTED);
        caseRequestRepository.save(request);

        String caseInfo = (request.getLegalCase().getCaseNumber() != null)
                ? request.getLegalCase().getCaseNumber()
                : "the requested case";

        notificationService.createAndSend(
                request.getLawyer(),
                "Case Access Denied",
                "Your request to access case '" + caseInfo + "' has been REJECTED."
        );
    }

    @Override
    @Cacheable(value = "case_requests", key = "{#status, #pageable}")
    public Page<CaseRequestResponseDto> getAllCaseRequestsByStatus(RequestStatus status,Pageable pageable) {
        return caseRequestRepository.findByStatus(status, PagenationHandler.createCleanPageable(pageable))
                .map(HelperDtoConverter::mapToCaseRequestDto);
    }


}

package com.fullDetailed.fullDetailedDemo.services.impl.admin;

import com.fullDetailed.fullDetailedDemo.domain.dtos.UserProfileResponseDto;
import com.fullDetailed.fullDetailedDemo.domain.dtos.judge.CreateUserDto;
import com.fullDetailed.fullDetailedDemo.domain.dtos.judge.JudgeProfileDto;
import com.fullDetailed.fullDetailedDemo.domain.dtos.judge.UserResponseDto;
import com.fullDetailed.fullDetailedDemo.domain.dtos.lawyer.LawyerDto;
import com.fullDetailed.fullDetailedDemo.domain.entities.User;
import com.fullDetailed.fullDetailedDemo.domain.enums.Role;
import com.fullDetailed.fullDetailedDemo.exceptions.DuplicateResourceException;
import com.fullDetailed.fullDetailedDemo.exceptions.NotFoundException;
import com.fullDetailed.fullDetailedDemo.mapper.UserMapper;
import com.fullDetailed.fullDetailedDemo.mapper.users.judge.JudgeMapper;
import com.fullDetailed.fullDetailedDemo.mapper.users.lawyer.LawyerMapper;
import com.fullDetailed.fullDetailedDemo.repository.UserRepo;
import com.fullDetailed.fullDetailedDemo.services.interfaces.admin.AdminUserManagementService;
import com.fullDetailed.fullDetailedDemo.util.PagenationHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminUserManagementServiceImpl implements AdminUserManagementService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;


    @Override
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
    }

    @Override
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
    public void deActivateUserById(UUID userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        user.setActive(false);
        userRepo.save(user);
    }

    @Override
    public void activateUserById(UUID userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setActive(true);
        userRepo.save(user);
    }

    @Override
    public void deleteUserById(UUID userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        user.setDeleted(true);
        userRepo.save(user);
    }

    @Override
    public void updateJudgeProfile(UUID judgeId, JudgeProfileDto judgeProfileDto) {
        User user = userRepo.findById(judgeId)
                .orElseThrow(() -> new NotFoundException("Judge not found"));
        if(user.isDeleted()){
            throw new NotFoundException("Judge not found");
        }
        JudgeMapper.updateEntity(user, judgeProfileDto);
        userRepo.save(user);
    }

    /*
        kol L gets L me7tagenha ll Admin

        - Kol L judges
        - kol L active judges
        - kol L deactivated judges
     */

    @Override
    public Page<JudgeProfileDto> getAllJudgesProfile(Pageable pageable) {
        return userRepo.findByRoleAndIsDeletedFalse(Role.JUDGE, PagenationHandler.createCleanPageable(pageable))
                .map(JudgeMapper::toDto);
    }

    @Override
    public Page<LawyerDto> getAllLawyerProfile(Pageable pageable) {
        return userRepo.findByRoleAndIsDeletedFalse(Role.LAWYER, PagenationHandler.createCleanPageable(pageable))
                .map(LawyerMapper::toDto);
    }

    @Override
    public Page<JudgeProfileDto> getAllDeactivatedJudges(Pageable pageable) {
        return userRepo.findByRoleAndIsActiveFalseAndIsDeletedFalse(Role.JUDGE, PagenationHandler.createCleanPageable(pageable))
                .map(JudgeMapper::toDto);
    }

    @Override
    public Page<LawyerDto> getAllDeactivatedLawyers(Pageable pageable) {
        return userRepo.findByRoleAndIsActiveFalseAndIsDeletedFalse(Role.LAWYER, PagenationHandler.createCleanPageable(pageable))
                .map(LawyerMapper::toDto);
    }

    @Override
    public Page<JudgeProfileDto> getAllActivatedJudges(Pageable pageable) {
        return userRepo.findByRoleAndIsActiveTrueAndIsDeletedFalse(Role.JUDGE, PagenationHandler.createCleanPageable(pageable))
                .map(JudgeMapper::toDto);
    }

    @Override
    public Page<LawyerDto> getAllActivatedLawyers(Pageable pageable) {
        return userRepo.findByRoleAndIsActiveTrueAndIsDeletedFalse(Role.LAWYER, PagenationHandler.createCleanPageable(pageable))
                .map(LawyerMapper::toDto);
    }



    @Override
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
                .isActive(true)
                .isDeleted(false)
                .isPasswordReseted(false)
                .otpCode("")
                .assignedCasesCount(0)
                .build();

        User savedUser = userRepo.save(user);
        return mapToUserResponseDto(savedUser);
    }

    @Override
    public Page<LawyerDto> getAllApprovedLawyers(Pageable pageable) {
        return userRepo.findByRoleAndIsApprovedTrueAndIsDeletedFalse(Role.LAWYER, PagenationHandler.createCleanPageable(pageable))
                .map(LawyerMapper::toDto);
    }

    @Override
    public Page<LawyerDto> getAllRejectedLawyers(Pageable pageable) {
        return userRepo.findByRoleAndIsApprovedFalseAndIsDeletedFalse(Role.LAWYER, PagenationHandler.createCleanPageable(pageable))
                .map(LawyerMapper::toDto);
    }

    @Override
    public UserProfileResponseDto getUserById(UUID userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (user.isDeleted()) {
            throw new NotFoundException("User not found");
        }

        return UserMapper.toDto(user);
    }

    private UserResponseDto mapToUserResponseDto(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .age(user.getAge())
                .role(user.getRole())
                .court(user.getCourt())
                .isActive(user.isActive())
                .createdAt(user.getCreatedAt())
                .build();
    }

}

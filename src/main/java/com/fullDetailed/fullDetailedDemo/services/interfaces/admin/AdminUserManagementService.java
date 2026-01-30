package com.fullDetailed.fullDetailedDemo.services.interfaces.admin;

import com.fullDetailed.fullDetailedDemo.domain.dtos.UserProfileResponseDto;
import com.fullDetailed.fullDetailedDemo.domain.dtos.judge.CreateUserDto;
import com.fullDetailed.fullDetailedDemo.domain.dtos.judge.JudgeProfileDto;
import com.fullDetailed.fullDetailedDemo.domain.dtos.judge.UserResponseDto;
import com.fullDetailed.fullDetailedDemo.domain.dtos.lawyer.LawyerDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface AdminUserManagementService {

    void acceptLawyerApprovalById(UUID userId);

    void rejectLawyerApprovalById(UUID userId);

    void deActivateUserById(UUID userId);

    void deleteUserById(UUID userId);

    void updateJudgeProfile(UUID judgeId, JudgeProfileDto judgeProfileDto);

    Page<JudgeProfileDto> getAllJudgesProfile(Pageable pageable);

    Page<LawyerDto> getAllLawyerProfile(Pageable pageable);

    void activateUserById(UUID userId);

    Page<JudgeProfileDto>getAllDeactivatedJudges(Pageable pageable);

    Page<LawyerDto>getAllDeactivatedLawyers(Pageable pageable);

    Page<JudgeProfileDto>getAllActivatedJudges(Pageable pageable);

    Page<LawyerDto>getAllActivatedLawyers(Pageable pageable);


    UserResponseDto createUser(CreateUserDto createUserDto);

    Page<LawyerDto> getAllApprovedLawyers(Pageable pageable);

    Page<LawyerDto> getAllRejectedLawyers(Pageable pageable);

    UserProfileResponseDto getUserById(UUID userId);

}

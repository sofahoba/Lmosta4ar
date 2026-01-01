package com.fullDetailed.fullDetailedDemo.services.interfaces.admin;

import com.fullDetailed.fullDetailedDemo.domain.dtos.judge.JudgeProfileDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface AdminService {

    void deActivateUserById(UUID userId);
    void deleteUserById(UUID userId);
    void updateJudgeProfile(UUID judgeId, JudgeProfileDto judgeProfileDto);
    Page<JudgeProfileDto> getAllJudgesProfile(Pageable pageable);
    void activateUserById(UUID userId);
    Page<JudgeProfileDto>getAllDeactivatedJudges(Pageable pageable);
    Page<JudgeProfileDto>getAllActivatedJudges(Pageable pageable);


}

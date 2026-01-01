package com.fullDetailed.fullDetailedDemo.services.impl.admin;

import com.fullDetailed.fullDetailedDemo.domain.dtos.judge.JudgeProfileDto;
import com.fullDetailed.fullDetailedDemo.domain.entities.User;
import com.fullDetailed.fullDetailedDemo.domain.enums.Role;
import com.fullDetailed.fullDetailedDemo.exceptions.NotFoundException;
import com.fullDetailed.fullDetailedDemo.mapper.users.judge.JudgeMapper;
import com.fullDetailed.fullDetailedDemo.repository.UserRepo;
import com.fullDetailed.fullDetailedDemo.services.interfaces.admin.AdminService;
import com.fullDetailed.fullDetailedDemo.util.UserContextService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepo userRepo;

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
        return userRepo.findByRoleAndIsDeletedFalse(Role.JUDGE, pageable)
                .map(JudgeMapper::toDto);
    }

    @Override
    public Page<JudgeProfileDto> getAllDeactivatedJudges(Pageable pageable) {//
        return userRepo.findByRoleAndIsActiveFalseAndIsDeletedFalse(Role.JUDGE, pageable)
                .map(JudgeMapper::toDto);
    }

    @Override
    public Page<JudgeProfileDto> getAllActivatedJudges(Pageable pageable) {
        return userRepo.findByRoleAndIsActiveTrueAndIsDeletedFalse(Role.JUDGE, pageable)
                .map(JudgeMapper::toDto);
    }

}

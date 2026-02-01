package com.fullDetailed.fullDetailedDemo.controller.admin;

import com.fullDetailed.fullDetailedDemo.domain.dtos.Case.CaseRequestResponseDto;
import com.fullDetailed.fullDetailedDemo.domain.dtos.UserProfileResponseDto;
import com.fullDetailed.fullDetailedDemo.domain.dtos.judge.CreateUserDto;
import com.fullDetailed.fullDetailedDemo.domain.dtos.judge.JudgeProfileDto;
import com.fullDetailed.fullDetailedDemo.domain.dtos.judge.UserResponseDto;
import com.fullDetailed.fullDetailedDemo.domain.dtos.lawyer.LawyerDto;
import com.fullDetailed.fullDetailedDemo.domain.enums.RequestStatus;
import com.fullDetailed.fullDetailedDemo.services.interfaces.admin.AdminUserManagementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/users")
@RequiredArgsConstructor
public class AdminUserManagementController {

    private final AdminUserManagementService adminService;

    @PutMapping("/{userId}/deactivate")
    public ResponseEntity<Map<String, String>> deactivateUser(@PathVariable UUID userId) {
        adminService.deActivateUserById(userId);
        return ResponseEntity.ok(Map.of(
                "message", "User deactivated successfully"
        ));
    }
    @GetMapping("/lawyers/active")
    public ResponseEntity<Page<LawyerDto>> getAllActiveLawyers(Pageable pageable) {
        return ResponseEntity.ok(adminService.getAllActivatedLawyers(pageable));
    }

    @GetMapping("/lawyers/deactivated")
    public ResponseEntity<Page<LawyerDto>> getAllDeactivatedLawyers(Pageable pageable) {
        return ResponseEntity.ok(adminService.getAllDeactivatedLawyers(pageable));
    }

    @GetMapping("/lawyers")
    public ResponseEntity<Page<LawyerDto>> getAllLawyers(Pageable pageable) {
        return ResponseEntity.ok(adminService.getAllLawyerProfile(pageable));
    }

    @PutMapping("/lawyers/{lawyerId}/approve")
    public ResponseEntity<String> approveLawyer(@PathVariable UUID lawyerId) {
        adminService.acceptLawyerApprovalById(lawyerId);
        return ResponseEntity.ok("Lawyer approved successfully");
    }

    @PutMapping("/lawyers/{lawyerId}/reject")
    public ResponseEntity<String> rejectLawyer(@PathVariable UUID lawyerId) {
        adminService.rejectLawyerApprovalById(lawyerId);
        return ResponseEntity.ok("Lawyer rejected successfully");
    }

    @GetMapping("/lawyers/approved")
    public ResponseEntity<Page<LawyerDto>> getAllApprovedLawyers(Pageable pageable) {
        Page<LawyerDto> approvedLawyers = adminService.getAllApprovedLawyers(pageable);
        return ResponseEntity.ok(approvedLawyers);
    }

    @GetMapping("/lawyers/rejected")
    public ResponseEntity<Page<LawyerDto>> getAllRejectedLawyers(Pageable pageable) {
        Page<LawyerDto> rejectedLawyers = adminService.getAllRejectedLawyers(pageable);
        return ResponseEntity.ok(rejectedLawyers);
    }

    @PostMapping("/users")
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody CreateUserDto createUserDto) {
        UserResponseDto createdUser = adminService.createUser(createUserDto);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserProfileResponseDto> getUserById(@PathVariable UUID userId) {
        UserProfileResponseDto user = adminService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{userId}/activate")
    public ResponseEntity<Map<String, String>> activateUser(@PathVariable UUID userId) {
        adminService.activateUserById(userId);
        return ResponseEntity.ok(Map.of(
                "message", "User activated successfully"
        ));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable UUID userId) {
        adminService.deleteUserById(userId);
        return ResponseEntity.ok(Map.of(
                "message", "User deleted successfully"
        ));
    }

    @GetMapping("/judges")
    public ResponseEntity<Page<JudgeProfileDto>> getAllJudges(Pageable pageable) {
        return ResponseEntity.ok(adminService.getAllJudgesProfile(pageable));
    }

    @GetMapping("/judges/active")
    public ResponseEntity<Page<JudgeProfileDto>> getAllActiveJudges(Pageable pageable) {
        return ResponseEntity.ok(adminService.getAllActivatedJudges(pageable));
    }

    @GetMapping("/judges/deactivated")
    public ResponseEntity<Page<JudgeProfileDto>> getAllDeactivatedJudges(Pageable pageable) {
        return ResponseEntity.ok(adminService.getAllDeactivatedJudges(pageable));
    }

    @PutMapping("/judges/{judgeId}")
    public ResponseEntity<Map<String, String>> updateJudgeProfile(
            @PathVariable UUID judgeId,
            @RequestBody JudgeProfileDto judgeProfileDto
    ) {
        adminService.updateJudgeProfile(judgeId, judgeProfileDto);
        return ResponseEntity.ok(Map.of(
                "message", "Judge profile updated successfully"
        ));
    }

    @PutMapping("lawyer-access/{requestId}/approve")
    public ResponseEntity<String> approveRequest(@PathVariable UUID requestId) {
        adminService.approveCaseAccessRequest(requestId);
        return ResponseEntity.ok("Request approved successfully. Lawyer assigned to case.");
    }

    @PutMapping("lawyer-access/{requestId}/reject")
    public ResponseEntity<String> rejectRequest(@PathVariable UUID requestId) {
        adminService.rejectCaseAccessRequest(requestId);
        return ResponseEntity.ok("Request rejected successfully.");
    }

    @GetMapping("/lawyer-access/status")
    public ResponseEntity<Page<CaseRequestResponseDto>> getAllCaseRequestsByStatus(@RequestParam RequestStatus status, Pageable pageable) {
        return ResponseEntity.ok(adminService.getAllCaseRequestsByStatus(status,pageable));
    }

}

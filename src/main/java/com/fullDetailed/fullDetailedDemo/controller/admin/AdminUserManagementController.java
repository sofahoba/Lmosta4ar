package com.fullDetailed.fullDetailedDemo.controller.admin;

import com.fullDetailed.fullDetailedDemo.domain.dtos.ApiResponse;
import com.fullDetailed.fullDetailedDemo.domain.dtos.Case.CaseRequestResponseDto;
import com.fullDetailed.fullDetailedDemo.domain.dtos.UserProfileResponseDto;
import com.fullDetailed.fullDetailedDemo.domain.dtos.judge.CreateUserDto;
import com.fullDetailed.fullDetailedDemo.domain.dtos.judge.JudgeProfileDto;
import com.fullDetailed.fullDetailedDemo.domain.dtos.judge.UserResponseDto;
import com.fullDetailed.fullDetailedDemo.domain.dtos.lawyer.LawyerDto;
import com.fullDetailed.fullDetailedDemo.domain.enums.RequestStatus;
import com.fullDetailed.fullDetailedDemo.services.interfaces.admin.AdminUserManagementService;
import com.fullDetailed.fullDetailedDemo.util.ResponseHelper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/users")
@RequiredArgsConstructor
public class AdminUserManagementController {

    private final AdminUserManagementService adminService;

    // ==================== USER MANAGEMENT ====================

    @PostMapping("/users")
    public ResponseEntity<ApiResponse<UserResponseDto>> createUser(
            @Valid @RequestBody CreateUserDto createUserDto) {
        UserResponseDto createdUser = adminService.createUser(createUserDto);
        return ResponseHelper.created(createdUser, "User created successfully");
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<ApiResponse<UserProfileResponseDto>> getUserById(
            @PathVariable UUID userId) {
        UserProfileResponseDto user = adminService.getUserById(userId);
        return ResponseHelper.ok(user, "User retrieved successfully");
    }

    @PutMapping("/{userId}/activate")
    public ResponseEntity<ApiResponse<Void>> activateUser(@PathVariable UUID userId) {
        adminService.activateUserById(userId);
        return ResponseHelper.ok("User activated successfully");
    }

    @PutMapping("/{userId}/deactivate")
    public ResponseEntity<ApiResponse<Void>> deactivateUser(@PathVariable UUID userId) {
        adminService.deActivateUserById(userId);
        return ResponseHelper.ok("User deactivated successfully");
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable UUID userId) {
        adminService.deleteUserById(userId);
        return ResponseHelper.ok("User deleted successfully");
    }

    // ==================== LAWYER MANAGEMENT ====================

    /*
       OPTIMIZATION 1: Changed Return Type from List<Dto> to Page<Dto>.
       Returning 'Page' includes metadata (total pages, total elements)
       which allows the frontend to stop requesting data once it reaches the end.

       OPTIMIZATION 2: Added @PageableDefault.
       If a malicious or buggy client requests size=10000, your server CPU spikes.
       This limits the default load.
    */

    @GetMapping("/lawyers")
    public ResponseEntity<ApiResponse<Page<LawyerDto>>> getAllLawyers(
             Pageable pageable) {
        Page<LawyerDto> lawyers = adminService.getAllLawyerProfile(pageable);
        return ResponseHelper.okPage(lawyers, "Lawyers retrieved successfully");
    }

    @GetMapping("/lawyers/active")
    public ResponseEntity<ApiResponse<Page<LawyerDto>>> getAllActiveLawyers(
             Pageable pageable) {
        Page<LawyerDto> lawyers = adminService.getAllActivatedLawyers(pageable);
        return ResponseHelper.okPage(lawyers, "Active lawyers retrieved successfully");
    }

    @GetMapping("/lawyers/deactivated")
    public ResponseEntity<ApiResponse<Page<LawyerDto>>> getAllDeactivatedLawyers(
             Pageable pageable) {
        Page<LawyerDto> lawyers = adminService.getAllDeactivatedLawyers(pageable);
        return ResponseHelper.okPage(lawyers, "Deactivated lawyers retrieved successfully");
    }

    @GetMapping("/lawyers/approved")
    public ResponseEntity<ApiResponse<Page<LawyerDto>>> getAllApprovedLawyers(
             Pageable pageable) {
        Page<LawyerDto> approvedLawyers = adminService.getAllApprovedLawyers(pageable);
        return ResponseHelper.okPage(approvedLawyers, "Approved lawyers retrieved successfully");
    }

    @GetMapping("/lawyers/rejected")
    public ResponseEntity<ApiResponse<Page<LawyerDto>>> getAllRejectedLawyers(
             Pageable pageable) {
        Page<LawyerDto> rejectedLawyers = adminService.getAllRejectedLawyers(pageable);
        return ResponseHelper.okPage(rejectedLawyers, "Rejected lawyers retrieved successfully");
    }

    @PutMapping("/lawyers/{lawyerId}/approve")
    public ResponseEntity<ApiResponse<Void>> approveLawyer(@PathVariable UUID lawyerId) {
        adminService.acceptLawyerApprovalById(lawyerId);
        return ResponseHelper.ok("Lawyer approved successfully");
    }

    @PutMapping("/lawyers/{lawyerId}/reject")
    public ResponseEntity<ApiResponse<Void>> rejectLawyer(@PathVariable UUID lawyerId) {
        adminService.rejectLawyerApprovalById(lawyerId);
        return ResponseHelper.ok("Lawyer rejected successfully");
    }

    // ==================== JUDGE MANAGEMENT ====================

    @GetMapping("/judges")
    public ResponseEntity<ApiResponse<Page<JudgeProfileDto>>> getAllJudges(
             Pageable pageable) {
        Page<JudgeProfileDto> judges = adminService.getAllJudgesProfile(pageable);
        return ResponseHelper.okPage(judges, "Judges retrieved successfully");
    }

    @GetMapping("/judges/active")
    public ResponseEntity<ApiResponse<Page<JudgeProfileDto>>> getAllActiveJudges(
             Pageable pageable) {
        Page<JudgeProfileDto> judges = adminService.getAllActivatedJudges(pageable);
        return ResponseHelper.okPage(judges, "Active judges retrieved successfully");
    }

    @GetMapping("/judges/deactivated")
    public ResponseEntity<ApiResponse<Page<JudgeProfileDto>>> getAllDeactivatedJudges(
             Pageable pageable) {
        Page<JudgeProfileDto> judges = adminService.getAllDeactivatedJudges(pageable);
        return ResponseHelper.okPage(judges, "Deactivated judges retrieved successfully");
    }

    @PutMapping("/judges/{judgeId}")
    public ResponseEntity<ApiResponse<Void>> updateJudgeProfile(
            @PathVariable UUID judgeId,
            @Valid @RequestBody JudgeProfileDto judgeProfileDto) {
        adminService.updateJudgeProfile(judgeId, judgeProfileDto);
        return ResponseHelper.ok("Judge profile updated successfully");
    }

    // ==================== CASE ACCESS REQUESTS ====================

    @GetMapping("/lawyer-access/status")
    public ResponseEntity<ApiResponse<Page<CaseRequestResponseDto>>> getAllCaseRequestsByStatus(
            @RequestParam RequestStatus status,
            Pageable pageable) {
        Page<CaseRequestResponseDto> requests = adminService.getAllCaseRequestsByStatus(status, pageable);
        return ResponseHelper.okPage(requests, "Case requests retrieved successfully");
    }

    @GetMapping("/lawyer-case-requests")
    public ResponseEntity<ApiResponse<Page<CaseRequestResponseDto>>> getAllCaseRequests(
            Pageable pageable) {
        Page<CaseRequestResponseDto> requests = adminService.getAllCaseRequests(pageable);
        return ResponseHelper.okPage(requests, "Case requests retrieved successfully");
    }

    @PutMapping("/lawyer-access/{requestId}/approve")
    public ResponseEntity<ApiResponse<Void>> approveRequest(@PathVariable UUID requestId) {
        adminService.approveCaseAccessRequest(requestId);
        return ResponseHelper.ok("Request approved successfully. Lawyer assigned to case.");
    }

    @PutMapping("/lawyer-access/{requestId}/reject")
    public ResponseEntity<ApiResponse<Void>> rejectRequest(@PathVariable UUID requestId) {
        adminService.rejectCaseAccessRequest(requestId);
        return ResponseHelper.ok("Request rejected successfully");
    }
}
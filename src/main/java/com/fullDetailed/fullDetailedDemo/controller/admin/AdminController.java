package com.fullDetailed.fullDetailedDemo.controller.admin;

import com.fullDetailed.fullDetailedDemo.domain.dtos.judge.JudgeProfileDto;
import com.fullDetailed.fullDetailedDemo.services.interfaces.admin.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/users")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PutMapping("/{userId}/deactivate")
    public ResponseEntity<Map<String, String>> deactivateUser(@PathVariable UUID userId) {
        adminService.deActivateUserById(userId);
        return ResponseEntity.ok(Map.of(
                "message", "User deactivated successfully"
        ));
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
}

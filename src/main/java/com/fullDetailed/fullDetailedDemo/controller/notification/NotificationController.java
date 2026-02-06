package com.fullDetailed.fullDetailedDemo.controller.notification;

import com.fullDetailed.fullDetailedDemo.domain.dtos.ApiResponse;
import com.fullDetailed.fullDetailedDemo.domain.dtos.notificatino.NotificationDto;
import com.fullDetailed.fullDetailedDemo.services.interfaces.notification.NotificationService;
import com.fullDetailed.fullDetailedDemo.util.ResponseHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<NotificationDto>>> getMyNotifications() {
        List<NotificationDto> notifications = notificationService.getAllNotifications();
        return ResponseHelper.ok(notifications, "Notifications retrieved successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<NotificationDto>> getNotificationById(@PathVariable UUID id) {
        NotificationDto notification = notificationService.getNotificationById(id);
        return ResponseHelper.ok(notification, "Notification retrieved successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteNotification(@PathVariable UUID id) {
        notificationService.deleteById(id);
        return ResponseHelper.ok("Notification deleted successfully");
    }

    @GetMapping("/unread-count")
    public ResponseEntity<ApiResponse<Integer>> getUnreadCount() {
        int count = notificationService.getUnreadNotificationsCount();
        return ResponseHelper.ok(count, "Unread notifications count retrieved successfully");
    }
}
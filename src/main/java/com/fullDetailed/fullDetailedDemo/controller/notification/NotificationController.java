package com.fullDetailed.fullDetailedDemo.controller.notification;


import com.fullDetailed.fullDetailedDemo.domain.dtos.notificatino.NotificationDto;
import com.fullDetailed.fullDetailedDemo.services.interfaces.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<NotificationDto>> getMyNotifications() {
        return ResponseEntity.ok(notificationService.getAllNotifications());
    }


    @GetMapping("/{id}")
    public ResponseEntity<NotificationDto> getNotificationById(@PathVariable UUID id) {
        return ResponseEntity.ok(notificationService.getNotificationById(id));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteNotification(@PathVariable UUID id) {
        notificationService.deleteById(id);
        return ResponseEntity.ok(Map.of("message", "Notification deleted successfully"));
    }


    @GetMapping("/unread-count")
    public ResponseEntity<Map<String, Integer>> getUnreadCount() {
        int count = notificationService.getUnreadNotificationsCount();
        return ResponseEntity.ok(Map.of("count", count));
    }
}

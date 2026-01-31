package com.fullDetailed.fullDetailedDemo.mapper.notification;

import com.fullDetailed.fullDetailedDemo.domain.dtos.notificatino.NotificationDto;
import com.fullDetailed.fullDetailedDemo.domain.entities.Notification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

public class NotificationMapper {

    public static NotificationDto toDto(Notification entity) {
        if (entity == null) {
            return null;
        }
        return NotificationDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .message(entity.getMessage())
                .isRead(entity.isRead())
                .createdAt(entity.getCreatedAt())
                .build();
    }

}

package com.fullDetailed.fullDetailedDemo.domain.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationEvent {
    private UUID receiverId;
    private String title;
    private String message;
}

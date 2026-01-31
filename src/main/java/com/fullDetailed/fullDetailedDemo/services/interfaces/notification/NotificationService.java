package com.fullDetailed.fullDetailedDemo.services.interfaces.notification;

import com.fullDetailed.fullDetailedDemo.domain.dtos.notificatino.NotificationDto;
import com.fullDetailed.fullDetailedDemo.domain.entities.User;

import java.rmi.server.UID;
import java.util.List;
import java.util.UUID;

public interface NotificationService {
    void createAndSend(User receiver,String title,String message);
    List<NotificationDto>getAllNotifications();
    NotificationDto getNotificationById(UUID notId);
    void deleteById(UUID notId);
    int getUnreadNotificationsCount();

}

package com.fullDetailed.fullDetailedDemo.services.impl.notification;

import com.fullDetailed.fullDetailedDemo.domain.event.NotificationEvent;
import com.fullDetailed.fullDetailedDemo.services.interfaces.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationEventListener {
    private final NotificationService notificationService;

    @Async("notificationExecutor")
    @TransactionalEventListener
    public void handleNotification(NotificationEvent ev){
        try{
            notificationService.createAndSend(
                    ev.getReceiverId(),
                    ev.getTitle(),
                    ev.getMessage()
            );
        } catch (Exception e) {
            log.error("Failed to send notification to {}: {}",
                    ev.getReceiverId(), e.getMessage());
        }
    }
}

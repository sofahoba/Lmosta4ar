package com.fullDetailed.fullDetailedDemo.services.impl.notification;

import com.fullDetailed.fullDetailedDemo.domain.dtos.notificatino.NotificationDto;
import com.fullDetailed.fullDetailedDemo.domain.entities.Notification;
import com.fullDetailed.fullDetailedDemo.domain.entities.User;
import com.fullDetailed.fullDetailedDemo.exceptions.NotFoundException;
import com.fullDetailed.fullDetailedDemo.mapper.notification.NotificationMapper;
import com.fullDetailed.fullDetailedDemo.repository.NotificationRepository;
import com.fullDetailed.fullDetailedDemo.repository.UserRepo;
import com.fullDetailed.fullDetailedDemo.services.interfaces.notification.NotificationService;
import com.fullDetailed.fullDetailedDemo.util.UserContextService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepo userRepo;
    private final SimpMessagingTemplate messagingTemplate;
    private final UserContextService contextService;

    @Override
    public void createAndSend(User receiver, String title, String message) {
        Notification notification = Notification.builder()
                .recipient(receiver)
                .title(title)
                .message(message)
                .isRead(false)
                .build();

        notificationRepository.save(notification);

        NotificationDto notDto = NotificationDto.builder()
                .title(title)
                .message(message)
                .build();

        messagingTemplate.convertAndSendToUser(
                receiver.getEmail(),
                "/queue/notifications",
                notDto
        );
    }

    @Override
    public List<NotificationDto> getAllNotifications() {
        User currUser=contextService.getCurrentUser();

        return notificationRepository.findByRecipientIdOrderByCreatedAtDesc(currUser.getId())
                .stream()
                .map(NotificationMapper::toDto)
                .toList();
    }

    @Override
    public NotificationDto getNotificationById(UUID notId) {

        User currUser=contextService.getCurrentUser();
        Notification not=notificationRepository.findById(notId).orElseThrow(()->new NotFoundException("notification not found"));
        if(not.getRecipient().getId() != currUser.getId()){
            throw new NotFoundException("notification not found");
        }
        not.setRead(true);
        notificationRepository.save(not);
        return NotificationMapper.toDto(not);

    }

    @Override
    public void deleteById(UUID notId) {
        User currentUser = contextService.getCurrentUser();
        Notification not = notificationRepository.findById(notId)
                .orElseThrow(() -> new NotFoundException("Notification not found"));

        if (!not.getRecipient().getId().equals(currentUser.getId())) {
            throw new NotFoundException("Notification not found");
        }
        notificationRepository.delete(not);
    }

    @Override
    public int getUnreadNotificationsCount() {
        User currentUser = contextService.getCurrentUser();
        return (int) notificationRepository.countByRecipientIdAndIsReadFalse(currentUser.getId());
    }
}

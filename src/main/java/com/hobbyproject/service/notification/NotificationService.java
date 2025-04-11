package com.hobbyproject.service.notification;

import com.hobbyproject.dto.notification.NotificationMessage;
import com.hobbyproject.entity.NotificationLog;
import com.hobbyproject.repository.notificationlog.NotificationLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationLogRepository notificationLogRepository;

    public void saveNotification(NotificationMessage message) {
        NotificationLog log = new NotificationLog(
                message.getUserId(),
                message.getMessage(),
                LocalDateTime.now()
        );
        notificationLogRepository.save(log);
    }

    public List<NotificationMessage> getUserNotifications(String userId) {
        List<NotificationLog> logs = notificationLogRepository.findTop6ByUserIdOrderByCreatedAtDesc(userId);

        return logs.stream()
                .map(log -> new NotificationMessage(
                        log.getId(),
                        log.getUserId(),
                        log.getMessage(),
                        log.getCreatedAt(),
                        log.isRead()
                ))
                .toList();
    }


    @Scheduled(cron = "0 0 3 * * *")
    public void deleteOldNotifications() {
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
        notificationLogRepository.deleteByCreatedAtBefore(oneWeekAgo);
    }

    @Transactional
    public void markNotificationsAsRead(List<Long> notificationIds) {
        List<NotificationLog> notifications = notificationLogRepository.findAllById(notificationIds);

        for (NotificationLog log : notifications) {
            log.markAsRead();
        }
    }
}

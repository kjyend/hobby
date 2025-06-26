package com.hobbyproject.controller.notification;

import com.hobbyproject.dto.notification.NotificationMessage;
import com.hobbyproject.rabbitmq.NotificationProducer;
import com.hobbyproject.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;
    private final NotificationProducer notificationProducer;

    @PostMapping("/send")
    public ResponseEntity<String> sendNotification(@RequestBody NotificationMessage message) {
        notificationProducer.sendNotification(message); // RabbitMQ로 전송
        return ResponseEntity.ok("알림이 RabbitMQ를 통해 전송되었습니다.");
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<NotificationMessage>> getNotifications(@PathVariable("userId") String userId) {
        List<NotificationMessage> notifications = notificationService.getUserNotifications(userId);
        return ResponseEntity.ok(notifications);
    }

    @PatchMapping("/read")
    public ResponseEntity<Void> markNotificationsAsRead(@RequestBody List<Long> ids) {
        notificationService.markNotificationsAsRead(ids);
        return ResponseEntity.ok().build();
    }
}

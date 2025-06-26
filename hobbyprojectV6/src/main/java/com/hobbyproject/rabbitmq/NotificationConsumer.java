package com.hobbyproject.rabbitmq;

import com.hobbyproject.dto.notification.NotificationMessage;
import com.hobbyproject.service.notification.NotificationService;
import com.hobbyproject.service.sse.SseEmitterService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


@Component
@RabbitListener(queues = "${rabbitmq.queue.name}")
@RequiredArgsConstructor
public class NotificationConsumer {

    private final NotificationService notificationService;
    private final SseEmitterService sseEmitters;


    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void receiveNotification(NotificationMessage message) {
        notificationService.saveNotification(message);
        sseEmitters.sendNotification(message.getUserId(), message.getMessage());
    }
}

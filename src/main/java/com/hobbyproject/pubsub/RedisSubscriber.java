package com.hobbyproject.pubsub;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hobbyproject.dto.notification.NotificationMessage;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class RedisSubscriber implements MessageListener {

    private final SseEmitters sseEmitter;

    public RedisSubscriber(SseEmitters sseEmitter) {
        this.sseEmitter = sseEmitter;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String msg = new String(message.getBody(), StandardCharsets.UTF_8);
            ObjectMapper objectMapper = new ObjectMapper();
            NotificationMessage notificationMessage = objectMapper.readValue(msg, NotificationMessage.class);

            sseEmitter.sendNotification(notificationMessage.getPostOwner(), notificationMessage.getContent());
        } catch (Exception e) {
            System.err.println("Redis 메시지 처리 중 오류 발생: {}" + e.getMessage());
        }
    }
}

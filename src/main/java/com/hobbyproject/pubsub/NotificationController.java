package com.hobbyproject.pubsub;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notifications")
public class NotificationController {
    private final SseEmitters sseEmitters;

    @GetMapping("/subscribe/{userId}")
    public SseEmitter subscribe(@PathVariable String userId) {
        SseEmitter emitter = sseEmitters.addEmitter(userId);

        emitter.onCompletion(() -> {
            System.out.println("SSE 연결 종료됨: " + userId);
            sseEmitters.removeEmitter(userId);
        });

        emitter.onTimeout(() -> {
            System.out.println("SSE 타임아웃: " + userId);
            sseEmitters.removeEmitter(userId);
        });

        System.out.println("SSE 구독 성공: " + userId);
        return emitter;
    }
}

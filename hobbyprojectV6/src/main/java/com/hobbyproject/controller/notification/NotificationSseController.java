package com.hobbyproject.controller.notification;

import com.hobbyproject.service.sse.SseEmitterService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/notifications/sse")
public class NotificationSseController {

    private final SseEmitterService sseEmitters;

    public NotificationSseController(SseEmitterService sseEmitters) {
        this.sseEmitters = sseEmitters;
    }

    @GetMapping(value = "/subscribe/{userId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@PathVariable("userId") String userId) {
        return sseEmitters.subscribe(userId);
    }
}

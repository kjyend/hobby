package com.hobbyproject.service.sse;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SseEmitters {
    private final Map<String, SseEmitter> emitters=new ConcurrentHashMap<>();

    public SseEmitter addEmitter(String userId) {
        SseEmitter emitter = new SseEmitter(30 * 60 * 1000L);
        emitters.put(userId,emitter);
        emitter.onCompletion(()->emitters.remove(userId));
        emitter.onTimeout(()->emitters.remove(userId));
        return emitter;
    }

    public void removeEmitter(String userId) {
        emitters.remove(userId);
    }

    public void sendNotification(String userId, String message){
        SseEmitter emitter = emitters.get(userId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().name("notification").data(message));
            } catch (IOException e) {
                emitters.remove(userId);
            }
        }
    }
}

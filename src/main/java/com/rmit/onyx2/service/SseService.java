package com.rmit.onyx2.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class SseService {
    final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();
    private Long workspaceId;
    private int numSetPos;

    public void setNumSetPos(int numSetPos) {
        this.numSetPos = numSetPos;
    }

    public void setWorkspaceId(Long workspaceId) {
        this.workspaceId = workspaceId;
    }

    public void addEmitter(final SseEmitter emitter) {
        emitters.add(emitter);
    }

    public void removeEmitter(final SseEmitter emitter) {
        emitters.remove(emitter);
    }

    //Notify where emitters receive something to return
    @Async
    public void doNotify(String message) {
        List<SseEmitter> deadEmitters = new ArrayList<>();
        emitters.forEach(emitter -> {
            try {
                emitter.send(SseEmitter.event().data(workspaceId + " " + message + " " + numSetPos));
            } catch (Exception e) {
                deadEmitters.add(emitter);
            }
        });
        emitters.removeAll(deadEmitters);
    }
    
    //Notify where emitters receive something to return
    @Async
    public void doNotifyDashboard(String message) {
        List<SseEmitter> deadEmitters = new ArrayList<>();
        emitters.forEach(emitter -> {
            try {
                emitter.send(SseEmitter.event().data(message));
            } catch (Exception e) {
                deadEmitters.add(emitter);
            }
        });
        emitters.removeAll(deadEmitters);
    }
}

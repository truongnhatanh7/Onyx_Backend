package com.rmit.onyx2.controller;

import com.rmit.onyx2.service.SseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequestMapping("/api/v1/sse")
@CrossOrigin(origins = "*")
public class SseController {

    @Autowired
    SseService service;

    final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    @GetMapping("/notification/{workspaceId}")
    public ResponseEntity<SseEmitter> doNotify(@PathVariable(name = "workspaceId") Long workspaceId) {
        final SseEmitter emitter = new SseEmitter();
        service.setWorkspaceId(workspaceId);
        service.addEmitter(emitter);
        emitter.onCompletion(() -> service.removeEmitter(emitter));
        emitter.onTimeout(() -> service.removeEmitter(emitter));
        return new ResponseEntity<>(emitter, HttpStatus.OK);
    }

    @GetMapping("/notification")
    public ResponseEntity<SseEmitter> doNotifyDashboard() {
        final SseEmitter emitter = new SseEmitter();
        service.addEmitter(emitter);
        emitter.onCompletion(() -> service.removeEmitter(emitter));
        emitter.onTimeout(() -> service.removeEmitter(emitter));
        return new ResponseEntity<>(emitter, HttpStatus.OK);
    }

    @PostMapping("/num-setpos/{num}")
    public void setNumSetPos(@PathVariable(name = "num") int num) {
        service.setNumSetPos(num);
    }
}

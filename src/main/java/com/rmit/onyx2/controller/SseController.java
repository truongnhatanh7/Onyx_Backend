package com.rmit.onyx2.controller;

import com.rmit.onyx2.service.SseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/v1/sse")
@CrossOrigin(origins = "*")
public class SseController {

    @Autowired
    SseService service;
    //Need an emitter to track changes im the front end
    @GetMapping("/notification/{workspaceId}")
    public ResponseEntity<SseEmitter> doNotify(@PathVariable(name = "workspaceId") Long workspaceId) {
        final SseEmitter emitter = new SseEmitter();
        service.setWorkspaceId(workspaceId);
        service.addEmitter(emitter);
        emitter.onCompletion(() -> service.removeEmitter(emitter));
        emitter.onTimeout(() -> service.removeEmitter(emitter));
        return new ResponseEntity<>(emitter, HttpStatus.OK);
    }

    //Keep track of changes in the dashboard
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

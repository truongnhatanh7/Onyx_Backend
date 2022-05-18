package com.rmit.onyx2.controller;

import com.rmit.onyx2.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/email")
@CrossOrigin(origins = "*")
public class EmailController {
    private final EmailService emailService;

    //Dependency injection of Email Controller
    @Autowired
    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    //A function to send email to the user
    @PostMapping("/notifyUser/{userEmail}")
    public void sendEmail(@PathVariable String userEmail) {
        emailService.sendSimpleEmail(userEmail);
    }
}
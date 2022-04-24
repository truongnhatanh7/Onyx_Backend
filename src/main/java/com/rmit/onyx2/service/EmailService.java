package com.rmit.onyx2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private JavaMailSender emailSender;

    @Autowired
    public EmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void sendSimpleEmail(String userEmail) {

        // Create a Simple MailMessage.
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("noreply.onxy@gmail.com");
        message.setTo(userEmail);
        message.setSubject("Onyx Notification");
        message.setText("ONYX NOTIFICATION: You have been added to a new project");

        // Send Message
        emailSender.send(message);
    }
}

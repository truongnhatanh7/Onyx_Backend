package com.rmit.onyx2.service;

import org.junit.Rule;
//import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Arrays;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public
class EmailServiceTest {

    private EmailService emailService;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private JavaMailSender emailSender;

    private SimpleMailMessage testMessage;

    @Before
    public void setUp() {
        testMessage = new SimpleMailMessage();
        emailService = new EmailService(emailSender);
    }

    @After
    public void tearDown() {
        testMessage = null;
        emailService = null;
    }

    @Test
    @DisplayName("Should send the email successfully")
    public void should_Send_Simple_Email() {
        String recipient = "rinkakitoran@gmail.com";
        String testUserEmail = "rinkakitoran@gmail.com";

        // Setting contents for message
        testMessage.setFrom("noreply.onxy@gmail.com");
        testMessage.setTo(testUserEmail);
        testMessage.setSubject("Onyx Notification");
        testMessage.setText("ONYX NOTIFICATION: You have been added to a new project");

        emailService.sendSimpleEmail(testUserEmail);
        ArgumentCaptor<SimpleMailMessage> messageArgumentCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);

        verify(emailSender).send(messageArgumentCaptor.capture()); // Check for successful invocation of send()
        assertThat(messageArgumentCaptor.getValue()).isEqualTo(testMessage); // Check the passing argument in send() is same with the testMessage
        assertThat(recipient).isEqualTo(Arrays.stream(Objects.requireNonNull(testMessage.getTo())).findFirst().get()); // Check for equality of recipient
        System.out.println("Test case passed: Email is sent successfully.");
    }
}
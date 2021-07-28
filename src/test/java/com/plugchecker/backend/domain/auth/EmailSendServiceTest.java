package com.plugchecker.backend.domain.auth;

import com.plugchecker.backend.ServiceTest;
import com.plugchecker.backend.domain.auth.service.EmailSendService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.mail.internet.MimeMessage;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

public class EmailSendServiceTest extends ServiceTest {
    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailSendService emailSendService;

    @Test
    void sendCertification_Test() {
        // given
        String toEmail = "xxxxx@gmail.com";
        String number = "9999";

        JavaMailSender testMailSender = new JavaMailSenderImpl();

        given(mailSender.createMimeMessage()).willReturn(testMailSender.createMimeMessage());

        // when
        emailSendService.sendCertification(toEmail, number);

        // then
        verify(mailSender).send((MimeMessage) any());
    }

    @Test
    void sendCertification_Exception_Test() {
        // given
        String toEmail = "xxxxx@gmail.com";
        String number = "9999";

        // when, then
        Assertions.assertThrows(RuntimeException.class, ()-> emailSendService.sendCertification(toEmail, number));
    }

}

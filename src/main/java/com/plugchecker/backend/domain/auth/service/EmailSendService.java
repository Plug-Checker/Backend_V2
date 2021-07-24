package com.plugchecker.backend.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
public class EmailSendService {

    @Value("${spring.mail.username}")
    private String fromEmail;

    private final JavaMailSender mailSender;

    public void sendCertification(String toEmail, String number) {

        StringBuffer sb = new StringBuffer();
        sb.append("<h2>인증번호</h3>\n");
        sb.append("<h1>" + number + "</h1>");
        sb.append("<p>PLUG-CHECKER 회원가입을 위한 인증번호입니다. 5분내로 인증해주세요.</p>");
        String html = sb.toString();

        MimeMessage message = mailSender.createMimeMessage();

        try {

            Multipart mParts = new MimeMultipart();
            MimeBodyPart mTextPart = new MimeBodyPart();

            mTextPart.setText(html, "UTF-8", "html");
            mParts.addBodyPart(mTextPart);

            message.setFrom(new InternetAddress(fromEmail, "PLUG-CHEKCER"));
            message.setRecipients(MimeMessage.RecipientType.TO, InternetAddress.parse(toEmail, false));
            message.setSubject("PLUG-CHECKER 회원가입 인증번호 발송");
            message.setContent(mParts);

        } catch (Exception e) {
            e.printStackTrace();
        }

        mailSender.send(message);
    }
}

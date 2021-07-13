package com.plugchecker.backend.domain.auth.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.activation.CommandMap;
import javax.activation.MailcapCommandMap;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

@Service
public class EmailSendService {

    @Value("${auth.email.address}")
    private String fromEmail;

    @Value("${auth.email.password}")
    private String password;

    public void sendCertification(String toEmail, String number) {
        final String bodyEncoding = "UTF-8";

        String subject = "PLUG-CHECKER 회원가입 인증번호 발송";
        String fromUsername = "PLUG-CHECKER";

        // 메일에 출력할 텍스트
        StringBuffer sb = new StringBuffer();
        sb.append("<h2>인증번호</h3>\n");
        sb.append("<h1>" + number + "</h1>");
        sb.append("<p>PLUG-CHECKER 회원가입을 위한 인증번호입니다. 5분내로 인증해주세요.</p>");
        String html = sb.toString();

        // 메일 옵션 설정
        Properties props = mailPropertiesSettings();

        try {
            // 메일 서버  인증 계정 설정
            Authenticator auth = new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(fromEmail, password);
                }
            };

            // 메일 세션 생성
            Session session = Session.getInstance(props, auth);

            // 메일 송/수신 옵션 설정
            Message message = new MimeMessage(session);
            mailSendSettigns(message, fromUsername, toEmail, subject);

            // 메일 콘텐츠 설정
            Multipart mParts = new MimeMultipart();
            MimeBodyPart mTextPart = new MimeBodyPart();

            // 메일 콘텐츠 - 내용
            mTextPart.setText(html, bodyEncoding, "html");
            mParts.addBodyPart(mTextPart);

            // 메일 콘텐츠 설정
            message.setContent(mParts);

            mailMimeSettings();

            // 메일 발송
            Transport.send( message );

        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    private Properties mailPropertiesSettings() {
        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.auth", "true");

        props.put("mail.smtp.quitwait", "false");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");

        return props;
    }

    private void mailMimeSettings() {
        MailcapCommandMap MailcapCmdMap = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
        MailcapCmdMap.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
        MailcapCmdMap.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
        MailcapCmdMap.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
        MailcapCmdMap.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
        MailcapCmdMap.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
        CommandMap.setDefaultCommandMap(MailcapCmdMap);
    }

    private void mailSendSettigns(Message message, String fromUsername, String toEmail, String subject) throws MessagingException, UnsupportedEncodingException {
        message.setFrom(new InternetAddress(fromEmail, fromUsername));
        message.setRecipients(MimeMessage.RecipientType.TO, InternetAddress.parse(toEmail, false));
        message.setSubject(subject);
        message.setSentDate(new Date());
    }
}

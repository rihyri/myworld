package com.community.board.mail;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
public class TempPasswordMail {

    private final JavaMailSender javaMailSender465;
    private final JavaMailSender javaMailSender587;

    private String ePw;

    public MimeMessage createMessage(String to, JavaMailSender javaMailSender) throws UnsupportedEncodingException, MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();

        message.addRecipients(Message.RecipientType.TO, to);
        message.setSubject("임시 비밀번호");

        String msgg = "";
        msgg += "<div style='margin:100px;'>";
        msgg += "<div align='center' style='border:1px solid black; font-family:verdana';>";
        msgg += "<h3 style='color:blue;'>MY WORLD 임시 비밀번호입니다.</h3>";
        msgg += "<div style='font-size:130%'>";
        msgg += "CODE : <strong>";
        msgg += ePw + "</strong><div><br/> ";
        msgg += "</div>";
        message.setText(msgg, "utf-8", "html");
        message.setFrom(new InternetAddress("hyerijw1221@naver.com", "admin"));

        return message;
    }

    public void sendSimpleMessage(String to, String pw, boolean usePort587) {
        this.ePw = pw;

        JavaMailSenderImpl senderImpl;
        if (usePort587) {
            senderImpl = (JavaMailSenderImpl) javaMailSender587;
        } else {
            senderImpl = (JavaMailSenderImpl) javaMailSender465;
        }

        if (senderImpl == null) {
            throw new IllegalStateException("JavaMailSenderImpl is null. Please check your configuration.");
        }

        MimeMessage message;
        try {
            message = createMessage(to, senderImpl);
        } catch (UnsupportedEncodingException | MessagingException e) {
            e.printStackTrace();
            throw new EmailException("이메일 생성 에러");
        }

        try {
            senderImpl.send(message);
        } catch (MailException e) {
            e.printStackTrace();
            throw new EmailException("이메일 전송 에러");
        }
    }
}

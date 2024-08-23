package com.community.board.mail;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Random;

@Service
public class RegisterMail implements MailServiceInter {

    private final JavaMailSender emailsender;

    @Autowired
    public RegisterMail(@Qualifier("mailSender465") JavaMailSender emailsender) {
        this.emailsender = emailsender;
    }

    private String ePw;

    @Override
    public MimeMessage createMessage(String to) throws MessagingException, UnsupportedEncodingException {
        System.out.println("보내는 대상 : " + to);
        System.out.println("인증 번호 : " + ePw);

        MimeMessage message = emailsender.createMimeMessage();

        message.addRecipients(Message.RecipientType.TO, to);
        message.setSubject("MyWorld 회원가입 이메일 인증");

        String msgg = "";
        msgg += "<div style='margin:100px;'>";
        msgg += "<h1>안녕하세요.</h1>";
        msgg += "<h1>온라인 커뮤니티 사이트 MyWorld 입니다.</h1>";
        msgg += "<p>아래 코드를 회원가입 창으로 돌아가 입력해주세요.</p>";
        msgg += "<br>";
        msgg += "<div align='center' style='border:1px solid black;'>";
        msgg += "<h3>회원가입 인증 코드</h3>";
        msgg += "CODE : <strong>";
        msgg += ePw + "</strong><div><br />";
        msgg += "<div>";
        message.setText(msgg, "utf-8", "html");
        message.setFrom(new InternetAddress("hyerijw1221@naver.com", "MyWorld_Admin"));

        return message;
    }

    @Override
    public String createKey() {
        StringBuffer key = new StringBuffer();
        Random rnd = new Random();

        for (int i = 0; i < 8; i++) {
            int index = rnd.nextInt(3);

            switch (index) {
                case 0:
                    key.append((char) ((int) (rnd.nextInt(26)) + 97));
                    break;
                case 1:
                    key.append((char) ((int) (rnd.nextInt(26)) + 65));
                    break;
                case 2:
                    key.append((rnd.nextInt(10)));
                    break;
            }
        }

        return key.toString();
    }

    @Override
    public String sendSimpleMessage(String to) throws Exception {

        ePw = createKey();

        MimeMessage message = createMessage(to);
        try {
            emailsender.send(message);
        } catch (MailException es) {
            es.printStackTrace();
            throw new IllegalStateException();
        }

        return ePw;
    }
}

package com.community.board.mail;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

    @Bean(name = "mailSender465")
    @Primary
    public JavaMailSender javaMailService465() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost("smtp.naver.com");
        javaMailSender.setUsername("hyerijw1221@naver.com");
        javaMailSender.setPassword("haktitch11020421");
        javaMailSender.setPort(465);
        javaMailSender.setJavaMailProperties(getMailProperties(true)); // SSL 사용
        return javaMailSender;
    }

    @Bean(name = "mailSender587")
    public JavaMailSender javaMailService587() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost("smtp.naver.com");
        javaMailSender.setUsername("hyerijw1221@naver.com");
        javaMailSender.setPassword("haktitch11020421");
        javaMailSender.setPort(587);
        javaMailSender.setJavaMailProperties(getMailProperties(false)); // SSL 사용 안 함
        return javaMailSender;
    }

    private Properties getMailProperties(boolean useSSL) {
        Properties properties = new Properties();
        properties.setProperty("mail.transport.protocol", "smtp");
        properties.setProperty("mail.smtp.auth", "true");

        if (useSSL) {
            properties.setProperty("mail.smtp.starttls.enable", "false");
            properties.setProperty("mail.smtp.ssl.enable", "true");
        } else {
            properties.setProperty("mail.smtp.starttls.enable", "true");
            properties.setProperty("mail.smtp.ssl.enable", "false");
        }

        properties.setProperty("mail.debug", "true");
        properties.setProperty("mail.smtp.ssl.trust", "smtp.naver.com");

        return properties;
    }
}


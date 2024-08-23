package com.community.board.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.MailMessage;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public interface MailServiceInter {

    MimeMessage createMessage(String to) throws MessagingException, UnsupportedEncodingException;

    String createKey();

    String sendSimpleMessage(String to) throws Exception;
}

package com.urutare.stockm.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.concurrent.CompletableFuture;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    public EmailService(@Autowired JavaMailSender mailSender,
                        @Autowired SpringTemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
        this.mailSender = mailSender;
    }

    public void sendEmail(String to, String subject, String body) {
        CompletableFuture.runAsync(() -> {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
        });
    }

    public void sendEmail(String to, String subject, Context context, String template) throws MessagingException {
        CompletableFuture.runAsync(() -> {
            try {
                String body = templateEngine.process(template, context);
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true);
                helper.setTo(to);
                helper.setSubject(subject);
                helper.setText(body, true);
                mailSender.send(message);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        });
    }
}

package com.example.facebook_demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class SendEmailService {
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String fromEmailId;

    public void sendEmail(String recipient,String body,String subject){
        SimpleMailMessage simpleMailMessage=new SimpleMailMessage();
        simpleMailMessage.setFrom(fromEmailId);
        simpleMailMessage.setTo(recipient);
        simpleMailMessage.setText(body);
        simpleMailMessage.setSubject(subject);

        javaMailSender.send(simpleMailMessage);
    }

    public void sendSignupEmail(String recipient,String firstname,String lastname) throws MessagingException{
        Context context = new Context();
        context.setVariable("firstname", firstname);
        context.setVariable("lastname", lastname);

        String htmlContent=templateEngine.process("signup_email", context);

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        helper.setFrom(fromEmailId);
        helper.setTo(recipient);
        helper.setSubject("Welcome to Our Platform, " + firstname + "!");
        helper.setText(htmlContent, true);

        javaMailSender.send(mimeMessage);
    }
}
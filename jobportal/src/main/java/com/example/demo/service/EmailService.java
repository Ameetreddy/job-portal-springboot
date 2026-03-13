package com.example.demo.service;


import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

   
    private JavaMailSender mailSender;
    
   
    public EmailService(JavaMailSender mailSender) {
		super();
		this.mailSender = mailSender;
	}
	public void sendVerificationEmail(String toEmail, String token) {

        String subject = "Verify Your Email - Job Portal";

        String verificationUrl = "http://localhost:9000/verify?token=" + token;

        String message = "Click the link below to verify your account:\n"
                + verificationUrl;

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(toEmail);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        mailSender.send(mailMessage);
    }
@Async
    public void sendSimpleMail(String to, String subject, String text) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        message.setFrom("ameetreddy2001@gmail.com");

        mailSender.send(message);
    }
}
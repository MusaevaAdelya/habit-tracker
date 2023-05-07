package com.example.habittracker.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
public class EmailService {
	private final JavaMailSender javaMailSender;

	@Value("${spring.mail.username}")
	private String mailUsername;

	public void sendEmail(SimpleMailMessage simpleMailMessage) {
		javaMailSender.send(simpleMailMessage);
	}


	public void sendPasswordResetVerificationEmail(String userEmail, String url) throws MessagingException, UnsupportedEncodingException {
		String subject = "Password Reset Request Verification";
		String senderName = "Habit Tracker Service";
		String mailContent = "<p><b>Hi. You recently requested to reset your password,</b>"+"" +
				"Please, follow the link below to complete the action.</p>"+
				"<a href=\"" +url+ "\">Reset password</a>"+
				"<p> Habit Tracker Service";
		MimeMessage message = javaMailSender.createMimeMessage();
		var messageHelper = new MimeMessageHelper(message);
		messageHelper.setFrom(mailUsername, senderName);
		messageHelper.setTo(userEmail);
		messageHelper.setSubject(subject);
		messageHelper.setText(mailContent, true);
		javaMailSender.send(message);
	}
	
}

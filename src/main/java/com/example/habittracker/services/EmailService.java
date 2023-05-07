package com.example.habittracker.services;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
	private final JavaMailSender javaMailSender;

	public void sendEmail(SimpleMailMessage simpleMailMessage) {
		javaMailSender.send(simpleMailMessage);
	}
	
}

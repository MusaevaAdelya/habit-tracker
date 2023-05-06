package com.example.habittracker.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component	
public class EmailService {

	@Autowired
	private JavaMailSender javaMailSender;
	

	public void sendEmail(SimpleMailMessage simpleMailMessage) {
		javaMailSender.send(simpleMailMessage);
	}
	
}

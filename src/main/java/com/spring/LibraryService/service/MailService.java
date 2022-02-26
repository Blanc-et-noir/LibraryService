package com.spring.LibraryService.service;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service("mailService")
public class MailService {
	@Autowired
	private JavaMailSender mailSender;
	
	
	
	
	//============================================================================================
	//특정 이메일 주소로 메일을 전송하는 메소드.
	//============================================================================================
	@Async
	public void sendMail(String to, String subject, String text) throws Exception{
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
		messageHelper.setTo(to);
		messageHelper.setSubject(subject);
		messageHelper.setText(text);
		messageHelper.setFrom("dign9060@gmail.com");
		mailSender.send(message);
	}
}

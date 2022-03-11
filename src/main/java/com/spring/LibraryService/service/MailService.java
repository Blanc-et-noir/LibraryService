package com.spring.LibraryService.service;

import java.util.HashMap;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service("mailService")
public class MailService implements MailServiceInterface{
	@Autowired
	private JavaMailSender mailSender;
	
	
	
	
	//============================================================================================
	//특정 이메일 주소로 메일을 전송하는 메소드.
	//============================================================================================
	@Async
	public void sendMail(HashMap<String,String> param) throws Exception{
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
		messageHelper.setTo(param.get("to"));
		messageHelper.setSubject(param.get("subject"));
		messageHelper.setText(param.get("text"));
		messageHelper.setFrom("dign9060@gmail.com");
		mailSender.send(message);
	}
}

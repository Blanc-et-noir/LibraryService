package com.spring.LibraryService.controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spring.LibraryService.encrypt.SHA;
import com.spring.LibraryService.service.MailService;
import com.spring.LibraryService.vo.CustomerVO;

@Controller
@EnableAsync
public class MailController {
	@Autowired
	private MailService mailService;

	private void sendMail(String to, String subject, String text) throws Exception{
		mailService.sendMail(to,subject,text);
	}
	
	@RequestMapping(value="/mail/sendEmailAuthCode.do")
	@ResponseBody
	public HashMap<String,String> sendEmailAuthCode(HttpServletRequest request, HttpServletResponse response) {
		HashMap<String, String> map = new HashMap<String,String>();
		HttpSession session = request.getSession(true);
		CustomerVO customerVO = (CustomerVO) session.getAttribute("CUSTOMER");
		
		String EMAIL_AUTHCODE = SHA.DSHA512(SHA.getSalt(), SHA.getSalt()).substring(0, 16);
		String to = request.getParameter("CUSTOMER_EMAIL");
		String subject = "이메일 인증코드";
		String text = "이메일 인증코드는 "+EMAIL_AUTHCODE+" 입니다.";

		try {
			sendMail(to,subject,text);
			session.setAttribute("EMAIL_AUTHCODE", EMAIL_AUTHCODE);
			session.setAttribute("TEMP_EMAIL", to);
			map.put("FLAG", "TRUE");
			map.put("CONTENT", "해당 이메일로 인증번호를 전송했습니다.");
			return map;
		}catch(Exception e) {
			e.printStackTrace();
			map.put("FLAG", "FALSE");
			map.put("CONTENT", "인증번호 전송과정에서 오류가 발생했습니다.");
			return map;
		}
	}
}
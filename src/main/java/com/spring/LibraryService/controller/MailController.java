package com.spring.LibraryService.controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSendException;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.spring.LibraryService.encrypt.SHA;
import com.spring.LibraryService.service.MailServiceInterface;
import com.spring.LibraryService.vo.CustomerVO;

@Controller
@EnableAsync
public class MailController {
	@Autowired
	private MailServiceInterface mailService;

	
	
	
	
	
	//============================================================================================
	//사용자의 이메일을 인증하기 위해 이메일 인증 코드를 전송하는 메소드.
	//============================================================================================
	@RequestMapping(value="/mail/sendEmailAuthCode.do")
	public ResponseEntity<HashMap> sendEmailAuthCode(@RequestParam HashMap param, HttpServletRequest request) {
		HashMap result = new HashMap();
		HttpSession session = request.getSession(true);
		CustomerVO customerVO = (CustomerVO) session.getAttribute("CUSTOMER");
		
		//이메일 인증 코드는 SHA512의 알고리즘을 활용하여 16자리를 전송함.
		//무작위 솔트값 2개를 이용해 더블 해싱함.
		String email_authcode = SHA.DSHA512(SHA.getSalt(), SHA.getSalt()).substring(0, 16);

		param.put("to", param.get("customer_email"));
		param.put("subject", "이메일 인증코드");
		param.put("text", "이메일 인증코드는 "+email_authcode+" 입니다.");
		
		try {
			mailService.sendMail(param);
			
			//세션에 전송한 이메일 인증코드와 이메일 주소를 저장함.
			//이메일 인증후 다시 이메일을 존재하지 않는 것으로 변경하면 다시 인증을 시도하게끔 해야함.
			session.setAttribute("email_authcode", email_authcode);
			session.setAttribute("temp_email", param.get("customer_email"));
			
			System.out.println("템프이메일"+param.get("customer_email"));
			
			result.put("flag", "true");
			result.put("content", "해당 이메일로 인증번호를 전송했습니다.");
			return new ResponseEntity<HashMap>(result,HttpStatus.OK);
		}catch(MailSendException e) {
			result.put("flag", "false");
			result.put("content", "이메일주소가 잘못되었습니다.");
			return new ResponseEntity<HashMap>(result,HttpStatus.BAD_REQUEST);
		}catch(Exception e) {
			result.put("flag", "false");
			result.put("content", "인증번호 전송과정에서 오류가 발생했습니다.");
			return new ResponseEntity<HashMap>(result,HttpStatus.BAD_REQUEST);
		}
	}
}
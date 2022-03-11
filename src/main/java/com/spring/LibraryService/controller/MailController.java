package com.spring.LibraryService.controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spring.LibraryService.encrypt.SHA;
import com.spring.LibraryService.service.MailServiceInterface;
import com.spring.LibraryService.vo.CustomerVO;

@Controller
@EnableAsync
public class MailController {
	@Autowired
	private MailServiceInterface mailService;

	
	
	
	
	
	//============================================================================================
	//������� �̸����� �����ϱ� ���� �̸��� ���� �ڵ带 �����ϴ� �޼ҵ�.
	//============================================================================================
	@RequestMapping(value="/mail/sendEmailAuthCode.do")
	public ResponseEntity<HashMap> sendEmailAuthCode(@RequestParam HashMap param, HttpServletRequest request) {
		HashMap result = new HashMap();
		HttpSession session = request.getSession(true);
		CustomerVO customerVO = (CustomerVO) session.getAttribute("CUSTOMER");
		
		//�̸��� ���� �ڵ�� SHA512�� �˰����� Ȱ���Ͽ� 16�ڸ��� ������.
		//������ ��Ʈ�� 2���� �̿��� ���� �ؽ���.
		String email_authcode = SHA.DSHA512(SHA.getSalt(), SHA.getSalt()).substring(0, 16);

		param.put("to", param.get("customer_email"));
		param.put("subject", "�̸��� �����ڵ�");
		param.put("text", "�̸��� �����ڵ�� "+email_authcode+" �Դϴ�.");
		
		try {
			mailService.sendMail(param);
			
			//���ǿ� ������ �̸��� �����ڵ�� �̸��� �ּҸ� ������.
			//�̸��� ������ �ٽ� �̸����� �������� �ʴ� ������ �����ϸ� �ٽ� ������ �õ��ϰԲ� �ؾ���.
			session.setAttribute("email_authcode", email_authcode);
			session.setAttribute("temp_email", param.get("customer_email"));
			
			System.out.println("�����̸���"+param.get("customer_email"));
			
			result.put("flag", "true");
			result.put("content", "�ش� �̸��Ϸ� ������ȣ�� �����߽��ϴ�.");
			return new ResponseEntity<HashMap>(result,HttpStatus.OK);
		}catch(Exception e) {
			e.printStackTrace();
			result.put("flag", "false");
			result.put("content", "������ȣ ���۰������� ������ �߻��߽��ϴ�.");
			return new ResponseEntity<HashMap>(result,HttpStatus.BAD_REQUEST);
		}
	}
}
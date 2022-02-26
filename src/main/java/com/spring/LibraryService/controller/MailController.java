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
	
	
	
	
	//============================================================================================
	//������ Ư�� �̸����ּҷ� �����ϴ� �޼ҵ�.
	//============================================================================================
	private void sendMail(String to, String subject, String text) throws Exception{
		mailService.sendMail(to,subject,text);
	}
	
	
	
	
	//============================================================================================
	//������� �̸����� �����ϱ� ���� �̸��� ���� �ڵ带 �����ϴ� �޼ҵ�.
	//============================================================================================
	@RequestMapping(value="/mail/sendEmailAuthCode.do")
	@ResponseBody
	public HashMap<String,String> sendEmailAuthCode(HttpServletRequest request, HttpServletResponse response) {
		HashMap<String, String> map = new HashMap<String,String>();
		HttpSession session = request.getSession(true);
		CustomerVO customerVO = (CustomerVO) session.getAttribute("CUSTOMER");
		
		//�̸��� ���� �ڵ�� SHA512�� �˰����� Ȱ���Ͽ� 16�ڸ��� ������.
		//������ ��Ʈ�� 2���� �̿��� ���� �ؽ���.
		String EMAIL_AUTHCODE = SHA.DSHA512(SHA.getSalt(), SHA.getSalt()).substring(0, 16);
		String to = request.getParameter("CUSTOMER_EMAIL");
		String subject = "�̸��� �����ڵ�";
		String text = "�̸��� �����ڵ�� "+EMAIL_AUTHCODE+" �Դϴ�.";

		try {
			sendMail(to,subject,text);
			
			//���ǿ� ������ �̸��� �����ڵ�� �̸��� �ּҸ� ������.
			//�̸��� ������ �ٽ� �̸����� �������� �ʴ� ������ �����ϸ� �ٽ� ������ �õ��ϰԲ� �ؾ���.
			session.setAttribute("EMAIL_AUTHCODE", EMAIL_AUTHCODE);
			session.setAttribute("TEMP_EMAIL", to);
			map.put("FLAG", "TRUE");
			map.put("CONTENT", "�ش� �̸��Ϸ� ������ȣ�� �����߽��ϴ�.");
			return map;
		}catch(Exception e) {
			e.printStackTrace();
			map.put("FLAG", "FALSE");
			map.put("CONTENT", "������ȣ ���۰������� ������ �߻��߽��ϴ�.");
			return map;
		}
	}
}
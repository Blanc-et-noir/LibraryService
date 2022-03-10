package com.spring.LibraryService.controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
	//������� �̸����� �����ϱ� ���� �̸��� ���� �ڵ带 �����ϴ� �޼ҵ�.
	//============================================================================================
	@RequestMapping(value="/mail/sendEmailAuthCode.do")
	@ResponseBody
	public HashMap<String,String> sendEmailAuthCode(@RequestParam HashMap param, HttpServletRequest request) {
		HashMap<String, String> map = new HashMap<String,String>();
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
			
			map.put("flag", "true");
			map.put("content", "�ش� �̸��Ϸ� ������ȣ�� �����߽��ϴ�.");
			return map;
		}catch(Exception e) {
			e.printStackTrace();
			map.put("flag", "false");
			map.put("content", "������ȣ ���۰������� ������ �߻��߽��ϴ�.");
			return map;
		}
	}
}
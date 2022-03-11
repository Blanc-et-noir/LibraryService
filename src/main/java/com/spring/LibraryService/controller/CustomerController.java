package com.spring.LibraryService.controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.spring.LibraryService.encrypt.RSA2048;
import com.spring.LibraryService.encrypt.SHA;
import com.spring.LibraryService.service.CustomerServiceInterface;
import com.spring.LibraryService.vo.CustomerVO;

@Controller("customerController")
public class CustomerController {
	@Autowired
	private CustomerServiceInterface customerService;
	
	
	
	
	
	
	//============================================================================================
	//���� ȭ�� �並 �����ϴ� �޼ҵ�.
	//============================================================================================
	@RequestMapping(value= {"/customer/mainForm.do","/*"})
	public ModelAndView mainForm(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("main");
	}
	
	
	
	
	
	
	//============================================================================================
	//�α��� ȭ�� �並 �����ϴ� �޼ҵ�.
	//============================================================================================
	@RequestMapping(value="/customer/loginForm.do")
	public ModelAndView loginForm(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("login");
	}
	
	
	
	
	
	
	//============================================================================================
	//ȸ������ ȭ�� �並 �����ϴ� �޼ҵ�.
	//============================================================================================
	@RequestMapping(value="/customer/joinForm.do")
	public ModelAndView joinForm(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("join");
	}
	
	
	
	
	
	
	//============================================================================================
	//���̵�, ��й�ȣ ã�� ȭ�� �並 �����ϴ� �޼ҵ�.
	//============================================================================================
	@RequestMapping(value="/customer/findForm.do")
	public ModelAndView findForm(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("find");
	}
	
	
	
	
	
	
	//============================================================================================
	//����� ���� ���� ȭ�� �並 �����ϴ� �޼ҵ�
	//============================================================================================
	@RequestMapping(value= {"/customer/infoForm.do"})
	public ModelAndView infoForm(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("info");
	}
	
	
	
	
	
	
	//============================================================================================
	//������� �α��� ��û�� ó���ϴ� �޼ҵ�.
	//============================================================================================
	@RequestMapping(value="/customer/login.do")
	public ResponseEntity<HashMap> login(@RequestParam HashMap param, HttpServletRequest request) {
		HashMap result = new HashMap();
		try {
			HttpSession session = request.getSession();
			param.put("privatekey", session.getAttribute("privatekey"));			
			session.setAttribute("customer", customerService.login(param));
			
			result.put("flag", "true");
			result.put("content", "�α��� ����");
			return new ResponseEntity<HashMap>(result,HttpStatus.OK) ;
		}catch(Exception e) {
			result.put("flag", "false");
			result.put("content", "�α��� ����");
			return new ResponseEntity<HashMap>(result,HttpStatus.BAD_REQUEST);
		}
	}
	
	
	
	
	
	
	//============================================================================================
	//������� �α׾ƿ� ��û�� �����ϴ� �޼ҵ�.
	//============================================================================================
	@RequestMapping(value="/customer/logout.do")
	public ModelAndView logout(HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		session.invalidate();
		return new ModelAndView("main");
	}
	
	
	
	
	
	
	//============================================================================================
	//������� ȸ������ ��û�� ó���ϴ� �޼ҵ�.
	//============================================================================================
	@RequestMapping(value="/customer/join.do")
	public ResponseEntity<HashMap> join(@RequestParam HashMap<String,String> param, HttpServletRequest request) {
		HashMap<String,String> result = new HashMap<String,String>();
		HttpSession session = request.getSession(true);
		CustomerVO customer = (CustomerVO) session.getAttribute("customer");
		
		//���� �̸��� �����ڵ带 �߱޹��� �ʾҰų� �̸��� ������ �Ϸ���� �ʾҴµ� ȸ�������� �õ��Ҷ� �ٽ� ������ ��û.
		if(session.getAttribute("email_authflag")==null || session.getAttribute("temp_email") == null){
			result.put("flag", "false");
			result.put("content", "�̸��� ������ �Ϸ��ؾ� �մϴ�.");
			return new ResponseEntity<HashMap>(result,HttpStatus.BAD_REQUEST);
		//�̸��� ������ �Ϸ�������, �̸��� ������ �ٽ� �ٸ� �������� ���� �̸��Ϸ� ȸ�������� ��û�ϸ� �ٽ� ������ ��û.
		}else if(!((String)session.getAttribute("temp_email")).equals(param.get("customer_email"))){
			result.put("flag", "false");			
			result.put("content", "����� �̸��Ͽ� ���� ������ �ٽ��ؾ� �մϴ�.");
			session.removeAttribute("email_authcode");
			session.removeAttribute("email_authflag");
			return new ResponseEntity<HashMap>(result,HttpStatus.BAD_REQUEST);
		}else {
			try {
				//�̸��� ������ �����ϸ� ����� ��й�ȣ�� ��ȣȭ�� ����ߴ� ����Ű�� �����Ǵ� ���Ű�� ��ȣȭ��.
				String privatekey = (String) session.getAttribute("privatekey");
				
				//��й�ȣ ã�� ������ ���� �� ���� ��ȣȭ �ؾ���.
				String salt = SHA.getSalt();
				String customer_pw = SHA.DSHA512((RSA2048.decrypt(param.get("customer_pw"), privatekey)).replaceAll(" ", ""),salt);
				String password_hint_answer = SHA.DSHA512((RSA2048.decrypt(param.get("password_hint_answer"), privatekey)).replaceAll(" ", ""),salt);
				
				//ȸ�����Կ� �ʿ��� ������ �����ϴ� ��ü.
				param.put("salt", salt);
				param.put("customer_pw", customer_pw);
				param.put("password_hint_answer", password_hint_answer);
				return new ResponseEntity<HashMap>(customerService.join(param),HttpStatus.OK);
			} catch (Exception e) {
				e.printStackTrace();
				result.put("flag", "false");
				result.put("content", "ȸ�����Կ� �����߽��ϴ�.");
				return new ResponseEntity<HashMap>(result,HttpStatus.BAD_REQUEST);
			}
		}
	}
	
	
	
	
	
	
	//============================================================================================
	//����ڰ� ����Ű�� ��û�ϸ� ����Ű�� �߱��ϰ�, �׿� �����Ǵ� ���Ű�� ���ǿ� �����ϴ� �޼ҵ�.
	//============================================================================================
	@RequestMapping(value="/customer/getPublicKey.do")
	public ResponseEntity<HashMap> getPublicKey(HttpServletRequest request) {
		HashMap result = new HashMap();
		try {
			return new ResponseEntity<HashMap>(customerService.getPublicKey(request),HttpStatus.OK);
		}catch(Exception e) {
			e.printStackTrace();
			result.put("flag", "false");
			result.put("content", "����Ű �߱� ����");
			return new ResponseEntity<HashMap>(result,HttpStatus.BAD_REQUEST);
		}
	}
	
	
	
	
	
	
	//============================================================================================
	//ȸ�����Խÿ� ��й�ȣ ã�� �������� HTML, JSP�ڵ忡 �������� �߰��ϴ� ���� �ƴ϶�
	//�������� DB���� ���� ��� �̸� �񵿱�� JSON Ÿ������ ������.
	//============================================================================================
	@RequestMapping(value="/customer/getPasswordQuestionList.do")
	public ResponseEntity<HashMap> getPasswordQuestionList(HttpServletRequest request) {
		HashMap result = new HashMap();
		try {
			result.put("flag", "true");
			result.put("content", "��� �߱� ����");
			result.put("list", customerService.getPasswordQuestionList());
			return new ResponseEntity<HashMap>(result,HttpStatus.OK);
		}catch(Exception e) {
			result.put("flag", "false");
			result.put("content", "��� �߱� ����");
			return new ResponseEntity<HashMap>(result,HttpStatus.BAD_REQUEST);
		}
	}
	
	
	
	
	
	
	//============================================================================================
	//������� ���̵�, ��й�ȣ ���� ã�� �� �ֵ��� ó���ϴ� �޼ҵ�.
	//============================================================================================
	@RequestMapping(value="/customer/find.do")
	public ResponseEntity<HashMap> find(@RequestParam HashMap<String,String> param, HttpServletRequest request) {
		HashMap result = new HashMap();
		HttpSession session = request.getSession(true);
		
		String flag = param.get("flag");
		
		//flag������ ���� ���δٸ� ó���� ������.
		
		if(flag.equals("find_by_phone")) {
			try {
				return customerService.findByPhone(param);
			}catch(Exception e) {
				result.put("flag", "false");
				result.put("content", "ȸ������ ����");
				return new ResponseEntity<HashMap>(result,HttpStatus.BAD_REQUEST);
			}
		}else if(flag.equals("find_by_email")) {
			try {
				return customerService.findByEmail(param);
			}catch(Exception e) {
				result.put("flag", "false");
				result.put("content", "ȸ������ ����");
				return new ResponseEntity<HashMap>(result,HttpStatus.BAD_REQUEST);
			}
		}else if(flag.equals("get_question_button")) {
			try {
				return customerService.getPasswordQuestion(param);
			}catch(Exception e) {
				result.put("flag", "false");
				result.put("content", "ȸ������ ����");
				return new ResponseEntity<HashMap>(result,HttpStatus.BAD_REQUEST);
			}
		}else {
			try {
				return customerService.validateAnswer(param,request);
			}catch(Exception e) {
				result.put("flag", "false");
				result.put("content", "����");
				return new ResponseEntity<HashMap>(result,HttpStatus.BAD_REQUEST);
			}
		}
	}
	
	
	
	
	
	
	//============================================================================================
	//��й�ȣ ���� ��û�� ó���ϴ� �޼ҵ�.
	//============================================================================================
	@RequestMapping(value="/customer/changePassword.do")
	public ResponseEntity<HashMap> changePassword(@RequestParam HashMap<String,String> param, HttpServletRequest request) {
		HashMap result = new HashMap();
		try {
			return customerService.changePassword(param, request);
		}catch(Exception e) {
			e.printStackTrace();
			result.put("flag", "false");
			result.put("content", "ȸ������ ���濡 �����߽��ϴ�.");
			return new ResponseEntity<HashMap>(result,HttpStatus.BAD_REQUEST);
		}
	}
	
	
	
	
	
	
	//============================================================================================
	//��Ÿ ���� ���� ��û�� ó���ϴ� �޼ҵ�.
	//============================================================================================
	@RequestMapping(value="/customer/changeOther.do")
	public ResponseEntity<HashMap> changeOther(@RequestParam HashMap<String,String> param, HttpServletRequest request) {
		HashMap result = new HashMap();
		try {
			return customerService.changeOther(param,request);
		}catch(Exception e) {
			e.printStackTrace();
			result.put("flag", "false");
			result.put("content", "ȸ������ ���濡 �����߽��ϴ�.");
			return new ResponseEntity<HashMap>(result,HttpStatus.BAD_REQUEST);
		}
	}
	
	
	
	
	
	
	//============================================================================================
	//������� �̸��� �����ڵ带 �����ϰ� �̸��� ������ ó���ϴ� �޼ҵ�.
	//============================================================================================
	@RequestMapping(value="/customer/authenticateEmail.do")
	public ResponseEntity<HashMap> authenticateEmail(HttpServletRequest request) {
		HashMap result = new HashMap();
		try {
			return customerService.authenticateEmail(request);
		}catch(Exception e) {
			result.put("flag", "false");
			result.put("content", "�̸��� ���� ����");
			return new ResponseEntity<HashMap>(result,HttpStatus.BAD_REQUEST);
		}
	}
}

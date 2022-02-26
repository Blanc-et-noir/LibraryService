package com.spring.LibraryService.controller;

import java.security.Key;
import java.security.KeyPair;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.spring.LibraryService.encrypt.RSA2048;
import com.spring.LibraryService.service.CustomerService;
import com.spring.LibraryService.vo.CustomerVO;

@Controller("customer")
public class CustomerController {
	@Autowired
	private CustomerService customerService;
	
	
	
	
	
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
	public ModelAndView LOGOFFMAV_loginForm(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("login");
	}
	
	
	
	
	
	//============================================================================================
	//ȸ������ ȭ�� �並 �����ϴ� �޼ҵ�.
	//============================================================================================
	@RequestMapping(value="/customer/joinForm.do")
	public ModelAndView LOGOFFMAV_joinForm(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("join");
	}
	
	
	
	
	
	//============================================================================================
	//���̵�, ��й�ȣ ã�� ȭ�� �並 �����ϴ� �޼ҵ�.
	//============================================================================================
	@RequestMapping(value="/customer/findForm.do")
	public ModelAndView LOGOFFMAV_findForm(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("find");
	}

	
	
	
	
	
	//============================================================================================
	//����� ���� ���� ȭ�� �並 �����ϴ� �޼ҵ�
	//============================================================================================
	@RequestMapping(value= {"/customer/infoForm.do"})
	public ModelAndView LOGONMAV_infoForm(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("info");
	}
	
	
	
	
	
	//============================================================================================
	//������� �α��� ��û�� ó���ϴ� �޼ҵ�.
	//============================================================================================
	@RequestMapping(value="/customer/login.do")
	@ResponseBody
	public HashMap<String,String> LOGOFFMAP_login(HttpServletRequest request, HttpServletResponse response) {
		
		HashMap<String,String> map = new HashMap<String,String>();
		HttpSession session = request.getSession(true);
		CustomerVO customerVO;

		//����� ���ǿ� �Ҵ�� ���Ű�� ������, �ش� ���Ű�� ����ڰ� ��ȣȭ�Ͽ� ������ ��й�ȣ�� ��ȣȭ��.
		String privateKey = (String) session.getAttribute("PRIVATEKEY");
		String TEMP = request.getParameter("CUSTOMER_PW");
		String CUSTOMER_PW = RSA2048.decrypt(TEMP, privateKey);
		
		customerVO = customerService.login(request.getParameter("CUSTOMER_ID"), CUSTOMER_PW);
		
		if(customerVO != null) {
			//�α��ο� �����ϸ� ���ǿ� ����� ��ü�� ������.
			map.put("FLAG", "TRUE");
			map.put("CONTENT", "�α��ο� �����߽��ϴ�.");
			session.setAttribute("CUSTOMER", customerVO);
			return map;
		}else {
			map.put("FLAG", "FALSE");
			map.put("CONTENT", "�α��� ������ ��ġ���� �ʽ��ϴ�.");
			return map;
		}
	}
	
	
	
	
	
	
	//============================================================================================
	//������� �α׾ƿ� ��û�� �����ϴ� �޼ҵ�.
	//============================================================================================
	@RequestMapping(value="/customer/logout.do")
	public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(true);
		session.invalidate();
		return new ModelAndView("main");
	}
	
	
	
	
	
	
	//============================================================================================
	//������� ȸ������ ��û�� ó���ϴ� �޼ҵ�.
	//============================================================================================
	@RequestMapping(value="/customer/join.do")
	@ResponseBody
	public HashMap<String,String> LOGOFFMAP_join(@ModelAttribute CustomerVO joinInfo, HttpServletRequest request, HttpServletResponse response) {
		HashMap<String,String> map = new HashMap<String,String>();
		HttpSession session = request.getSession(true);
		CustomerVO customerVO = (CustomerVO) session.getAttribute("CUSTOMER");
		
		//���� �̸��� �����ڵ带 �߱޹��� �ʾҰų� �̸��� ������ �Ϸ���� �ʾҴµ� ȸ�������� �õ��Ҷ� �ٽ� ������ ��û.
		if(session.getAttribute("EMAIL_AUTHCODE") == null || session.getAttribute("EMAIL_AUTHFLAG")==null || session.getAttribute("TEMP_EMAIL") == null){
			map.put("FLAG", "FALSE");
			map.put("CONTENT", "�̸��� ������ �Ϸ��ؾ� �մϴ�.");
			return map;
		//�̸��� ������ �Ϸ�������, �̸��� ������ �ٽ� �ٸ� �������� ���� �̸��Ϸ� ȸ�������� ��û�ϸ� �ٽ� ������ ��û.
		}else if(!((String)session.getAttribute("TEMP_EMAIL")).equals(joinInfo.getCUSTOMER_EMAIL())){
			map.put("FLAG", "FALSE");
			map.put("CONTENT", "����� �̸��Ͽ� ���� ������ �ٽ��ؾ� �մϴ�.");
			session.removeAttribute("EMAIL_AUTHCODE");
			return map;
		}else {
			//�̸��� ������ �����ϸ� ����� ��й�ȣ�� ��ȣȭ�� ����ߴ� ����Ű�� �����Ǵ� ���Ű�� ��ȣȭ��.
			String privateKey = (String) session.getAttribute("PRIVATEKEY");
			String TEMP = joinInfo.getCUSTOMER_PW();
			String CUSTOMER_PW = RSA2048.decrypt(TEMP, privateKey);
			
			//��й�ȣ ã�� ������ ���� �� ���� ��ȣȭ �ؾ���.
			String PASSWORD_HINT_ANSWER = RSA2048.decrypt(request.getParameter("PASSWORD_HINT_ANSWER"), privateKey);
			String PASSWORD_QUESTION_LIST_ID = request.getParameter("PASSWORD_QUESTION_LIST_ID");
			
			//ȸ�����Կ� �ʿ��� ������ �����ϴ� ��ü.
			joinInfo.setCUSTOMER_PW(CUSTOMER_PW);
			try {
				return map = customerService.join(joinInfo,PASSWORD_QUESTION_LIST_ID,PASSWORD_HINT_ANSWER);
			} catch (Exception e) {
				map.put("FLAG", "FALSE");
				map.put("CONTENT", "ȸ�����Կ� �����߽��ϴ�.");
				return map;
			}
		}
	}
	
	
	
	
	
	
	//============================================================================================
	//����ڰ� ����Ű�� ��û�ϸ� ����Ű�� �߱��ϰ�, �׿� �����Ǵ� ���Ű�� ���ǿ� �����ϴ� �޼ҵ�.
	//============================================================================================
	@RequestMapping(value="/customer/getPublicKey.do")
	@ResponseBody
	public String getPublicKey(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(true);
		
		//RSA2048 Ű ��� ��ü ������, ����Ű�� ���Ű�� ��� ���ڿ��� ��ȯ.
		//��ȯ�� ����Ű, ���Ű�� ���ǿ� �����ϰ� ����Ű�� ������.
		KeyPair keypair = RSA2048.createKey();
		Key privatekey = keypair.getPrivate();
		Key publickey = keypair.getPublic();
		session.setAttribute("PRIVATEKEY", RSA2048.keyToString(privatekey));
		session.setAttribute("PUBLICKEY", RSA2048.keyToString(publickey));
		return RSA2048.keyToString(publickey);
	}
	
	
	
	
	
	
	//============================================================================================
	//ȸ�����Խÿ� ��й�ȣ ã�� �������� HTML, JSP�ڵ忡 �������� �߰��ϴ� ���� �ƴ϶�
	//�������� DB���� ���� ��� �̸� �񵿱�� JSON Ÿ������ ������.
	//============================================================================================
	@RequestMapping(value="/customer/getPasswordQuestionList.do")
	@ResponseBody
	public List getPasswordQuestionList(HttpServletRequest request, HttpServletResponse response) {
		return customerService.getPasswordQuestionList();
	}
	
	
	
	
	
	
	//============================================================================================
	//������� ���̵�, ��й�ȣ ���� ã�� �� �ֵ��� ó���ϴ� �޼ҵ�.
	//============================================================================================
	@RequestMapping(value="/customer/find.do")
	@ResponseBody
	public HashMap<String,String> LOGOFFMAP_find(HttpServletRequest request, HttpServletResponse response) {
		String action = request.getParameter("flag");
		HttpSession session = request.getSession(true);
		
		//flag������ ���� ���δٸ� ó���� ������.
		if(action.equals("FIND_BY_PHONE")) {
			return customerService.findByPhone(request.getParameter("CUSTOMER_PHONE"));
		}else if(action.equals("FIND_BY_EMAIL")) {
			return customerService.findByEmail(request.getParameter("CUSTOMER_EMAIL"));
		}else if(action.equals("GET_QUESTION_BUTTON")) {
			return customerService.getPasswordQuestion(request.getParameter("CUSTOMER_ID"));
		}else {
			//��й�ȣ ã�� ������ ���� ���� ������.
			//��й�ȣ ã�� ������ ���� ���� RSA2048�� ��ȣȭ �Ǿ������Ƿ� ��ȣȭ �ؾ���.
			//��й�ȣ ã�� ������ ���� ���� SHA512�� ��Ʈ�� �Բ� �� �� ������ ���� �ؽ����� �ܹ��� ��ȣȭ�� ��.
			String PRIVATEKEY = (String) session.getAttribute("PRIVATEKEY");
			String PASSWORD_HINT_ANSWER = RSA2048.decrypt(request.getParameter("PASSWORD_HINT_ANSWER"),PRIVATEKEY);
			return customerService.validateAnswer(request.getParameter("CUSTOMER_ID"),PASSWORD_HINT_ANSWER);
		}
	}
	
	
	
	
	
	
	//============================================================================================
	//��й�ȣ ���� ��û�� ó���ϴ� �޼ҵ�.
	//============================================================================================
	@RequestMapping(value="/customer/changePassword.do")
	@ResponseBody
	public HashMap<String,String> LOGONMAP_changePassword(HttpServletRequest request, HttpServletResponse response) {
		HashMap<String, String> map  = new HashMap<String, String>();
		HttpSession session = request.getSession(true);
		CustomerVO customerVO = (CustomerVO) session.getAttribute("CUSTOMER");
		
		//����ڰ� ��ȣȭ�� ����� ����Ű�� �����Ǵ� ���Ű�� ���ǰ�ü���� ����.
		String privateKey = (String) session.getAttribute("PRIVATEKEY");
		
		//���� ������� ��й�ȣ�� �� ��й�ȣ, ��й�ȣ ã�� ������ ���� ���� ���� ��ȣȭ��.
		String CUSTOMER_PW_OLD = RSA2048.decrypt(request.getParameter("CUSTOMER_PW_OLD"), privateKey);
		String CUSTOMER_PW = RSA2048.decrypt(request.getParameter("CUSTOMER_PW"), privateKey);
		String PASSWORD_HINT_ANSWER = RSA2048.decrypt(request.getParameter("PASSWORD_HINT_ANSWER"), privateKey);
		String PASSWORD_QUESTION_LIST_ID = request.getParameter("PASSWORD_QUESTION_LIST_ID");
		String CUSTOMER_ID = customerVO.getCUSTOMER_ID();
		try {
			map =  customerService.changePassword(customerVO,CUSTOMER_PW_OLD,CUSTOMER_PW, PASSWORD_QUESTION_LIST_ID, PASSWORD_HINT_ANSWER);
			//��й�ȣ ���濡 �����ÿ� SALT���� ���ο� ������ �����ؾ���.
			if(map.get("FLAG").equals("TRUE")) {
				String newSalt = map.get("newSalt");
				customerVO.setSALT(map.get("newSalt"));
				customerVO.setCUSTOMER_PW(map.get("newCUSTOMER_PW"));
				session.setAttribute("CUSTOMER", customerVO);
				return map;
			}else {
				return map;
			}
		}catch(Exception e) {
			map.put("FLAG", "FALSE");
			map.put("CONTENT", "��й�ȣ ���濡 �����߽��ϴ�.");
			return map;			
		}
	}
	
	
	
	
	
	
	//============================================================================================
	//��Ÿ ���� ���� ��û�� ó���ϴ� �޼ҵ�.
	//============================================================================================
	@RequestMapping(value="/customer/changeOther.do")
	@ResponseBody
	public HashMap<String,String> LOGONMAP_changeOther(@RequestParam HashMap<String,String> info, HttpServletRequest request, HttpServletResponse response) {
		HashMap<String, String> map = new HashMap<String,String>();
		HttpSession session = request.getSession(true);
		CustomerVO customerVO = (CustomerVO) session.getAttribute("CUSTOMER");
		
		//�̸��� �����ڵ带 �߱޹��� �ʾҰų�, ���� �̸��� ������ ���� ���� ��� �̸��� ������ ��û��.
		if(session.getAttribute("EMAIL_AUTHCODE") == null || session.getAttribute("EMAIL_AUTHFLAG")==null || session.getAttribute("TEMP_EMAIL") == null){
			map.put("FLAG", "FALSE");
			map.put("CONTENT", "�̸��� ������ �Ϸ��ؾ� �մϴ�.");
			return map;
		//�̸��� ������ ���������� �������� ���� �̸��Ϸ� ������ ���� ������ ��û�� ���, �ٽ� ���ο� �̸��Ͽ� ���� ������ ��û��. 
		}else if(!((String)session.getAttribute("TEMP_EMAIL")).equals(info.get("CUSTOMER_EMAIL"))){
			map.put("FLAG", "FALSE");
			map.put("CONTENT", "����� �̸��Ͽ� ���� ������ �ٽ��ؾ� �մϴ�.");
			session.removeAttribute("EMAIL_AUTHCODE");
			return map;
		}else {
			//��Ÿ ���� ���濡 �����ϸ� ���ǿ� ��� ����� ��ü�� ������.
			map =  customerService.changeOther(customerVO,info);
			if(map.get("FLAG").equals("TRUE")) {
				customerVO.setCUSTOMER_NAME(info.get("CUSTOMER_NAME"));
				customerVO.setCUSTOMER_BDATE(info.get("CUSTOMER_BDATE"));
				customerVO.setCUSTOMER_PHONE(info.get("CUSTOMER_PHONE"));
				customerVO.setCUSTOMER_EMAIL(info.get("CUSTOMER_EMAIL"));
				customerVO.setCUSTOMER_ADDRESS(info.get("CUSTOMER_ADDRESS"));
				session.setAttribute("CUSTOMER", customerVO);
				return map;
			}else {
				return map;
			}
		}
	}
	
	
	
	
	
	
	//============================================================================================
	//������� �̸��� �����ڵ带 �����ϰ� �̸��� ������ ó���ϴ� �޼ҵ�.
	//============================================================================================
	@RequestMapping(value="/customer/authenticateEmail.do")
	@ResponseBody
	public HashMap<String,String> authenticateEmail(HttpServletRequest request, HttpServletResponse response) {
		HashMap<String, String> map = new HashMap<String,String>();
		HttpSession session = request.getSession(true);
		
		//�̸��� ���� �ڵ带 �߱޹��� �ʾ����� �����ڵ� �߱��� ��û��.
		if(session.getAttribute("EMAIL_AUTHCODE") == null){
			map.put("FLAG", "FALSE");
			map.put("CONTENT", "�̸��� �����ڵ带 �߱޹޾ƾ� �մϴ�.");
			return map;
		}else {
			
			String EMAIL_AUTHCODE = (String) session.getAttribute("EMAIL_AUTHCODE");
			String EMAIL_AUTHCODE_CHECK = request.getParameter("EMAIL_AUTHCODE");
			
			//�Է��� �̸��� �ּҷ� ������ �̸��� ���� �ڵ�� ����ڰ� �Է��� �ڵ尡 ��ġ�� ��쿡�� �̸��� ���� ����.
			if(EMAIL_AUTHCODE!=null&&EMAIL_AUTHCODE_CHECK!=null&&EMAIL_AUTHCODE.equals(EMAIL_AUTHCODE_CHECK)) {
				map.put("FLAG", "TRUE");
				map.put("CONTENT", "�̸��� ������ �����߽��ϴ�.");
				session.setAttribute("EMAIL_AUTHFLAG", "TRUE");
				return map;
			}else {
				map.put("FLAG", "FALSE");
				map.put("CONTENT", "�̸��� ������ �����߽��ϴ�.");
				return map;
			}
		}
	}
}

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
	@RequestMapping(value= {"/customer/mainForm.do","/*"})
	public ModelAndView mainForm(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("main");
	}
	@RequestMapping(value="/customer/loginForm.do")
	public ModelAndView LOGOFFMAV_loginForm(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("login");
	}
	@RequestMapping(value="/customer/joinForm.do")
	public ModelAndView LOGOFFMAV_joinForm(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("join");
	}
	@RequestMapping(value="/customer/findForm.do")
	public ModelAndView LOGOFFMAV_findForm(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("find");
	}

	@RequestMapping(value= {"/customer/infoForm.do"})
	public ModelAndView LOGONMAV_infoForm(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("info");
	}
	
	
	@RequestMapping(value="/customer/login.do")
	@ResponseBody
	public HashMap<String,String> LOGOFFMAP_login(HttpServletRequest request, HttpServletResponse response) {
		
		HashMap<String,String> map = new HashMap<String,String>();
		HttpSession session = request.getSession(true);
		CustomerVO customerVO;

		String privateKey = (String) session.getAttribute("PRIVATEKEY");
		String TEMP = request.getParameter("CUSTOMER_PW");
		String CUSTOMER_PW = RSA2048.decrypt(TEMP, privateKey);
			
		customerVO = customerService.login(request.getParameter("CUSTOMER_ID"), CUSTOMER_PW);
			
		if(customerVO != null) {
			map.put("FLAG", "TRUE");
			map.put("CONTENT", "로그인에 성공했습니다.");
			session.setAttribute("CUSTOMER", customerVO);
			return map;
		}else {
			map.put("FLAG", "FALSE");
			map.put("CONTENT", "로그인 정보가 일치하지 않습니다.");
			return map;
		}
	}
	
	@RequestMapping(value="/customer/logout.do")
	public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(true);
		session.invalidate();
		return new ModelAndView("main");
	}
	
	@RequestMapping(value="/customer/join.do")
	@ResponseBody
	public HashMap<String,String> LOGOFFMAP_join(@ModelAttribute CustomerVO joinInfo, HttpServletRequest request, HttpServletResponse response) {
		HashMap<String,String> map = new HashMap<String,String>();
		HttpSession session = request.getSession(true);
		CustomerVO customerVO = (CustomerVO) session.getAttribute("CUSTOMER");
		if(session.getAttribute("EMAIL_AUTHCODE") == null || session.getAttribute("EMAIL_AUTHFLAG")==null || session.getAttribute("TEMP_EMAIL") == null){
			map.put("FLAG", "FALSE");
			map.put("CONTENT", "이메일 인증을 완료해야 합니다.");
			return map;
		}else if(!((String)session.getAttribute("TEMP_EMAIL")).equals(joinInfo.getCUSTOMER_EMAIL())){
			map.put("FLAG", "FALSE");
			map.put("CONTENT", "변경된 이메일에 대한 인증을 다시해야 합니다.");
			session.removeAttribute("EMAIL_AUTHCODE");
			return map;
		}else {
			String privateKey = (String) session.getAttribute("PRIVATEKEY");
			String TEMP = joinInfo.getCUSTOMER_PW();
			String CUSTOMER_PW = RSA2048.decrypt(TEMP, privateKey);
			
			String PASSWORD_HINT_ANSWER = RSA2048.decrypt(request.getParameter("PASSWORD_HINT_ANSWER"), privateKey);
			String PASSWORD_QUESTION_LIST_ID = request.getParameter("PASSWORD_QUESTION_LIST_ID");
			
			joinInfo.setCUSTOMER_PW(CUSTOMER_PW);
			try {
				return map = customerService.join(joinInfo,PASSWORD_QUESTION_LIST_ID,PASSWORD_HINT_ANSWER);
			} catch (Exception e) {
				map.put("FLAG", "FALSE");
				map.put("CONTENT", "회원가입에 실패했습니다.");
				return map;
			}
		}
	}
	
	@RequestMapping(value="/customer/getPublicKey.do")
	@ResponseBody
	public String getPublicKey(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(true);
		KeyPair keypair = RSA2048.createKey();
		Key privatekey = keypair.getPrivate();
		Key publickey = keypair.getPublic();
		session.setAttribute("PRIVATEKEY", RSA2048.keyToString(privatekey));
		session.setAttribute("PUBLICKEY", RSA2048.keyToString(publickey));
		return RSA2048.keyToString(publickey);
	}
	
	@RequestMapping(value="/customer/getPasswordQuestionList.do")
	@ResponseBody
	public List getPasswordQuestionList(HttpServletRequest request, HttpServletResponse response) {
		return customerService.getPasswordQuestionList();
	}
	
	@RequestMapping(value="/customer/find.do")
	@ResponseBody
	public HashMap<String,String> LOGOFFMAP_find(HttpServletRequest request, HttpServletResponse response) {
		String action = request.getParameter("flag");
		HttpSession session = request.getSession(true);
		if(action.equals("FIND_BY_PHONE")) {
			return customerService.findByPhone(request.getParameter("CUSTOMER_PHONE"));
		}else if(action.equals("FIND_BY_EMAIL")) {
			return customerService.findByEmail(request.getParameter("CUSTOMER_EMAIL"));
		}else if(action.equals("GET_QUESTION_BUTTON")) {
			return customerService.getPasswordQuestion(request.getParameter("CUSTOMER_ID"));
		}else {
			String PRIVATEKEY = (String) session.getAttribute("PRIVATEKEY");
			String PASSWORD_HINT_ANSWER = RSA2048.decrypt(request.getParameter("PASSWORD_HINT_ANSWER"),PRIVATEKEY);
			return customerService.validateAnswer(request.getParameter("CUSTOMER_ID"),PASSWORD_HINT_ANSWER);
		}
	}
	
	@RequestMapping(value="/customer/changePassword.do")
	@ResponseBody
	public HashMap<String,String> LOGONMAP_changePassword(HttpServletRequest request, HttpServletResponse response) {
		HashMap<String, String> map  = new HashMap<String, String>();
		HttpSession session = request.getSession(true);
		CustomerVO customerVO = (CustomerVO) session.getAttribute("CUSTOMER");
		String privateKey = (String) session.getAttribute("PRIVATEKEY");
		String CUSTOMER_PW_OLD = RSA2048.decrypt(request.getParameter("CUSTOMER_PW_OLD"), privateKey);
		String CUSTOMER_PW = RSA2048.decrypt(request.getParameter("CUSTOMER_PW"), privateKey);
		String PASSWORD_HINT_ANSWER = RSA2048.decrypt(request.getParameter("PASSWORD_HINT_ANSWER"), privateKey);
		String PASSWORD_QUESTION_LIST_ID = request.getParameter("PASSWORD_QUESTION_LIST_ID");
		String CUSTOMER_ID = customerVO.getCUSTOMER_ID();
		try {
			map =  customerService.changePassword(customerVO,CUSTOMER_PW_OLD,CUSTOMER_PW, PASSWORD_QUESTION_LIST_ID, PASSWORD_HINT_ANSWER);
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
			map.put("CONTENT", "비밀번호 변경에 실패했습니다.");
			return map;			
		}
	}
	
	@RequestMapping(value="/customer/changeOther.do")
	@ResponseBody
	public HashMap<String,String> LOGONMAP_changeOther(@RequestParam HashMap<String,String> info, HttpServletRequest request, HttpServletResponse response) {
		HashMap<String, String> map = new HashMap<String,String>();
		HttpSession session = request.getSession(true);
		CustomerVO customerVO = (CustomerVO) session.getAttribute("CUSTOMER");
		if(session.getAttribute("EMAIL_AUTHCODE") == null || session.getAttribute("EMAIL_AUTHFLAG")==null || session.getAttribute("TEMP_EMAIL") == null){
			map.put("FLAG", "FALSE");
			map.put("CONTENT", "이메일 인증을 완료해야 합니다.");
			return map;
		}else if(!((String)session.getAttribute("TEMP_EMAIL")).equals(info.get("CUSTOMER_EMAIL"))){
			map.put("FLAG", "FALSE");
			map.put("CONTENT", "변경된 이메일에 대한 인증을 다시해야 합니다.");
			session.removeAttribute("EMAIL_AUTHCODE");
			return map;
		}else {
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
	
	@RequestMapping(value="/customer/authenticateEmail.do")
	@ResponseBody
	public HashMap<String,String> authenticateEmail(HttpServletRequest request, HttpServletResponse response) {
		HashMap<String, String> map = new HashMap<String,String>();
		HttpSession session = request.getSession(true);
		if(session.getAttribute("EMAIL_AUTHCODE") == null){
			map.put("FLAG", "FALSE");
			map.put("CONTENT", "이메일 인증코드를 발급받아야 합니다.");
			return map;
		}else {
			String EMAIL_AUTHCODE = (String) session.getAttribute("EMAIL_AUTHCODE");
			String EMAIL_AUTHCODE_CHECK = request.getParameter("EMAIL_AUTHCODE");
			if(EMAIL_AUTHCODE!=null&&EMAIL_AUTHCODE_CHECK!=null&&EMAIL_AUTHCODE.equals(EMAIL_AUTHCODE_CHECK)) {
				map.put("FLAG", "TRUE");
				map.put("CONTENT", "이메일 인증에 성공했습니다.");
				session.setAttribute("EMAIL_AUTHFLAG", "TRUE");
				return map;
			}else {
				map.put("FLAG", "FALSE");
				map.put("CONTENT", "이메일 인증에 실패했습니다.");
				return map;
			}
		}
	}
}

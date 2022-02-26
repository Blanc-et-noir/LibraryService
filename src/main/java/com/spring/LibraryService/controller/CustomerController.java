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
	//메인 화면 뷰를 리턴하는 메소드.
	//============================================================================================
	@RequestMapping(value= {"/customer/mainForm.do","/*"})
	public ModelAndView mainForm(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("main");
	}
	
	
	
	
	
	//============================================================================================
	//로그인 화면 뷰를 리턴하는 메소드.
	//============================================================================================
	@RequestMapping(value="/customer/loginForm.do")
	public ModelAndView LOGOFFMAV_loginForm(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("login");
	}
	
	
	
	
	
	//============================================================================================
	//회원가입 화면 뷰를 리턴하는 메소드.
	//============================================================================================
	@RequestMapping(value="/customer/joinForm.do")
	public ModelAndView LOGOFFMAV_joinForm(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("join");
	}
	
	
	
	
	
	//============================================================================================
	//아이디, 비밀번호 찾기 화면 뷰를 리턴하는 메소드.
	//============================================================================================
	@RequestMapping(value="/customer/findForm.do")
	public ModelAndView LOGOFFMAV_findForm(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("find");
	}

	
	
	
	
	
	//============================================================================================
	//사용자 정보 변경 화면 뷰를 리턴하는 메소드
	//============================================================================================
	@RequestMapping(value= {"/customer/infoForm.do"})
	public ModelAndView LOGONMAV_infoForm(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("info");
	}
	
	
	
	
	
	//============================================================================================
	//사용자의 로그인 요청을 처리하는 메소드.
	//============================================================================================
	@RequestMapping(value="/customer/login.do")
	@ResponseBody
	public HashMap<String,String> LOGOFFMAP_login(HttpServletRequest request, HttpServletResponse response) {
		
		HashMap<String,String> map = new HashMap<String,String>();
		HttpSession session = request.getSession(true);
		CustomerVO customerVO;

		//사용자 세션에 할당된 비밀키를 얻은후, 해당 비밀키로 사용자가 암호화하여 전송한 비밀번호를 복호화함.
		String privateKey = (String) session.getAttribute("PRIVATEKEY");
		String TEMP = request.getParameter("CUSTOMER_PW");
		String CUSTOMER_PW = RSA2048.decrypt(TEMP, privateKey);
		
		customerVO = customerService.login(request.getParameter("CUSTOMER_ID"), CUSTOMER_PW);
		
		if(customerVO != null) {
			//로그인에 성공하면 세션에 사용자 객체를 저장함.
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
	
	
	
	
	
	
	//============================================================================================
	//사용자의 로그아웃 요청을 수행하는 메소드.
	//============================================================================================
	@RequestMapping(value="/customer/logout.do")
	public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(true);
		session.invalidate();
		return new ModelAndView("main");
	}
	
	
	
	
	
	
	//============================================================================================
	//사용자의 회원가입 요청을 처리하는 메소드.
	//============================================================================================
	@RequestMapping(value="/customer/join.do")
	@ResponseBody
	public HashMap<String,String> LOGOFFMAP_join(@ModelAttribute CustomerVO joinInfo, HttpServletRequest request, HttpServletResponse response) {
		HashMap<String,String> map = new HashMap<String,String>();
		HttpSession session = request.getSession(true);
		CustomerVO customerVO = (CustomerVO) session.getAttribute("CUSTOMER");
		
		//아직 이메일 인증코드를 발급받지 않았거나 이메일 인증이 완료되지 않았는데 회원가입을 시도할때 다시 인증을 요청.
		if(session.getAttribute("EMAIL_AUTHCODE") == null || session.getAttribute("EMAIL_AUTHFLAG")==null || session.getAttribute("TEMP_EMAIL") == null){
			map.put("FLAG", "FALSE");
			map.put("CONTENT", "이메일 인증을 완료해야 합니다.");
			return map;
		//이메일 인증은 완료했으나, 이메일 인증후 다시 다른 인증되지 않은 이메일로 회원가입을 신청하면 다시 인증을 요청.
		}else if(!((String)session.getAttribute("TEMP_EMAIL")).equals(joinInfo.getCUSTOMER_EMAIL())){
			map.put("FLAG", "FALSE");
			map.put("CONTENT", "변경된 이메일에 대한 인증을 다시해야 합니다.");
			session.removeAttribute("EMAIL_AUTHCODE");
			return map;
		}else {
			//이메일 인증에 성공하면 사용자 비밀번호를 암호화에 사용했던 공개키와 대응되는 비밀키로 복호화함.
			String privateKey = (String) session.getAttribute("PRIVATEKEY");
			String TEMP = joinInfo.getCUSTOMER_PW();
			String CUSTOMER_PW = RSA2048.decrypt(TEMP, privateKey);
			
			//비밀번호 찾기 질문에 대한 답 또한 복호화 해야함.
			String PASSWORD_HINT_ANSWER = RSA2048.decrypt(request.getParameter("PASSWORD_HINT_ANSWER"), privateKey);
			String PASSWORD_QUESTION_LIST_ID = request.getParameter("PASSWORD_QUESTION_LIST_ID");
			
			//회원가입에 필요한 정보를 저장하는 객체.
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
	
	
	
	
	
	
	//============================================================================================
	//사용자가 공개키를 요청하면 공개키를 발급하고, 그에 대응되는 비밀키는 세션에 저장하는 메소드.
	//============================================================================================
	@RequestMapping(value="/customer/getPublicKey.do")
	@ResponseBody
	public String getPublicKey(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(true);
		
		//RSA2048 키 페어 객체 생성후, 공개키와 비밀키를 얻고 문자열로 변환.
		//변환된 공개키, 비밀키를 세션에 저장하고 공개키만 리턴함.
		KeyPair keypair = RSA2048.createKey();
		Key privatekey = keypair.getPrivate();
		Key publickey = keypair.getPublic();
		session.setAttribute("PRIVATEKEY", RSA2048.keyToString(privatekey));
		session.setAttribute("PUBLICKEY", RSA2048.keyToString(publickey));
		return RSA2048.keyToString(publickey);
	}
	
	
	
	
	
	
	//============================================================================================
	//회원가입시에 비밀번호 찾기 질문들은 HTML, JSP코드에 정적으로 추가하는 것이 아니라
	//동적으로 DB에서 직접 얻고 이를 비동기식 JSON 타입으로 리턴함.
	//============================================================================================
	@RequestMapping(value="/customer/getPasswordQuestionList.do")
	@ResponseBody
	public List getPasswordQuestionList(HttpServletRequest request, HttpServletResponse response) {
		return customerService.getPasswordQuestionList();
	}
	
	
	
	
	
	
	//============================================================================================
	//사용자의 아이디, 비밀번호 등을 찾을 수 있도록 처리하는 메소드.
	//============================================================================================
	@RequestMapping(value="/customer/find.do")
	@ResponseBody
	public HashMap<String,String> LOGOFFMAP_find(HttpServletRequest request, HttpServletResponse response) {
		String action = request.getParameter("flag");
		HttpSession session = request.getSession(true);
		
		//flag변수에 따라 서로다른 처리를 수행함.
		if(action.equals("FIND_BY_PHONE")) {
			return customerService.findByPhone(request.getParameter("CUSTOMER_PHONE"));
		}else if(action.equals("FIND_BY_EMAIL")) {
			return customerService.findByEmail(request.getParameter("CUSTOMER_EMAIL"));
		}else if(action.equals("GET_QUESTION_BUTTON")) {
			return customerService.getPasswordQuestion(request.getParameter("CUSTOMER_ID"));
		}else {
			//비밀번호 찾기 질문에 대한 답을 검증함.
			//비밀번호 찾기 질문에 대한 답은 RSA2048로 암호화 되어있으므로 복호화 해야함.
			//비밀번호 찾기 질문에 대한 답은 SHA512를 솔트와 함께 두 번 적용한 더블 해싱으로 단방향 암호화를 함.
			String PRIVATEKEY = (String) session.getAttribute("PRIVATEKEY");
			String PASSWORD_HINT_ANSWER = RSA2048.decrypt(request.getParameter("PASSWORD_HINT_ANSWER"),PRIVATEKEY);
			return customerService.validateAnswer(request.getParameter("CUSTOMER_ID"),PASSWORD_HINT_ANSWER);
		}
	}
	
	
	
	
	
	
	//============================================================================================
	//비밀번호 변경 요청을 처리하는 메소드.
	//============================================================================================
	@RequestMapping(value="/customer/changePassword.do")
	@ResponseBody
	public HashMap<String,String> LOGONMAP_changePassword(HttpServletRequest request, HttpServletResponse response) {
		HashMap<String, String> map  = new HashMap<String, String>();
		HttpSession session = request.getSession(true);
		CustomerVO customerVO = (CustomerVO) session.getAttribute("CUSTOMER");
		
		//사용자가 암호화에 사용한 공개키에 대응되는 비밀키를 세션객체에서 얻음.
		String privateKey = (String) session.getAttribute("PRIVATEKEY");
		
		//기존 사용자의 비밀번호와 새 비밀번호, 비밀번호 찾기 질문에 대한 답을 각각 복호화함.
		String CUSTOMER_PW_OLD = RSA2048.decrypt(request.getParameter("CUSTOMER_PW_OLD"), privateKey);
		String CUSTOMER_PW = RSA2048.decrypt(request.getParameter("CUSTOMER_PW"), privateKey);
		String PASSWORD_HINT_ANSWER = RSA2048.decrypt(request.getParameter("PASSWORD_HINT_ANSWER"), privateKey);
		String PASSWORD_QUESTION_LIST_ID = request.getParameter("PASSWORD_QUESTION_LIST_ID");
		String CUSTOMER_ID = customerVO.getCUSTOMER_ID();
		try {
			map =  customerService.changePassword(customerVO,CUSTOMER_PW_OLD,CUSTOMER_PW, PASSWORD_QUESTION_LIST_ID, PASSWORD_HINT_ANSWER);
			//비밀번호 변경에 성공시에 SALT값을 새로운 값으로 변경해야함.
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
	
	
	
	
	
	
	//============================================================================================
	//기타 정보 변경 요청을 처리하는 메소드.
	//============================================================================================
	@RequestMapping(value="/customer/changeOther.do")
	@ResponseBody
	public HashMap<String,String> LOGONMAP_changeOther(@RequestParam HashMap<String,String> info, HttpServletRequest request, HttpServletResponse response) {
		HashMap<String, String> map = new HashMap<String,String>();
		HttpSession session = request.getSession(true);
		CustomerVO customerVO = (CustomerVO) session.getAttribute("CUSTOMER");
		
		//이메일 인증코드를 발급받지 않았거나, 아직 이메일 인증이 되지 않은 경우 이메일 인증을 요청함.
		if(session.getAttribute("EMAIL_AUTHCODE") == null || session.getAttribute("EMAIL_AUTHFLAG")==null || session.getAttribute("TEMP_EMAIL") == null){
			map.put("FLAG", "FALSE");
			map.put("CONTENT", "이메일 인증을 완료해야 합니다.");
			return map;
		//이메일 인증에 성공했으나 인증되지 않은 이메일로 변경후 정보 변경을 요청한 경우, 다시 새로운 이메일에 대한 인증을 요청함. 
		}else if(!((String)session.getAttribute("TEMP_EMAIL")).equals(info.get("CUSTOMER_EMAIL"))){
			map.put("FLAG", "FALSE");
			map.put("CONTENT", "변경된 이메일에 대한 인증을 다시해야 합니다.");
			session.removeAttribute("EMAIL_AUTHCODE");
			return map;
		}else {
			//기타 정보 변경에 성공하면 세션에 담긴 사용자 객체도 갱신함.
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
	//사용자의 이메일 인증코드를 검증하고 이메일 인증을 처리하는 메소드.
	//============================================================================================
	@RequestMapping(value="/customer/authenticateEmail.do")
	@ResponseBody
	public HashMap<String,String> authenticateEmail(HttpServletRequest request, HttpServletResponse response) {
		HashMap<String, String> map = new HashMap<String,String>();
		HttpSession session = request.getSession(true);
		
		//이메일 인증 코드를 발급받지 않았으면 인증코드 발급을 요청함.
		if(session.getAttribute("EMAIL_AUTHCODE") == null){
			map.put("FLAG", "FALSE");
			map.put("CONTENT", "이메일 인증코드를 발급받아야 합니다.");
			return map;
		}else {
			
			String EMAIL_AUTHCODE = (String) session.getAttribute("EMAIL_AUTHCODE");
			String EMAIL_AUTHCODE_CHECK = request.getParameter("EMAIL_AUTHCODE");
			
			//입력한 이메일 주소로 전송한 이메일 인증 코드와 사용자가 입력한 코드가 일치한 경우에는 이메일 인증 성공.
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

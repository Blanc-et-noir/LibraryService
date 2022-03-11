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
	public ModelAndView loginForm(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("login");
	}
	
	
	
	
	
	
	//============================================================================================
	//회원가입 화면 뷰를 리턴하는 메소드.
	//============================================================================================
	@RequestMapping(value="/customer/joinForm.do")
	public ModelAndView joinForm(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("join");
	}
	
	
	
	
	
	
	//============================================================================================
	//아이디, 비밀번호 찾기 화면 뷰를 리턴하는 메소드.
	//============================================================================================
	@RequestMapping(value="/customer/findForm.do")
	public ModelAndView findForm(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("find");
	}
	
	
	
	
	
	
	//============================================================================================
	//사용자 정보 변경 화면 뷰를 리턴하는 메소드
	//============================================================================================
	@RequestMapping(value= {"/customer/infoForm.do"})
	public ModelAndView infoForm(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("info");
	}
	
	
	
	
	
	
	//============================================================================================
	//사용자의 로그인 요청을 처리하는 메소드.
	//============================================================================================
	@RequestMapping(value="/customer/login.do")
	public ResponseEntity<HashMap> login(@RequestParam HashMap param, HttpServletRequest request) {
		HashMap result = new HashMap();
		try {
			HttpSession session = request.getSession();
			param.put("privatekey", session.getAttribute("privatekey"));			
			session.setAttribute("customer", customerService.login(param));
			
			result.put("flag", "true");
			result.put("content", "로그인 성공");
			return new ResponseEntity<HashMap>(result,HttpStatus.OK) ;
		}catch(Exception e) {
			result.put("flag", "false");
			result.put("content", "로그인 실패");
			return new ResponseEntity<HashMap>(result,HttpStatus.BAD_REQUEST);
		}
	}
	
	
	
	
	
	
	//============================================================================================
	//사용자의 로그아웃 요청을 수행하는 메소드.
	//============================================================================================
	@RequestMapping(value="/customer/logout.do")
	public ModelAndView logout(HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		session.invalidate();
		return new ModelAndView("main");
	}
	
	
	
	
	
	
	//============================================================================================
	//사용자의 회원가입 요청을 처리하는 메소드.
	//============================================================================================
	@RequestMapping(value="/customer/join.do")
	public ResponseEntity<HashMap> join(@RequestParam HashMap<String,String> param, HttpServletRequest request) {
		HashMap<String,String> result = new HashMap<String,String>();
		HttpSession session = request.getSession(true);
		CustomerVO customer = (CustomerVO) session.getAttribute("customer");
		
		//아직 이메일 인증코드를 발급받지 않았거나 이메일 인증이 완료되지 않았는데 회원가입을 시도할때 다시 인증을 요청.
		if(session.getAttribute("email_authflag")==null || session.getAttribute("temp_email") == null){
			result.put("flag", "false");
			result.put("content", "이메일 인증을 완료해야 합니다.");
			return new ResponseEntity<HashMap>(result,HttpStatus.BAD_REQUEST);
		//이메일 인증은 완료했으나, 이메일 인증후 다시 다른 인증되지 않은 이메일로 회원가입을 신청하면 다시 인증을 요청.
		}else if(!((String)session.getAttribute("temp_email")).equals(param.get("customer_email"))){
			result.put("flag", "false");			
			result.put("content", "변경된 이메일에 대한 인증을 다시해야 합니다.");
			session.removeAttribute("email_authcode");
			session.removeAttribute("email_authflag");
			return new ResponseEntity<HashMap>(result,HttpStatus.BAD_REQUEST);
		}else {
			try {
				//이메일 인증에 성공하면 사용자 비밀번호를 암호화에 사용했던 공개키와 대응되는 비밀키로 복호화함.
				String privatekey = (String) session.getAttribute("privatekey");
				
				//비밀번호 찾기 질문에 대한 답 또한 복호화 해야함.
				String salt = SHA.getSalt();
				String customer_pw = SHA.DSHA512((RSA2048.decrypt(param.get("customer_pw"), privatekey)).replaceAll(" ", ""),salt);
				String password_hint_answer = SHA.DSHA512((RSA2048.decrypt(param.get("password_hint_answer"), privatekey)).replaceAll(" ", ""),salt);
				
				//회원가입에 필요한 정보를 저장하는 객체.
				param.put("salt", salt);
				param.put("customer_pw", customer_pw);
				param.put("password_hint_answer", password_hint_answer);
				return new ResponseEntity<HashMap>(customerService.join(param),HttpStatus.OK);
			} catch (Exception e) {
				e.printStackTrace();
				result.put("flag", "false");
				result.put("content", "회원가입에 실패했습니다.");
				return new ResponseEntity<HashMap>(result,HttpStatus.BAD_REQUEST);
			}
		}
	}
	
	
	
	
	
	
	//============================================================================================
	//사용자가 공개키를 요청하면 공개키를 발급하고, 그에 대응되는 비밀키는 세션에 저장하는 메소드.
	//============================================================================================
	@RequestMapping(value="/customer/getPublicKey.do")
	public ResponseEntity<HashMap> getPublicKey(HttpServletRequest request) {
		HashMap result = new HashMap();
		try {
			return new ResponseEntity<HashMap>(customerService.getPublicKey(request),HttpStatus.OK);
		}catch(Exception e) {
			e.printStackTrace();
			result.put("flag", "false");
			result.put("content", "공개키 발급 실패");
			return new ResponseEntity<HashMap>(result,HttpStatus.BAD_REQUEST);
		}
	}
	
	
	
	
	
	
	//============================================================================================
	//회원가입시에 비밀번호 찾기 질문들은 HTML, JSP코드에 정적으로 추가하는 것이 아니라
	//동적으로 DB에서 직접 얻고 이를 비동기식 JSON 타입으로 리턴함.
	//============================================================================================
	@RequestMapping(value="/customer/getPasswordQuestionList.do")
	public ResponseEntity<HashMap> getPasswordQuestionList(HttpServletRequest request) {
		HashMap result = new HashMap();
		try {
			result.put("flag", "true");
			result.put("content", "목록 발급 성공");
			result.put("list", customerService.getPasswordQuestionList());
			return new ResponseEntity<HashMap>(result,HttpStatus.OK);
		}catch(Exception e) {
			result.put("flag", "false");
			result.put("content", "목록 발급 실패");
			return new ResponseEntity<HashMap>(result,HttpStatus.BAD_REQUEST);
		}
	}
	
	
	
	
	
	
	//============================================================================================
	//사용자의 아이디, 비밀번호 등을 찾을 수 있도록 처리하는 메소드.
	//============================================================================================
	@RequestMapping(value="/customer/find.do")
	public ResponseEntity<HashMap> find(@RequestParam HashMap<String,String> param, HttpServletRequest request) {
		HashMap result = new HashMap();
		HttpSession session = request.getSession(true);
		
		String flag = param.get("flag");
		
		//flag변수에 따라 서로다른 처리를 수행함.
		
		if(flag.equals("find_by_phone")) {
			try {
				return customerService.findByPhone(param);
			}catch(Exception e) {
				result.put("flag", "false");
				result.put("content", "회원정보 없음");
				return new ResponseEntity<HashMap>(result,HttpStatus.BAD_REQUEST);
			}
		}else if(flag.equals("find_by_email")) {
			try {
				return customerService.findByEmail(param);
			}catch(Exception e) {
				result.put("flag", "false");
				result.put("content", "회원정보 없음");
				return new ResponseEntity<HashMap>(result,HttpStatus.BAD_REQUEST);
			}
		}else if(flag.equals("get_question_button")) {
			try {
				return customerService.getPasswordQuestion(param);
			}catch(Exception e) {
				result.put("flag", "false");
				result.put("content", "회원정보 없음");
				return new ResponseEntity<HashMap>(result,HttpStatus.BAD_REQUEST);
			}
		}else {
			try {
				return customerService.validateAnswer(param,request);
			}catch(Exception e) {
				result.put("flag", "false");
				result.put("content", "오답");
				return new ResponseEntity<HashMap>(result,HttpStatus.BAD_REQUEST);
			}
		}
	}
	
	
	
	
	
	
	//============================================================================================
	//비밀번호 변경 요청을 처리하는 메소드.
	//============================================================================================
	@RequestMapping(value="/customer/changePassword.do")
	public ResponseEntity<HashMap> changePassword(@RequestParam HashMap<String,String> param, HttpServletRequest request) {
		HashMap result = new HashMap();
		try {
			return customerService.changePassword(param, request);
		}catch(Exception e) {
			e.printStackTrace();
			result.put("flag", "false");
			result.put("content", "회원정보 변경에 실패했습니다.");
			return new ResponseEntity<HashMap>(result,HttpStatus.BAD_REQUEST);
		}
	}
	
	
	
	
	
	
	//============================================================================================
	//기타 정보 변경 요청을 처리하는 메소드.
	//============================================================================================
	@RequestMapping(value="/customer/changeOther.do")
	public ResponseEntity<HashMap> changeOther(@RequestParam HashMap<String,String> param, HttpServletRequest request) {
		HashMap result = new HashMap();
		try {
			return customerService.changeOther(param,request);
		}catch(Exception e) {
			e.printStackTrace();
			result.put("flag", "false");
			result.put("content", "회원정보 변경에 실패했습니다.");
			return new ResponseEntity<HashMap>(result,HttpStatus.BAD_REQUEST);
		}
	}
	
	
	
	
	
	
	//============================================================================================
	//사용자의 이메일 인증코드를 검증하고 이메일 인증을 처리하는 메소드.
	//============================================================================================
	@RequestMapping(value="/customer/authenticateEmail.do")
	public ResponseEntity<HashMap> authenticateEmail(HttpServletRequest request) {
		HashMap result = new HashMap();
		try {
			return customerService.authenticateEmail(request);
		}catch(Exception e) {
			result.put("flag", "false");
			result.put("content", "이메일 인증 실패");
			return new ResponseEntity<HashMap>(result,HttpStatus.BAD_REQUEST);
		}
	}
}

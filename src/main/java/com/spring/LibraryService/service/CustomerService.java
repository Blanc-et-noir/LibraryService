package com.spring.LibraryService.service;

import java.security.Key;
import java.security.KeyPair;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.spring.LibraryService.dao.CustomerDAOInterface;
import com.spring.LibraryService.encrypt.RSA2048;
import com.spring.LibraryService.encrypt.SHA;
import com.spring.LibraryService.vo.CustomerVO;

@Service("customerService")
@Transactional(propagation=Propagation.REQUIRED, rollbackFor= {Exception.class})
public class CustomerService implements CustomerServiceInterface{
	@Autowired
	private CustomerDAOInterface customerDAO;
	@Autowired
	private MailService mailService;
	
	
	
	
	
	
	//============================================================================================
	//사용자의 로그인 요청을 처리하는 메소드.
	//============================================================================================
	public CustomerVO login(HashMap<String,String> param) throws Exception{
		//솔트 값을 이용하여 사용자의 비밀번호를 SHA512로 더블 해싱하고 이를 저장함.
		//단방향 해싱이므로 복호화가 불가능하게끔 저장함.
		String privatekey = param.get("privatekey");
		String salt = customerDAO.getSalt(param);
		String customer_pw = param.get("customer_pw");
		
		param.put("customer_pw", SHA.DSHA512(RSA2048.decrypt(customer_pw, privatekey), salt));
		return customerDAO.login(param);
	}
	
	
	
	
	
	
	//============================================================================================
	//사용자의 회원가입 요청을 처리하는 메소드.
	//============================================================================================
	public HashMap<String, String> join(HashMap<String,String> param) throws Exception{
		HashMap<String,String> result = new HashMap<String,String>();
		//회원가입하려는 회원 정보를 새로 추가함, 비밀번호 찾기 질문 또한 같이 저장함.
		customerDAO.join(param);
		customerDAO.insertPasswordHint(param);
		result.put("flag", "true");
		result.put("content", "회원가입에 성공했습니다.");
		return result;
		//customerDAO.ERROR();
	}
	
	
	
	
	
	
	//============================================================================================
	//비밀번호 찾기 질문들을 리스트의 형태로 동적으로 반환하는 메소드.
	//============================================================================================
	public List getPasswordQuestionList() throws Exception{
		return customerDAO.getPasswordQuestionList();
	}
	
	
	
	
	
	
	//============================================================================================
	//가입한 전화번호로 사용자의 ID를 조회하는 요청을 처리하는 메소드.
	//============================================================================================
	public ResponseEntity<HashMap> findByPhone(HashMap<String,String> param) throws Exception{
		HashMap result = new HashMap();
		result.put("flag", "true");
		result.put("content", "아이디 조회 성공");
		result.put("password_question", customerDAO.findByPhone(param));
		return new ResponseEntity<HashMap>(result,HttpStatus.OK);
	}
	

	
	
	
	
	//============================================================================================
	//가입한 이메일로 사용자의 ID를 조회하는 요청을 처리하는 메소드.
	//============================================================================================
	public ResponseEntity<HashMap> findByEmail(HashMap<String,String> param) throws Exception{
		HashMap result = new HashMap();
		result.put("flag", "true");
		result.put("content", "아이디 조회 성공");
		result.put("password_question", customerDAO.findByEmail(param));
		return new ResponseEntity<HashMap>(result,HttpStatus.OK);
	}
	
	
	
	
	
	
	//============================================================================================
	//사용자가 설정한 자신의 비밀번호 찾기 질문을 반환하는 메소드.
	//============================================================================================
	public ResponseEntity<HashMap> getPasswordQuestion(HashMap<String,String> param) throws Exception{
		HashMap result = new HashMap();
		result.put("flag", "true");
		result.put("content", "질문 조회 성공");
		result.put("password_question", customerDAO.getPasswordQuestion(param));
		return new ResponseEntity<HashMap>(result,HttpStatus.OK);
	}
	
	
	
	
	
	
	//============================================================================================
	//비밀번호 찾기 질문에 대한 답을 검증하는 요청을 처리하는 메소드.
	//============================================================================================
	public ResponseEntity<HashMap> validateAnswer(HashMap<String,String> param) throws Exception{
		HashMap result = new HashMap();
		
		//사용자의 솔트값을 이용해 공백이 제거된 비밀번호 찾기 질문에 대한 답을 SHA512로 더블 해싱함.
		//해싱된 결과와 DB에 저장된 비밀번호 찾기 질문에 대한 답이 일치해야 검증에 성공.
		String salt = customerDAO.getSalt(param);

		param.put("password_hint_answer", SHA.DSHA512(param.get("password_hint_answer").replace(" ", ""), salt));

		//검증에 성공하면 무작위 솔트 값 2개를 이용하여 더블 해싱한 결과 16자리를 임시 비밀번호로써 사용함. 
		String customer_pw = SHA.DSHA512(SHA.getSalt(),SHA.getSalt()).substring(0,16);
		String customer_id = param.get("customer_id");

		customerDAO.validateAnswer(param);
		//해당 임시 비밀번호로 사용자의 비밀번호를 변경하고, 솔트값을 새로 설정함.
		//사용자가 가입할때 사용한 이메일 주소로 임시 비밀번호를 전달함.
		
		param.put("customer_pw", SHA.DSHA512(customer_pw, salt));
		param.put("salt", salt);
		
		customerDAO.changePassword(param);
		
		param.put("to", param.get("customer_email"));
		param.put("subject", "임시 비밀번호");
		param.put("text", customer_id+"에 대한 임시 비밀번호는 "+customer_pw+" 입니다.");
		
		//mailService.sendMail(param);
		
		result.put("flag", "true");
		result.put("content", param.get("customer_email")+"으로 임시비밀번호를 전송했습니다.");
		return new ResponseEntity<HashMap>(result,HttpStatus.OK); 
	}
	
	
	
	
	
	
	//============================================================================================
	//비밀번호를 변경하는 요청을 처리하는 메소드.
	//============================================================================================
	public ResponseEntity<HashMap> changePassword(HashMap<String,String> param, HttpServletRequest request) throws Exception{
		HashMap result = new HashMap();
		HttpSession session = request.getSession(true);
		CustomerVO customerVO = (CustomerVO) session.getAttribute("customer");
		
		//솔트 값을 얻어서 해당 솔트 값으로 기존 비밀번호를 SHA512로 더블 해싱한 결과가 DB에 저장된 더블 해싱된 비밀번호와 일치해야 비밀번호 변경함. 
		String salt = customerVO.getSalt();
		param.put("customer_pw", SHA.DSHA512(param.get("customer_pw_old"), salt));
		customerDAO.validatePassword(param);
		
		String newSalt = SHA.getSalt();
		HashMap<String,String> map = new HashMap<String,String>();
		
		//비밀번호 검증 성공시 비밀번호와 솔트 값, 비밀번호 찾기 질문과 비밀번호 찾기 질문에 대한 답을 변경함.
		//이때 비밀번호와 비밀번호 찾기 질문에 대한 답은 반드시 단방향 SHA512와 솔트 값으로 더블 해싱하여 저장함.
		//보안상 복호화가 불가능해야함.
		
		param.put("salt", newSalt);
		param.put("customer_pw", SHA.DSHA512(param.get("customer_pw"), newSalt));
		
		customerDAO.changePassword(param);
		
		param.put("password_hint_answer", SHA.DSHA512(param.get("password_hint_answer").replace(" ", ""),newSalt));
		customerDAO.changePasswordHint(param);
		
		session.invalidate();
		
		result.put("flag", "true");
		result.put("content", "비밀번호 변경에 성공했습니다.");
		return new ResponseEntity<HashMap>(result,HttpStatus.OK);
	}
	
	
	
	
	
	
	//============================================================================================
	//기타정보 변경 요청을 처리하는 메소드.
	//============================================================================================
	public ResponseEntity<HashMap> changeOther(HashMap<String,String> param, HttpServletRequest request) throws Exception{
		HashMap result = new HashMap();
		HttpSession session = request.getSession(true);
		
		//이메일 인증코드를 발급받지 않았거나, 아직 이메일 인증이 되지 않은 경우 이메일 인증을 요청함.
		if(session.getAttribute("email_authcode") == null || session.getAttribute("email_authflag") == null || session.getAttribute("temp_email") == null){
			
			result.put("flag", "false");
			result.put("content", "이메일 인증을 완료해야 합니다.");
			return new ResponseEntity<HashMap>(result,HttpStatus.BAD_REQUEST);
			
		//이메일 인증에 성공했으나 인증되지 않은 이메일로 변경후 정보 변경을 요청한 경우, 다시 새로운 이메일에 대한 인증을 요청함. 
		}else if(!((String)session.getAttribute("temp_email")).equals(param.get("customer_email"))){
			
			session.removeAttribute("email_authcode");
			result.put("flag", "false");
			result.put("content", "변경된 이메일에 대한 인증을 다시해야 합니다.");
			return new ResponseEntity<HashMap>(result,HttpStatus.BAD_REQUEST);
		
		}else {

			customerDAO.changeOther(param);
			
			//기타 정보 변경에 성공하면 세션을 강제로 종료함
			session.invalidate();
			
			result.put("flag", "true");
			result.put("content", "회원정보 변경에 성공했습니다.");
			return new ResponseEntity<HashMap>(result,HttpStatus.OK);
		
		}
	}
	
	
	
	
	
	
	//============================================================================================
	//사용자의 이메일 인증코드를 검증하고 이메일 인증을 처리하는 메소드.
	//============================================================================================
	public ResponseEntity<HashMap> authenticateEmail(HttpServletRequest request) throws Exception{
		HashMap result = new HashMap();
		HttpSession session = request.getSession(true);
		
		//이메일 인증 코드를 발급받지 않았으면 인증코드 발급을 요청함.
		if(session.getAttribute("email_authcode") == null){
			result.put("flag", "false");
			result.put("content", "이메일 인증코드를 발급받아야 합니다.");
			return new ResponseEntity<HashMap>(result,HttpStatus.BAD_REQUEST);
		}else {
			
			String email_authcode = (String) session.getAttribute("email_authcode");
			String email_authcode_check = request.getParameter("email_authcode");

			//입력한 이메일 주소로 전송한 이메일 인증 코드와 사용자가 입력한 코드가 일치한 경우에는 이메일 인증 성공.
			if(email_authcode!=null&&email_authcode_check!=null&&email_authcode.equals(email_authcode_check)) {
				result.put("flag", "true");
				result.put("content", "이메일 인증에 성공했습니다.");
				session.setAttribute("email_authflag", "true");
				return new ResponseEntity<HashMap>(result,HttpStatus.OK);
			}else {
				result.put("flag", "false");
				result.put("content", "이메일 인증에 실패했습니다.");
				return new ResponseEntity<HashMap>(result,HttpStatus.BAD_REQUEST);
			}
		}
	}
	
	
	
	
	
	
	
	
	//============================================================================================
	//사용자가 공개키를 요청하면 공개키를 발급하고, 그에 대응되는 비밀키는 세션에 저장하는 메소드.
	//============================================================================================
	public HashMap<String,String> getPublicKey(HttpServletRequest request){
		HashMap result = new HashMap();
		//RSA2048 키 페어 객체 생성후, 공개키와 비밀키를 얻고 문자열로 변환.
		//변환된 공개키, 비밀키를 세션에 저장하고 공개키만 리턴함.
		try {
			HttpSession session = request.getSession(true);
			KeyPair keypair = RSA2048.createKey();
			Key privatekey = keypair.getPrivate();
			Key publickey = keypair.getPublic();
			session.setAttribute("privatekey", RSA2048.keyToString(privatekey));
			session.setAttribute("publickey", RSA2048.keyToString(publickey));
			
			result.put("flag", "true");
			result.put("content", "공개키 발급 성공");
			result.put("publickey", RSA2048.keyToString(publickey));
			return result;
		}catch(Exception e) {
			result.put("flag", "false");
			result.put("content", "공개키 발급 실패");
			return result;
		}
	}
}

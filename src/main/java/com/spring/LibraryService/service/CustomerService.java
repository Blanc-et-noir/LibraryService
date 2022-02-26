package com.spring.LibraryService.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.spring.LibraryService.dao.CustomerDAO;
import com.spring.LibraryService.encrypt.SHA;
import com.spring.LibraryService.vo.CustomerVO;

@Service("customerService")
@Transactional(propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
public class CustomerService {
	@Autowired
	private CustomerDAO customerDAO;
	@Autowired
	private MailService mailService;
	
	
	
	
	
	
	//============================================================================================
	//사용자의 로그인 요청을 처리하는 메소드.
	//============================================================================================
	public CustomerVO login(String CUSTOMER_ID, String CUSTOMER_PW) {
		//솔트 값을 이용하여 사용자의 비밀번호를 SHA512로 더블 해싱하고 이를 저장함.
		//단방향 해싱이므로 복호화가 불가능하게끔 저장함.
		String salt = customerDAO.getSALT(CUSTOMER_ID);
		if(salt == null) {
			return null;
		}else {
			return customerDAO.login(CUSTOMER_ID, SHA.DSHA512(CUSTOMER_PW,salt));
		}
	}
	
	
	
	
	
	
	//============================================================================================
	//사용자의 회원가입 요청을 처리하는 메소드.
	//============================================================================================
	public HashMap<String, String> join(CustomerVO customerVO,String PASSWORD_QUESTION_LIST_ID, String PASSWORD_HINT_ANSWER) throws Exception{
		//회원가입하려는 회원 정보를 새로 추가함, 비밀번호 찾기 질문 또한 같이 저장함.
		customerDAO.join(customerVO);
		customerDAO.insertPasswordHint(customerVO, PASSWORD_QUESTION_LIST_ID, PASSWORD_HINT_ANSWER);
		HashMap<String,String> map = new HashMap<String,String>();
		map.put("FLAG", "TRUE");
		map.put("CONTENT", "회원가입에 성공했습니다.");
		return map;
	}
	
	
	
	
	
	
	//============================================================================================
	//비밀번호 찾기 질문들을 리스트의 형태로 동적으로 반환하는 메소드.
	//============================================================================================
	public List getPasswordQuestionList() {
		return customerDAO.getPasswordQuestionList();
	}
	
	
	
	
	
	
	//============================================================================================
	//가입한 전화번호로 사용자의 ID를 조회하는 요청을 처리하는 메소드.
	//============================================================================================
	public HashMap<String,String> findByPhone(String CUSTOMER_PHONE){
		return customerDAO.findByPhone(CUSTOMER_PHONE);
	}
	
	
	
	
	
	
	//============================================================================================
	//가입한 이메일로 사용자의 ID를 조회하는 요청을 처리하는 메소드.
	//============================================================================================
	public HashMap<String,String> findByEmail(String CUSTOMER_EMAIL){
		return customerDAO.findByEmail(CUSTOMER_EMAIL);
	}
	
	
	
	
	
	
	//============================================================================================
	//사용자가 설정한 자신의 비밀번호 찾기 질문을 반환하는 메소드.
	//============================================================================================
	public HashMap<String,String> getPasswordQuestion(String CUSTOMER_ID){
		return customerDAO.getPasswordQuestion(CUSTOMER_ID);
	}
	
	
	
	
	
	
	//============================================================================================
	//비밀번호 찾기 질문에 대한 답을 검증하는 요청을 처리하는 메소드.
	//============================================================================================
	public HashMap<String,String> validateAnswer(String CUSTOMER_ID, String PASSWORD_HINT_ANSWER){
		//사용자의 솔트값을 이용해 공백이 제거된 비밀번호 찾기 질문에 대한 답을 SHA512로 더블 해싱함.
		//해싱된 결과와 DB에 저장된 비밀번호 찾기 질문에 대한 답이 일치해야 검증에 성공.
		String SALT = customerDAO.getSALT(CUSTOMER_ID);
		PASSWORD_HINT_ANSWER = SHA.DSHA512(PASSWORD_HINT_ANSWER.replace(" ", ""), SALT);
		HashMap<String,String> map = customerDAO.validateAnswer(CUSTOMER_ID,PASSWORD_HINT_ANSWER);
		
		//검증에 성공하면 무작위 솔트 값 2개를 이용하여 더블 해싱한 결과 16자리를 임시 비밀번호로써 사용함. 
		String TEMP = SHA.DSHA512(SHA.getSalt(),SHA.getSalt()).substring(0,16);
		if(map.get("FLAG").equals("TRUE")) {
			try {
				//해당 임시 비밀번호로 사용자의 비밀번호를 변경하고, 솔트값을 새로 설정함.
				//사용자가 가입할때 사용한 이메일 주소로 임시 비밀번호를 전달함.
				customerDAO.changePassword(CUSTOMER_ID,SHA.DSHA512(TEMP, SALT),SALT);
				mailService.sendMail(map.get("CUSTOMER_EMAIL"), "임시 비밀번호", CUSTOMER_ID+"에 대한 임시 비밀번호는 "+TEMP+" 입니다.");
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		return map; 
	}
	
	
	
	
	
	
	//============================================================================================
	//비밀번호를 변경하는 요청을 처리하는 메소드.
	//============================================================================================
	public HashMap<String,String> changePassword(CustomerVO customerVO, String CUSTOMER_PW_OLD, String CUSTOMER_PW, String PASSWORD_QUESTION_LIST_ID, String PASSWORD_HINT_ANSWER) throws Exception{
		//솔트 값을 얻어서 해당 솔트 값으로 기존 비밀번호를 SHA512로 더블 해싱한 결과가 DB에 저장된 더블 해싱된 비밀번호와 일치해야 비밀번호 변경함. 
		String salt = customerVO.getSALT();
		boolean flag = customerDAO.validatePassword(customerVO.getCUSTOMER_ID(), SHA.DSHA512(CUSTOMER_PW_OLD, salt));
		String newSalt = SHA.getSalt();
		HashMap<String,String> map = new HashMap<String,String>();
		if(flag) {
			//비밀번호 검증 성공시 비밀번호와 솔트 값, 비밀번호 찾기 질문과 비밀번호 찾기 질문에 대한 답을 변경함.
			//이때 비밀번호와 비밀번호 찾기 질문에 대한 답은 반드시 단방향 SHA512와 솔트 값으로 더블 해싱하여 저장함.
			//보안상 복호화가 불가능해야함.
			customerDAO.changePassword(customerVO.getCUSTOMER_ID(), SHA.DSHA512(CUSTOMER_PW, newSalt), newSalt);
			customerDAO.changePasswordHint(customerVO.getCUSTOMER_ID(), PASSWORD_QUESTION_LIST_ID, SHA.DSHA512(PASSWORD_HINT_ANSWER.replace(" ", ""), newSalt));
			
			//변경 성공시에 현재 세션에 저장된 사용자 객체를 갱신하기 위해 해당 정보를 리턴함.
			map.put("FLAG", "TRUE");
			map.put("CONTENT", "비밀번호 변경에 성공했습니다.");
			map.put("newSalt", newSalt);
			map.put("newCUSTOMER_PW", SHA.DSHA512(CUSTOMER_PW, newSalt));
		}else {
			map.put("FLAG", "FALSE");
			map.put("CONTENT", "비밀번호 변경에 실패했습니다.");
		}
		return map;
	}
	
	
	
	
	
	
	//============================================================================================
	//기타정보 변경 요청을 처리하는 메소드.
	//============================================================================================
	public HashMap<String,String> changeOther(CustomerVO customerVO, HashMap<String,String> info){
		return customerDAO.changeOther(customerVO,info);
	}
}

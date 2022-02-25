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
	
	public CustomerVO login(String CUSTOMER_ID, String CUSTOMER_PW) {
		String salt = customerDAO.getSALT(CUSTOMER_ID);
		if(salt == null) {
			return null;
		}else {
			return customerDAO.login(CUSTOMER_ID, SHA.DSHA512(CUSTOMER_PW,salt));
		}
	}
	public HashMap<String, String> join(CustomerVO customerVO,String PASSWORD_QUESTION_LIST_ID, String PASSWORD_HINT_ANSWER) throws Exception{
		customerDAO.join(customerVO);
		customerDAO.insertPasswordHint(customerVO, PASSWORD_QUESTION_LIST_ID, PASSWORD_HINT_ANSWER);
		HashMap<String,String> map = new HashMap<String,String>();
		map.put("FLAG", "TRUE");
		map.put("CONTENT", "회원가입에 성공했습니다.");
		return map;
	}
	public List getPasswordQuestionList() {
		return customerDAO.getPasswordQuestionList();
	}
	public HashMap<String,String> findByPhone(String CUSTOMER_PHONE){
		return customerDAO.findByPhone(CUSTOMER_PHONE);
	}
	public HashMap<String,String> findByEmail(String CUSTOMER_EMAIL){
		return customerDAO.findByEmail(CUSTOMER_EMAIL);
	}
	public HashMap<String,String> getPasswordQuestion(String CUSTOMER_ID){
		return customerDAO.getPasswordQuestion(CUSTOMER_ID);
	}
	
	public HashMap<String,String> validateAnswer(String CUSTOMER_ID, String PASSWORD_HINT_ANSWER){
		String SALT = customerDAO.getSALT(CUSTOMER_ID);
		PASSWORD_HINT_ANSWER = SHA.DSHA512(PASSWORD_HINT_ANSWER.replace(" ", ""), SALT);
		HashMap<String,String> map = customerDAO.validateAnswer(CUSTOMER_ID,PASSWORD_HINT_ANSWER);
		String TEMP = SHA.DSHA512(SHA.getSalt(),SHA.getSalt()).substring(0,16);
		if(map.get("FLAG").equals("TRUE")) {
			try {
				customerDAO.changePassword(CUSTOMER_ID,SHA.DSHA512(TEMP, SALT),SALT);
				mailService.sendMail(map.get("CUSTOMER_EMAIL"), "임시 비밀번호", CUSTOMER_ID+"에 대한 임시 비밀번호는 "+TEMP+" 입니다.");
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		return map; 
	}
	
	public HashMap<String,String> changePassword(CustomerVO customerVO, String CUSTOMER_PW_OLD, String CUSTOMER_PW, String PASSWORD_QUESTION_LIST_ID, String PASSWORD_HINT_ANSWER) throws Exception{
		String salt = customerVO.getSALT();
		boolean flag = customerDAO.validatePassword(customerVO.getCUSTOMER_ID(), SHA.DSHA512(CUSTOMER_PW_OLD, salt));
		String newSalt = SHA.getSalt();
		HashMap<String,String> map = new HashMap<String,String>();
		if(flag) {
			customerDAO.changePassword(customerVO.getCUSTOMER_ID(), SHA.DSHA512(CUSTOMER_PW, newSalt), newSalt);
			customerDAO.changePasswordHint(customerVO.getCUSTOMER_ID(), PASSWORD_QUESTION_LIST_ID, SHA.DSHA512(PASSWORD_HINT_ANSWER.replace(" ", ""), newSalt), newSalt);
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
	
	public HashMap<String,String> changeOther(CustomerVO customerVO, HashMap<String,String> info){
		return customerDAO.changeOther(customerVO,info);
	}
}

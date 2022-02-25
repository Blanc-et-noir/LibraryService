package com.spring.LibraryService.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.spring.LibraryService.encrypt.SHA;
import com.spring.LibraryService.service.MailService;
import com.spring.LibraryService.vo.CustomerVO;

@Repository("customerDAO")
public class CustomerDAO {
	@Autowired
	private SqlSession sqlSession;
	public CustomerVO login(String CUSTOMER_ID, String CUSTOMER_PW){
		
		HashMap<String,String> map = new HashMap<String,String>();
		map.put("CUSTOMER_ID", CUSTOMER_ID);
		map.put("CUSTOMER_PW", CUSTOMER_PW);
		CustomerVO customerVO = sqlSession.selectOne("customer.login",map);
		return customerVO;
		
	}
	public String getSALT(String CUSTOMER_ID){
		return  (String) sqlSession.selectOne("customer.getSalt",CUSTOMER_ID);
	}
	public void insertPasswordHint(CustomerVO customerVO, String PASSWORD_QUESTION_LIST_ID, String PASSWORD_HINT_ANSWER) throws Exception{
		HashMap<String,String> map = new HashMap<String,String>();
		map.put("CUSTOMER_ID", customerVO.getCUSTOMER_ID());
		map.put("PASSWORD_QUESTION_LIST_ID", PASSWORD_QUESTION_LIST_ID);
		map.put("PASSWORD_HINT_ANSWER", SHA.DSHA512(PASSWORD_HINT_ANSWER.replace(" ", ""), customerVO.getSALT()));
		if(sqlSession.insert("customer.insertPasswordHint",map)==0) {throw new Exception();}
	}
	public void join(CustomerVO customerVO) throws Exception{
		HashMap<String,String> map = new HashMap<String,String>();
		customerVO.setSALT(SHA.getSalt());
		customerVO.setCUSTOMER_PW(SHA.DSHA512(customerVO.getCUSTOMER_PW(), customerVO.getSALT()));
		if(sqlSession.insert("customer.join",customerVO)==0) {throw new Exception();}
	}
	public HashMap<String,String> findByPhone(String CUSTOMER_PHONE){
		HashMap<String,String> map = new HashMap<String,String>();
		String result = sqlSession.selectOne("customer.findByPhone",CUSTOMER_PHONE);
		if(result != null) {
			map.put("FLAG", "TRUE");
			map.put("CONTENT",CUSTOMER_PHONE+"로 가입한 아이디는 "+result+"입니다.");
			return map;
		}else {
			map.put("FLAG", "FALSE");
			map.put("CONTENT","해당 전화번호로 가입한 사용자 정보가 없습니다.");
			return map;
		}
	}
	public HashMap<String,String> findByEmail(String CUSTOMER_EMAIL){
		HashMap<String,String> map = new HashMap<String,String>();
		String result = sqlSession.selectOne("customer.findByEmail",CUSTOMER_EMAIL);
		if(result != null) {
			map.put("FLAG", "TRUE");
			map.put("CONTENT",CUSTOMER_EMAIL+"로 가입한 아이디는 "+result+"입니다.");
			return map;
		}else {
			map.put("FLAG", "FALSE");
			map.put("CONTENT","해당 이메일로 가입한 사용자 정보가 없습니다.");
			return map;
		}
	}
	public HashMap<String,String> getPasswordQuestion(String CUSTOMER_ID){
		HashMap<String,String> map = new HashMap<String,String>();
		String result = sqlSession.selectOne("customer.getPasswordQuestion",CUSTOMER_ID);
		if(result != null) {
			map.put("FLAG", "TRUE");
			map.put("CONTENT",result);
			return map;
		}else {
			map.put("FLAG", "FALSE");
			map.put("CONTENT","해당 사용자 정보가 없습니다.");
			return map;
		}
	}
	public HashMap<String,String> validateAnswer(String CUSTOMER_ID,String PASSWORD_HINT_ANSWER){
		HashMap<String,String> map = new HashMap<String,String>();
		HashMap<String,String> param = new HashMap<String,String>();
		
		param.put("CUSTOMER_ID", CUSTOMER_ID);
		param.put("PASSWORD_HINT_ANSWER", PASSWORD_HINT_ANSWER);
		
		String result = sqlSession.selectOne("customer.validateAnswer",param);
		
		if(result != null) {
			map.put("FLAG", "TRUE");
			map.put("CONTENT","등록된 이메일로 임시 비밀번호를 전송했습니다.");
			map.put("CUSTOMER_EMAIL", result);
			return map;
		}else {
			map.put("FLAG", "FALSE");
			map.put("CONTENT","해당 사용자 정보가 없습니다.");
			return map;
		}
	}
	public boolean validatePassword(String CUSTOMER_ID, String CUSTOMER_PW_OLD) {
		HashMap<String, String> param = new HashMap<String,String>();
		param.put("CUSTOMER_ID", CUSTOMER_ID);
		param.put("CUSTOMER_PW", CUSTOMER_PW_OLD);
		String result = sqlSession.selectOne("customer.validatePassword",param);
		if(result!=null) {
			return true;
		}else {
			return false;
		}
	}
	public void changePassword(String CUSTOMER_ID, String CUSTOMER_PW, String SALT) throws Exception{
		HashMap<String, String> param = new HashMap<String,String>();
		param.put("CUSTOMER_ID", CUSTOMER_ID);
		param.put("CUSTOMER_PW", CUSTOMER_PW);
		param.put("SALT", SALT);
		if(sqlSession.update("customer.changePassword", param)==0) {throw new Exception();}
	}
	public void changePasswordHint(String CUSTOMER_ID,String PASSWORD_QUESTION_LIST_ID, String PASSWORD_HINT_ANSWER, String SALT) throws Exception{
		HashMap<String, String> param = new HashMap<String,String>();
		param.put("CUSTOMER_ID", CUSTOMER_ID);
		param.put("PASSWORD_QUESTION_LIST_ID", PASSWORD_QUESTION_LIST_ID);
		param.put("PASSWORD_HINT_ANSWER", PASSWORD_HINT_ANSWER);
		param.put("SALT", SALT);
		if(sqlSession.update("customer.changePasswordHint", param)==0) {throw new Exception();}
	}
	public HashMap<String,String> changeOther(CustomerVO customerVO, HashMap<String,String> info) {
		HashMap<String, String> map = new HashMap<String,String>();
		info.put("CUSTOMER_ID", customerVO.getCUSTOMER_ID());
		try {
			int result =  sqlSession.update("customer.changeOther", info);
			if(result!=0) {
				map.put("FLAG", "TRUE");
				map.put("CONTENT", "개인정보 변경에 성공했습니다.");
				return map;
			}else {
				map.put("FLAG", "FALSE");
				map.put("CONTENT", "개인정보 변경에 실패했습니다.");
				return map;
			}
		}catch(Exception e) {
			map.put("FLAG", "FALSE");
			map.put("CONTENT", "아이디 또는 전화번호가 중복되었습니다.");
			return map;
		}
	}
	public List getPasswordQuestionList(){
		return sqlSession.selectList("customer.getPasswordQuestionList");
	}
}

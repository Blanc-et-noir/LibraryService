package com.spring.LibraryService.dao;

import java.util.HashMap;
import java.util.List;

import com.spring.LibraryService.vo.CustomerVO;

public interface CustomerDAOInterface {
	//============================================================================================
	//사용자의 로그인 요청을 처리하는 메소드.
	//============================================================================================
	public CustomerVO login(HashMap<String,String> param) throws Exception;
	
	
	
	
	
	
	//============================================================================================
	//사용자의 솔트값을 반환하는 메소드.
	//============================================================================================
	public String getSalt(HashMap<String,String> param) throws Exception;
	
	
	
	
	
	
	//============================================================================================
	//사용자의 회원가입 요청을 처리하는 메소드.
	//============================================================================================
	public void join(HashMap<String,String> param) throws Exception;
	
	
	
	
	
	//============================================================================================
	//전화번호로 가입한 ID를 찾는 요청을 처리하는 메소드.
	//============================================================================================
	public String findByPhone(HashMap param) throws Exception;
	
	
	
	
	
	
	//============================================================================================
	//이메일로 가입한 ID를 찾는 요청을 처리하는 메소드.
	//============================================================================================
	public String findByEmail(HashMap param) throws Exception;
	
	
	
	
	
	
	//============================================================================================
	//비밀번호 찾기를 할때 자신의 비밀번호 찾기 질문을 얻는 요청을 처리하는 메소드.
	//============================================================================================
	public String getPasswordQuestion(HashMap param) throws Exception;
	
	
	
	
	
	//============================================================================================
	//비밀번호 찾기 질문을 검증하는 요청을 처리하는 메소드.
	//============================================================================================
	public String validateAnswer(HashMap param) throws Exception;
	
	
	
	
	//============================================================================================
	//비밀번호를 변경할때 자신의 현재 비밀번호를 알고있는지 검증하는 요청을 처리하는 메소드.
	//============================================================================================
	public void validatePassword(HashMap param) throws Exception;
	
	
	
	
	
	
	//============================================================================================
	//비밀번호를 변경하는 요청을 처리하는 메소드.
	//============================================================================================
	public void changePasswordOnly(HashMap param) throws Exception;
	
	
	
	
	//============================================================================================
	//비밀번호를 변경하는 요청을 처리하는 메소드.
	//============================================================================================
	public void changePassword(HashMap param) throws Exception;
	
	
	
	
	
	//============================================================================================
	//기타 정보를 변경하는 요청을 처리하는 메소드.
	//============================================================================================
	public void changeOther(HashMap param) throws Exception;
	
	
	
	
	
	
	//============================================================================================
	//비밀번호 찾기 질문을 리스트의 형태로 동적으로 반환하는 메소드.
	//============================================================================================
	public List getPasswordQuestionList();

	
	
	
	
	
	//============================================================================================
	//ID 중복여부 확인
	//============================================================================================
	public void checkID(HashMap param) throws Exception;
	
	
	
	
	
	//============================================================================================
	//전화번호 중복여부 확인
	//============================================================================================
	public void checkPhone(HashMap param) throws Exception;
	
	
	
	
	
	
	//============================================================================================
	//이메일 중복여부 확인
	//============================================================================================
	public void checkEmail(HashMap param) throws Exception;
}

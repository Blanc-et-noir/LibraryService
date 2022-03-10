package com.spring.LibraryService.service;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;

import com.spring.LibraryService.vo.CustomerVO;

public interface CustomerServiceInterface {
	public CustomerVO login(HashMap<String,String> param) throws Exception;
	
	//============================================================================================
	//사용자의 회원가입 요청을 처리하는 메소드.
	//============================================================================================
	public HashMap<String, String> join(HashMap<String,String> param) throws Exception;
	
	
	
	
	
	//============================================================================================
	//비밀번호 찾기 질문들을 리스트의 형태로 동적으로 반환하는 메소드.
	//============================================================================================
	public List getPasswordQuestionList() throws Exception;
	
	
	
	
	//============================================================================================
	//가입한 전화번호로 사용자의 ID를 조회하는 요청을 처리하는 메소드.
	//============================================================================================
	public ResponseEntity<HashMap> findByPhone(HashMap<String,String> param) throws Exception;
	

	
	
	
	
	//============================================================================================
	//가입한 이메일로 사용자의 ID를 조회하는 요청을 처리하는 메소드.
	//============================================================================================
	public ResponseEntity<HashMap> findByEmail(HashMap<String,String> param) throws Exception;
	
	
	
	
	
	
	//============================================================================================
	//사용자가 설정한 자신의 비밀번호 찾기 질문을 반환하는 메소드.
	//============================================================================================
	public ResponseEntity<HashMap> getPasswordQuestion(HashMap<String,String> param) throws Exception;
	
	
	
	
	
	
	//============================================================================================
	//비밀번호 찾기 질문에 대한 답을 검증하는 요청을 처리하는 메소드.
	//============================================================================================
	public ResponseEntity<HashMap> validateAnswer(HashMap<String,String> param) throws Exception;
	
	
	
	
	
	
	//============================================================================================
	//비밀번호를 변경하는 요청을 처리하는 메소드.
	//============================================================================================
	public ResponseEntity<HashMap> changePassword(HashMap<String,String> param, HttpServletRequest request) throws Exception;
	
	
	
	
	
	
	//============================================================================================
	//기타정보 변경 요청을 처리하는 메소드.
	//============================================================================================
	public ResponseEntity<HashMap> changeOther(HashMap<String,String> param, HttpServletRequest request) throws Exception;
	
	
	
	
	
	
	//============================================================================================
	//사용자의 이메일 인증코드를 검증하고 이메일 인증을 처리하는 메소드.
	//============================================================================================
	public ResponseEntity<HashMap> authenticateEmail(HttpServletRequest request) throws Exception;
	
	
	
	
	
	
	//============================================================================================
	//사용자가 공개키를 요청하면 공개키를 발급하고, 그에 대응되는 비밀키는 세션에 저장하는 메소드.
	//============================================================================================
	public HashMap<String,String> getPublicKey(HttpServletRequest request);
}
package com.spring.LibraryService.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.spring.LibraryService.vo.CustomerVO;

@Repository("customerDAO")
public class CustomerDAO implements CustomerDAOInterface{
	@Autowired
	private SqlSession sqlSession;
	
	
	
	
	
	
	//============================================================================================
	//사용자의 로그인 요청을 처리하는 메소드.
	//============================================================================================
	public CustomerVO login(HashMap<String,String> param) throws Exception{
		CustomerVO customerVO = null;
		if((customerVO = sqlSession.selectOne("customer.login",param))==null) {
			throw new Exception();
		}else {
			return customerVO;
		}
	}
	
	
	
	
	
	
	//============================================================================================
	//사용자의 솔트값을 반환하는 메소드.
	//============================================================================================
	public String getSalt(HashMap<String,String> param) throws Exception{
		String salt = null;
		if((salt = sqlSession.selectOne("customer.getSalt",param))==null) {
			throw new Exception();
		}else {
			return salt;
		}
	}
	
	
	
	
	
	
	//============================================================================================
	//사용자의 비밀번호찾기 질문에 대한 정보를 저장하는 메소드.
	//============================================================================================
	public void insertPasswordHint(HashMap<String,String> param) throws Exception{		
		if(sqlSession.insert("customer.insertPasswordHint",param)==0) {
			throw new Exception();
		}
	}
	
	
	
	
	
	
	//============================================================================================
	//사용자의 회원가입 요청을 처리하는 메소드.
	//============================================================================================
	public void join(HashMap<String,String> param) throws Exception{
		if(sqlSession.insert("customer.join",param)==0) {
			throw new Exception();
		}
	}
	
	
	
	
	
	
	//============================================================================================
	//전화번호로 가입한 ID를 찾는 요청을 처리하는 메소드.
	//============================================================================================
	public String findByPhone(HashMap param) throws Exception{
		String result = null;
		if((result = sqlSession.selectOne("customer.findByPhone",param))==null) {
			throw new Exception();
		}else {
			return result;
		}
	}
	
	
	
	
	
	
	//============================================================================================
	//이메일로 가입한 ID를 찾는 요청을 처리하는 메소드.
	//============================================================================================
	public String findByEmail(HashMap param) throws Exception{
		String result = null;
		if((result = sqlSession.selectOne("customer.findByEmail",param))==null) {
			throw new Exception();
		}else {
			return result;
		}
	}
	
	
	
	
	
	
	//============================================================================================
	//비밀번호 찾기를 할때 자신의 비밀번호 찾기 질문을 얻는 요청을 처리하는 메소드.
	//============================================================================================
	public String getPasswordQuestion(HashMap param) throws Exception{
		String result = null;
		if((result = sqlSession.selectOne("customer.getPasswordQuestion",param))==null) {
			throw new Exception();
		}else {
			return result;
		}
	}
	
	
	
	
	
	
	//============================================================================================
	//비밀번호 찾기 질문을 검증하는 요청을 처리하는 메소드.
	//============================================================================================
	public String validateAnswer(HashMap param) throws Exception{
		String customer_email = null;
		if((customer_email=sqlSession.selectOne("customer.validateAnswer",param))==null) {
			throw new Exception();
		}
		return customer_email;
	}
	
	
	
	
	
	
	//============================================================================================
	//비밀번호를 변경할때 자신의 현재 비밀번호를 알고있는지 검증하는 요청을 처리하는 메소드.
	//============================================================================================
	public void validatePassword(HashMap param) throws Exception{
		if(sqlSession.selectOne("customer.validatePassword",param)==null) {
			throw new Exception();
		}
	}
	
	
	
	
	
	
	//============================================================================================
	//비밀번호를 변경하는 요청을 처리하는 메소드.
	//============================================================================================
	public void changePassword(HashMap param) throws Exception{
		if(sqlSession.update("customer.changePassword", param)==0) {
			throw new Exception();
		}
	}
	
	
	
	
	
	
	//============================================================================================
	//비밀번호 찾기 질문과 그에 대한 답을 변경하는 요청을 처리하는 메소드.
	//============================================================================================
	public void changePasswordHint(HashMap param) throws Exception{
		if(sqlSession.update("customer.changePasswordHint", param)==0) {
			throw new Exception();
		}
	}
	
	
	
	
	
	
	//============================================================================================
	//기타 정보를 변경하는 요청을 처리하는 메소드.
	//============================================================================================
	public void changeOther(HashMap param) throws Exception{
		if(sqlSession.update("customer.changeOther", param)==0) {
			throw new Exception();
		}
	}
	
	
	
	
	
	
	//============================================================================================
	//비밀번호 찾기 질문을 리스트의 형태로 동적으로 반환하는 메소드.
	//============================================================================================
	public List getPasswordQuestionList(){
		return sqlSession.selectList("customer.getPasswordQuestionList");
	}
}

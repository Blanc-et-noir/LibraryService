package com.spring.LibraryService.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.spring.LibraryService.exception.customer.DuplicateEmailException;
import com.spring.LibraryService.exception.customer.DuplicateIDException;
import com.spring.LibraryService.exception.customer.DuplicatePhoneException;
import com.spring.LibraryService.exception.customer.InvalidEmailException;
import com.spring.LibraryService.exception.customer.InvalidIDException;
import com.spring.LibraryService.exception.customer.InvalidPasswordException;
import com.spring.LibraryService.exception.customer.InvalidPasswordHintAnswerException;
import com.spring.LibraryService.exception.customer.InvalidPhoneException;
import com.spring.LibraryService.vo.CustomerVO;

@Repository("customerDAO")
public class CustomerDAO implements CustomerDAOInterface{
	@Autowired
	private SqlSession sqlSession;
	
	
	
	
	
	
	//============================================================================================
	//������� �α��� ��û�� ó���ϴ� �޼ҵ�.
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
	//������� ��Ʈ���� ��ȯ�ϴ� �޼ҵ�.
	//============================================================================================
	public String getSalt(HashMap<String,String> param) throws InvalidIDException{
		String salt = null;
		if((salt = sqlSession.selectOne("customer.getSalt",param))==null) {
			throw new InvalidIDException();
		}else {
			return salt;
		}
	}
	
	
	
	
	
	
	//============================================================================================
	//������� ��й�ȣã�� ������ ���� ������ �����ϴ� �޼ҵ�.
	//============================================================================================
	public void insertPasswordHint(HashMap<String,String> param) throws Exception{		
		if(sqlSession.insert("customer.insertPasswordHint",param)==0) {
			throw new Exception();
		}
	}
	
	
	
	
	
	
	//============================================================================================
	//������� ȸ������ ��û�� ó���ϴ� �޼ҵ�.
	//============================================================================================
	public void join(HashMap<String,String> param) throws Exception{
		if(sqlSession.insert("customer.join",param)==0) {
			throw new Exception();
		}
	}
	
	
	
	
	
	
	//============================================================================================
	//��ȭ��ȣ�� ������ ID�� ã�� ��û�� ó���ϴ� �޼ҵ�.
	//============================================================================================
	public String findByPhone(HashMap param) throws InvalidPhoneException{
		String result = null;
		if((result = sqlSession.selectOne("customer.findByPhone",param))==null) {
			throw new InvalidPhoneException();
		}else {
			return result;
		}
	}
	
	
	
	
	
	
	//============================================================================================
	//�̸��Ϸ� ������ ID�� ã�� ��û�� ó���ϴ� �޼ҵ�.
	//============================================================================================
	public String findByEmail(HashMap param) throws InvalidEmailException{
		String result = null;
		if((result = sqlSession.selectOne("customer.findByEmail",param))==null) {
			throw new InvalidEmailException();
		}else {
			return result;
		}
	}
	
	
	
	
	
	
	//============================================================================================
	//��й�ȣ ã�⸦ �Ҷ� �ڽ��� ��й�ȣ ã�� ������ ��� ��û�� ó���ϴ� �޼ҵ�.
	//============================================================================================
	public String getPasswordQuestion(HashMap param) throws InvalidIDException{
		String result = null;
		if((result = sqlSession.selectOne("customer.getPasswordQuestion",param))==null) {
			throw new InvalidIDException();
		}else {
			return result;
		}
	}
	
	
	
	
	
	
	//============================================================================================
	//��й�ȣ ã�� ������ �����ϴ� ��û�� ó���ϴ� �޼ҵ�.
	//============================================================================================
	public String validateAnswer(HashMap param) throws InvalidPasswordHintAnswerException{
		String customer_email = null;
		if((customer_email=sqlSession.selectOne("customer.validateAnswer",param))==null) {
			throw new InvalidPasswordHintAnswerException();
		}
		return customer_email;
	}
	
	
	
	
	
	
	//============================================================================================
	//��й�ȣ�� �����Ҷ� �ڽ��� ���� ��й�ȣ�� �˰��ִ��� �����ϴ� ��û�� ó���ϴ� �޼ҵ�.
	//============================================================================================
	public void validatePassword(HashMap param) throws InvalidPasswordException{
		if(sqlSession.selectOne("customer.validatePassword",param)==null) {
			throw new InvalidPasswordException();
		}
	}
	
	
	
	
	
	
	//============================================================================================
	//��й�ȣ�� �����ϴ� ��û�� ó���ϴ� �޼ҵ�.
	//============================================================================================
	public void changePasswordOnly(HashMap param) throws Exception{
		if(sqlSession.update("customer.changePasswordOnly", param)==0) {
			throw new Exception();
		}
	}
	
	
	
	
	
	//============================================================================================
	//��й�ȣ �� ��Ʈ, ����, ������ �����ϴ� ��û�� ó���ϴ� �޼ҵ�.
	//============================================================================================
	public void changePassword(HashMap param) throws Exception{
		if(sqlSession.update("customer.changePassword", param)==0) {
			throw new InvalidPasswordHintAnswerException();
		}
	}
	
	
	
	
	
	
	//============================================================================================
	//��Ÿ ������ �����ϴ� ��û�� ó���ϴ� �޼ҵ�.
	//============================================================================================
	public void changeOther(HashMap param) throws Exception{
		if(sqlSession.update("customer.changeOther", param)==0) {
			throw new Exception();
		}
	}
	
	
	
	
	
	
	//============================================================================================
	//��й�ȣ ã�� ������ ����Ʈ�� ���·� �������� ��ȯ�ϴ� �޼ҵ�.
	//============================================================================================
	public List getPasswordQuestionList(){
		return sqlSession.selectList("customer.getPasswordQuestionList");
	}
	
	
	
	
	
	
	//============================================================================================
	//ID �ߺ����� Ȯ��
	//============================================================================================
	public void checkID(HashMap param) throws DuplicateIDException{
		String result = null;
		if((result = sqlSession.selectOne("customer.checkID",param))!=null) {
			throw new DuplicateIDException();
		}
	}
	
	
	
	
	
	
	//============================================================================================
	//��ȭ��ȣ �ߺ����� Ȯ��
	//============================================================================================
	public void checkPhone(HashMap param) throws DuplicatePhoneException{
		String result = null;
		if((result = sqlSession.selectOne("customer.checkPhone",param))!=null) {
			throw new DuplicatePhoneException();
		}
	}
	
	
	
	
	
	
	//============================================================================================
	//�̸��� �ߺ����� Ȯ��
	//============================================================================================
	public void checkEmail(HashMap param) throws DuplicateEmailException{
		String result = null;
		if((result = sqlSession.selectOne("customer.checkEmail",param))!=null) {
			throw new DuplicateEmailException();
		}
	}
}

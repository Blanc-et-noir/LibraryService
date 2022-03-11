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
	public String getSalt(HashMap<String,String> param) throws Exception{
		String salt = null;
		if((salt = sqlSession.selectOne("customer.getSalt",param))==null) {
			throw new Exception();
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
	public String findByPhone(HashMap param) throws Exception{
		String result = null;
		if((result = sqlSession.selectOne("customer.findByPhone",param))==null) {
			throw new Exception();
		}else {
			return result;
		}
	}
	
	
	
	
	
	
	//============================================================================================
	//�̸��Ϸ� ������ ID�� ã�� ��û�� ó���ϴ� �޼ҵ�.
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
	//��й�ȣ ã�⸦ �Ҷ� �ڽ��� ��й�ȣ ã�� ������ ��� ��û�� ó���ϴ� �޼ҵ�.
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
	//��й�ȣ ã�� ������ �����ϴ� ��û�� ó���ϴ� �޼ҵ�.
	//============================================================================================
	public String validateAnswer(HashMap param) throws Exception{
		String customer_email = null;
		if((customer_email=sqlSession.selectOne("customer.validateAnswer",param))==null) {
			throw new Exception();
		}
		return customer_email;
	}
	
	
	
	
	
	
	//============================================================================================
	//��й�ȣ�� �����Ҷ� �ڽ��� ���� ��й�ȣ�� �˰��ִ��� �����ϴ� ��û�� ó���ϴ� �޼ҵ�.
	//============================================================================================
	public void validatePassword(HashMap param) throws Exception{
		if(sqlSession.selectOne("customer.validatePassword",param)==null) {
			throw new Exception();
		}
	}
	
	
	
	
	
	
	//============================================================================================
	//��й�ȣ�� �����ϴ� ��û�� ó���ϴ� �޼ҵ�.
	//============================================================================================
	public void changePassword(HashMap param) throws Exception{
		if(sqlSession.update("customer.changePassword", param)==0) {
			throw new Exception();
		}
	}
	
	
	
	
	
	
	//============================================================================================
	//��й�ȣ ã�� ������ �׿� ���� ���� �����ϴ� ��û�� ó���ϴ� �޼ҵ�.
	//============================================================================================
	public void changePasswordHint(HashMap param) throws Exception{
		if(sqlSession.update("customer.changePasswordHint", param)==0) {
			throw new Exception();
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
}

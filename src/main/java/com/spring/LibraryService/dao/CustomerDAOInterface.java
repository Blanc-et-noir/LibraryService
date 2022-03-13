package com.spring.LibraryService.dao;

import java.util.HashMap;
import java.util.List;

import com.spring.LibraryService.exception.customer.DuplicateEmailException;
import com.spring.LibraryService.exception.customer.DuplicateIDException;
import com.spring.LibraryService.exception.customer.DuplicatePhoneException;
import com.spring.LibraryService.exception.customer.InvalidEmailException;
import com.spring.LibraryService.exception.customer.InvalidIDException;
import com.spring.LibraryService.exception.customer.InvalidPasswordException;
import com.spring.LibraryService.exception.customer.InvalidPasswordHintAnswerException;
import com.spring.LibraryService.exception.customer.InvalidPhoneException;
import com.spring.LibraryService.vo.CustomerVO;


public interface CustomerDAOInterface{
	//============================================================================================
	//������� �α��� ��û�� ó���ϴ� �޼ҵ�.
	//============================================================================================
	public CustomerVO login(HashMap<String,String> param) throws Exception;
	
	
	
	
	
	
	//============================================================================================
	//������� ��Ʈ���� ��ȯ�ϴ� �޼ҵ�.
	//============================================================================================
	public String getSalt(HashMap<String,String> param) throws InvalidIDException;
	
	
	
	
	
	
	//============================================================================================
	//������� ��й�ȣã�� ������ ���� ������ �����ϴ� �޼ҵ�.
	//============================================================================================
	public void insertPasswordHint(HashMap<String,String> param) throws Exception;
	
	
	
	
	
	
	//============================================================================================
	//������� ȸ������ ��û�� ó���ϴ� �޼ҵ�.
	//============================================================================================
	public void join(HashMap<String,String> param) throws Exception;
	
	
	
	
	
	
	//============================================================================================
	//��ȭ��ȣ�� ������ ID�� ã�� ��û�� ó���ϴ� �޼ҵ�.
	//============================================================================================
	public String findByPhone(HashMap param) throws InvalidPhoneException;
	
	
	
	
	
	
	//============================================================================================
	//�̸��Ϸ� ������ ID�� ã�� ��û�� ó���ϴ� �޼ҵ�.
	//============================================================================================
	public String findByEmail(HashMap param) throws InvalidEmailException;
	
	
	
	
	
	
	//============================================================================================
	//��й�ȣ ã�⸦ �Ҷ� �ڽ��� ��й�ȣ ã�� ������ ��� ��û�� ó���ϴ� �޼ҵ�.
	//============================================================================================
	public String getPasswordQuestion(HashMap param) throws Exception;
	
	
	
	
	
	
	//============================================================================================
	//��й�ȣ ã�� ������ �����ϴ� ��û�� ó���ϴ� �޼ҵ�.
	//============================================================================================
	public String validateAnswer(HashMap param) throws InvalidPasswordHintAnswerException;
	
	
	
	
	
	
	//============================================================================================
	//��й�ȣ�� �����Ҷ� �ڽ��� ���� ��й�ȣ�� �˰��ִ��� �����ϴ� ��û�� ó���ϴ� �޼ҵ�.
	//============================================================================================
	public void validatePassword(HashMap param) throws InvalidPasswordException;
	
	
	
	
	
	
	//============================================================================================
	//��й�ȣ�� �����ϴ� ��û�� ó���ϴ� �޼ҵ�.
	//============================================================================================
	public void changePasswordOnly(HashMap param) throws Exception;
	
	
	
	
	
	//============================================================================================
	//��й�ȣ �� ��Ʈ, ����, ������ �����ϴ� ��û�� ó���ϴ� �޼ҵ�.
	//============================================================================================
	public void changePassword(HashMap param) throws Exception;
	
	
	
	
	
	
	//============================================================================================
	//��Ÿ ������ �����ϴ� ��û�� ó���ϴ� �޼ҵ�.
	//============================================================================================
	public void changeOther(HashMap param) throws Exception;
	
	
	
	
	
	//============================================================================================
	//��й�ȣ ã�� ������ ����Ʈ�� ���·� �������� ��ȯ�ϴ� �޼ҵ�.
	//============================================================================================
	public List getPasswordQuestionList() throws Exception;
	
	
	
	
	
	
	//============================================================================================
	//ID �ߺ����� Ȯ��
	//============================================================================================
	public void checkID(HashMap param) throws DuplicateIDException;
	
	
	
	
	
	
	//============================================================================================
	//��ȭ��ȣ �ߺ����� Ȯ��
	//============================================================================================
	public void checkPhone(HashMap param) throws DuplicatePhoneException;
	
	
	
	
	
	
	//============================================================================================
	//�̸��� �ߺ����� Ȯ��
	//============================================================================================
	public void checkEmail(HashMap param) throws DuplicateEmailException;
}

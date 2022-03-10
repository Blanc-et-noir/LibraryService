package com.spring.LibraryService.service;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;

import com.spring.LibraryService.vo.CustomerVO;

public interface CustomerServiceInterface {
	public CustomerVO login(HashMap<String,String> param) throws Exception;
	
	//============================================================================================
	//������� ȸ������ ��û�� ó���ϴ� �޼ҵ�.
	//============================================================================================
	public HashMap<String, String> join(HashMap<String,String> param) throws Exception;
	
	
	
	
	
	//============================================================================================
	//��й�ȣ ã�� �������� ����Ʈ�� ���·� �������� ��ȯ�ϴ� �޼ҵ�.
	//============================================================================================
	public List getPasswordQuestionList() throws Exception;
	
	
	
	
	//============================================================================================
	//������ ��ȭ��ȣ�� ������� ID�� ��ȸ�ϴ� ��û�� ó���ϴ� �޼ҵ�.
	//============================================================================================
	public ResponseEntity<HashMap> findByPhone(HashMap<String,String> param) throws Exception;
	

	
	
	
	
	//============================================================================================
	//������ �̸��Ϸ� ������� ID�� ��ȸ�ϴ� ��û�� ó���ϴ� �޼ҵ�.
	//============================================================================================
	public ResponseEntity<HashMap> findByEmail(HashMap<String,String> param) throws Exception;
	
	
	
	
	
	
	//============================================================================================
	//����ڰ� ������ �ڽ��� ��й�ȣ ã�� ������ ��ȯ�ϴ� �޼ҵ�.
	//============================================================================================
	public ResponseEntity<HashMap> getPasswordQuestion(HashMap<String,String> param) throws Exception;
	
	
	
	
	
	
	//============================================================================================
	//��й�ȣ ã�� ������ ���� ���� �����ϴ� ��û�� ó���ϴ� �޼ҵ�.
	//============================================================================================
	public ResponseEntity<HashMap> validateAnswer(HashMap<String,String> param) throws Exception;
	
	
	
	
	
	
	//============================================================================================
	//��й�ȣ�� �����ϴ� ��û�� ó���ϴ� �޼ҵ�.
	//============================================================================================
	public ResponseEntity<HashMap> changePassword(HashMap<String,String> param, HttpServletRequest request) throws Exception;
	
	
	
	
	
	
	//============================================================================================
	//��Ÿ���� ���� ��û�� ó���ϴ� �޼ҵ�.
	//============================================================================================
	public ResponseEntity<HashMap> changeOther(HashMap<String,String> param, HttpServletRequest request) throws Exception;
	
	
	
	
	
	
	//============================================================================================
	//������� �̸��� �����ڵ带 �����ϰ� �̸��� ������ ó���ϴ� �޼ҵ�.
	//============================================================================================
	public ResponseEntity<HashMap> authenticateEmail(HttpServletRequest request) throws Exception;
	
	
	
	
	
	
	//============================================================================================
	//����ڰ� ����Ű�� ��û�ϸ� ����Ű�� �߱��ϰ�, �׿� �����Ǵ� ���Ű�� ���ǿ� �����ϴ� �޼ҵ�.
	//============================================================================================
	public HashMap<String,String> getPublicKey(HttpServletRequest request);
}
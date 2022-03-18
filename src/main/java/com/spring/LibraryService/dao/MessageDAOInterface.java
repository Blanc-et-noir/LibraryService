package com.spring.LibraryService.dao;

import java.util.HashMap;
import java.util.List;

import com.spring.LibraryService.exception.customer.InvalidIDException;
import com.spring.LibraryService.vo.MessageVO;

public interface MessageDAOInterface {
	//============================================================================================
	//�۽� �޼����Կ� �޼����� �߰��ϴ� �޼ҵ�.
	//============================================================================================
	public void sendMessageSent(HashMap<String,String> param) throws Exception;
	
	
	
	
	
	//============================================================================================
	//���� �޼����Կ� �޼����� �߰��ϴ� �޼ҵ�.
	//============================================================================================
	public void sendMessageReceived(HashMap<String,String> param) throws Exception;
	
	
	
	
	
	
	//============================================================================================
	//�޼��� ����� �д� ��û�� ó���ϴ� �޼ҵ�.
	//============================================================================================
	public List receiveMessage(HashMap<String,String> param) throws Exception;
	
	
	
	
	
	//============================================================================================
	//�˻����ǿ� �´� �޼������� ������ ��ȯ�ϴ� �޼ҵ�.
	//============================================================================================
	public int getMessageCount(HashMap<String,String> param) throws Exception;
	
	
	
	
	
	
	//============================================================================================
	//�޼������� �����ϴ� ��û�� ó���ϴ� �޼ҵ�.
	//============================================================================================
	public void deleteMessage(HashMap param) throws Exception;
	
	
	
	
	
	
	//============================================================================================
	//�޼����� �д� ��û�� ó���ϴ� �޼ҵ�.
	//============================================================================================
	public MessageVO readMessage(HashMap<String,String> param) throws Exception;





	//============================================================================================
	//ID ���翩�� Ȯ��
	//============================================================================================
	public void checkID(HashMap param) throws InvalidIDException;
}
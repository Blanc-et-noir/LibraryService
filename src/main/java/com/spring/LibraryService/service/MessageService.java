package com.spring.LibraryService.service;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.spring.LibraryService.dao.MessageDAO;
import com.spring.LibraryService.vo.MessageVO;

@Service("messageService")
@Transactional(propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
public class MessageService {
	/*
	 
	 
	 
	 
	@Autowired
	private MessageDAO messageDAO;
	
	
	
	
	
	
	//============================================================================================
	//�޼��� �۽� ��û�� ó���ϴ� �޼ҵ�.
	//============================================================================================
	public HashMap<String,String> sendMessage(HashMap info) throws Exception{
		//�۽� �޼����Կ� �ش� �޼����� �߰���.
		info.put("MESSAGE_BOX", "MESSAGE_SENT");
		messageDAO.sendMessageSent(info);
		
		//���� �޼����Կ� �ش� �޼����� �߰���.
		info.put("MESSAGE_BOX", "MESSAGE_RECEIVED");
		messageDAO.sendMessageReceived(info);
		
		HashMap result = new HashMap();
		
		result.put("FLAG","TRUE");
		result.put("CONTENT", "�޼��� ���ۿ� �����߽��ϴ�.");
		return result;
	}
	
	
	
	
	
	
	//============================================================================================
	//�޼��� ����� �� �޼������� ����� ����Ʈ�� ��ȯ�ϴ� �޼ҵ�.
	//============================================================================================
	public HashMap receiveMessage(HashMap info){
		HashMap result = new HashMap();
		result.put("TOTAL", messageDAO.getMessageCount(info));
		result.put("LIST", messageDAO.receiveMessage(info));
		return result;
	}
	
	
	
	
	
	
	//============================================================================================
	//Ư�� �޼������� �����ϴ� ��û�� ó���ϴ� �޼ҵ�.
	//============================================================================================
	public HashMap deleteMessage(HashMap info) throws Exception{
		HashMap result = new HashMap();
		//���� �޼������� �޼����� �����Ҷ��� �������� ID�� �ڱ� �ڽ��̾����.
		if(info.get("MESSAGE_BOX").equals("MESSAGE_RECEIVED")) {
			info.put("OWNER_ID", "RECEIVER_ID");
		}else {
			//�۽� �޼������� �޼����� �����Ҷ��� �۽����� ID�� �ڱ� �ڽ��̾����.
			info.put("OWNER_ID", "SENDER_ID");
		}
		messageDAO.deleteMessage(info);
		result.put("FLAG", "TRUE");
		result.put("CONTENT", "�޼��� ������ �����߽��ϴ�.");
		return result;
	}
	
	
	
	
	
	
	//============================================================================================
	//�޼��� �б� ��û�� ó���ϴ� �޼ҵ�.
	//============================================================================================
	public MessageVO readMessage(HashMap info) {
		return messageDAO.readMessage(info);
	}
	
	
	
	
	
	*/
}

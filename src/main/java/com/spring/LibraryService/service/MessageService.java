package com.spring.LibraryService.service;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.spring.LibraryService.dao.MessageDAOInterface;
import com.spring.LibraryService.exception.customer.InvalidIDException;
import com.spring.LibraryService.vo.MessageVO;

@Service("messageService")
@Transactional(propagation=Propagation.REQUIRED,rollbackFor={
			Exception.class,
		
			InvalidIDException.class
		}
)
public class MessageService implements MessageServiceInterface{
	@Autowired
	private MessageDAOInterface messageDAO;
	
	
	
	
	
	
	//============================================================================================
	//�޼��� �۽� ��û�� ó���ϴ� �޼ҵ�.
	//============================================================================================
	public void sendMessage(HashMap param) throws InvalidIDException,Exception{
		//������ �����ϴ� ID���� üũ
		messageDAO.checkID(param);
		
		//�۽� �޼����Կ� �ش� �޼����� �߰���.
		param.put("message_box", "message_sent");
		messageDAO.sendMessageSent(param);
		
		//���� �޼����Կ� �ش� �޼����� �߰���.
		param.put("message_box", "message_received");
		messageDAO.sendMessageReceived(param);
	}
	
	
	
	
	
	
	//============================================================================================
	//�޼��� ����� �� �޼������� ����� ����Ʈ�� ��ȯ�ϴ� �޼ҵ�.
	//============================================================================================
	public HashMap receiveMessage(HashMap param) throws Exception{
		HashMap result = new HashMap();
		result.put("total", messageDAO.getMessageCount(param));
		result.put("list", messageDAO.receiveMessage(param));
		return result;
	}
	
	
	
	
	
	
	//============================================================================================
	//Ư�� �޼������� �����ϴ� ��û�� ó���ϴ� �޼ҵ�.
	//============================================================================================
	public void deleteMessage(HashMap param) throws Exception{
		//���� �޼������� �޼����� �����Ҷ��� �������� ID�� �ڱ� �ڽ��̾����.
		if(param.get("message_box").equals("message_received")) {
			param.put("owner_id", "receiver_id");
		}else {
			//�۽� �޼������� �޼����� �����Ҷ��� �۽����� ID�� �ڱ� �ڽ��̾����.
			param.put("owner_id", "sender_id");
		}
		messageDAO.deleteMessage(param);
	}
	
	
	
	
	
	
	//============================================================================================
	//�޼��� �б� ��û�� ó���ϴ� �޼ҵ�.
	//============================================================================================
	public MessageVO readMessage(HashMap<String,String> param) throws Exception{
		if(param.get("message_box").equals("message_received")) {
			param.put("owner_id", "receiver_id");
		}else {
			param.put("owner_id", "sender_id");
		}
		return messageDAO.readMessage(param);
	}
}

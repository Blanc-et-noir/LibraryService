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
	@Autowired
	private MessageDAO messageDAO;
	
	public HashMap<String,String> sendMessage(HashMap info) throws Exception{
		info.put("MESSAGE_BOX", "MESSAGE_SENT");
		messageDAO.sendMessageSent(info);
		
		info.put("MESSAGE_BOX", "MESSAGE_RECEIVED");
		messageDAO.sendMessageReceived(info);
		
		HashMap result = new HashMap();
		
		result.put("FLAG","TRUE");
		result.put("CONTENT", "메세지 전송에 성공했습니다.");
		return result;
	}
	
	public HashMap receiveMessage(HashMap info){
		HashMap result = new HashMap();
		result.put("TOTAL", messageDAO.getMessageCount(info));
		result.put("LIST", messageDAO.receiveMessage(info));
		return result;
	}
	
	public HashMap deleteMessage(HashMap info) throws Exception{
		HashMap result = new HashMap();
		if(info.get("MESSAGE_BOX").equals("MESSAGE_RECEIVED")) {
			info.put("OWNER_ID", "RECEIVER_ID");
		}else {
			info.put("OWNER_ID", "SENDER_ID");
		}
		messageDAO.deleteMessage(info);
		result.put("FLAG", "TRUE");
		result.put("CONTENT", "메세지 삭제에 성공했습니다.");
		return result;
	}
	
	public MessageVO readMessage(HashMap info) {
		return messageDAO.readMessage(info);
	}
}

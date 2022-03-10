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
	//메세지 송신 요청을 처리하는 메소드.
	//============================================================================================
	public HashMap<String,String> sendMessage(HashMap info) throws Exception{
		//송신 메세지함에 해당 메세지를 추가함.
		info.put("MESSAGE_BOX", "MESSAGE_SENT");
		messageDAO.sendMessageSent(info);
		
		//수신 메세지함에 해당 메세지를 추가함.
		info.put("MESSAGE_BOX", "MESSAGE_RECEIVED");
		messageDAO.sendMessageReceived(info);
		
		HashMap result = new HashMap();
		
		result.put("FLAG","TRUE");
		result.put("CONTENT", "메세지 전송에 성공했습니다.");
		return result;
	}
	
	
	
	
	
	
	//============================================================================================
	//메세지 목록을 얻어서 메세지들이 저장된 리스트를 반환하는 메소드.
	//============================================================================================
	public HashMap receiveMessage(HashMap info){
		HashMap result = new HashMap();
		result.put("TOTAL", messageDAO.getMessageCount(info));
		result.put("LIST", messageDAO.receiveMessage(info));
		return result;
	}
	
	
	
	
	
	
	//============================================================================================
	//특정 메세지들을 삭제하는 요청을 처리하는 메소드.
	//============================================================================================
	public HashMap deleteMessage(HashMap info) throws Exception{
		HashMap result = new HashMap();
		//수신 메세지함의 메세지를 삭제할때는 수신자의 ID가 자기 자신이어야함.
		if(info.get("MESSAGE_BOX").equals("MESSAGE_RECEIVED")) {
			info.put("OWNER_ID", "RECEIVER_ID");
		}else {
			//송신 메세지함의 메세지를 삭제할때는 송신자의 ID가 자기 자신이어야함.
			info.put("OWNER_ID", "SENDER_ID");
		}
		messageDAO.deleteMessage(info);
		result.put("FLAG", "TRUE");
		result.put("CONTENT", "메세지 삭제에 성공했습니다.");
		return result;
	}
	
	
	
	
	
	
	//============================================================================================
	//메세지 읽기 요청을 처리하는 메소드.
	//============================================================================================
	public MessageVO readMessage(HashMap info) {
		return messageDAO.readMessage(info);
	}
	
	
	
	
	
	*/
}

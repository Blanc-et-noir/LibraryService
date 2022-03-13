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
	//메세지 송신 요청을 처리하는 메소드.
	//============================================================================================
	public void sendMessage(HashMap param) throws InvalidIDException,Exception{
		//실제로 존재하는 ID인지 체크
		messageDAO.checkID(param);
		
		//송신 메세지함에 해당 메세지를 추가함.
		param.put("message_box", "message_sent");
		messageDAO.sendMessageSent(param);
		
		//수신 메세지함에 해당 메세지를 추가함.
		param.put("message_box", "message_received");
		messageDAO.sendMessageReceived(param);
	}
	
	
	
	
	
	
	//============================================================================================
	//메세지 목록을 얻어서 메세지들이 저장된 리스트를 반환하는 메소드.
	//============================================================================================
	public HashMap receiveMessage(HashMap param) throws Exception{
		HashMap result = new HashMap();
		result.put("total", messageDAO.getMessageCount(param));
		result.put("list", messageDAO.receiveMessage(param));
		return result;
	}
	
	
	
	
	
	
	//============================================================================================
	//특정 메세지들을 삭제하는 요청을 처리하는 메소드.
	//============================================================================================
	public void deleteMessage(HashMap param) throws Exception{
		//수신 메세지함의 메세지를 삭제할때는 수신자의 ID가 자기 자신이어야함.
		if(param.get("message_box").equals("message_received")) {
			param.put("owner_id", "receiver_id");
		}else {
			//송신 메세지함의 메세지를 삭제할때는 송신자의 ID가 자기 자신이어야함.
			param.put("owner_id", "sender_id");
		}
		messageDAO.deleteMessage(param);
	}
	
	
	
	
	
	
	//============================================================================================
	//메세지 읽기 요청을 처리하는 메소드.
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

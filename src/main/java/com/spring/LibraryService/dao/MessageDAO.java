package com.spring.LibraryService.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.spring.LibraryService.exception.customer.InvalidIDException;
import com.spring.LibraryService.vo.MessageVO;

@Repository("messageDAO")
public class MessageDAO implements MessageDAOInterface{
	@Autowired
	private SqlSession sqlSession;
	
	
	
	
	
	
	//============================================================================================
	//송신 메세지함에 메세지를 추가하는 메소드.
	//============================================================================================
	public void sendMessageSent(HashMap<String,String> param) throws Exception{
		if(sqlSession.insert("message.sendMessageSent", param)==0) {
			throw new Exception();
		}
	}
	
	
	
	
	
	//============================================================================================
	//수신 메세지함에 메세지를 추가하는 메소드.
	//============================================================================================
	public void sendMessageReceived(HashMap<String,String> param) throws Exception{
		if(sqlSession.insert("message.sendMessageReceived", param)==0) {
			throw new Exception();
		}
	}
	
	
	
	
	
	
	//============================================================================================
	//메세지 목록을 읽는 요청을 처리하는 메소드.
	//============================================================================================
	public List receiveMessage(HashMap<String,String> param) throws Exception{
		List list = null;
		//수신 메세지함의 메세지를 읽을때는 수신자의 ID가 자기자신이어야함.
		//송신자의 ID는 자신이 검색하고자 하는 ID임.
		if(param.get("message_box").equals("message_received")) {
			param.put("owner_id", "receiver_id");
			param.put("target_id", "sender_id");
		}else {
			//송신 메세지함의 메세지를 읽을때는 송신자의 ID가 자기자신이어야함.
			//수신자의 ID는 자신이 검색하고자 하는 ID임.
			param.put("owner_id", "sender_id");
			param.put("target_id", "receiver_id");
		}
		
		if((list=sqlSession.selectList("message.receiveMessage", param))==null){
			throw new Exception();
		}
		return list;
	}
	
	
	
	
	
	
	//============================================================================================
	//검색조건에 맞는 메세지들의 개수를 반환하는 메소드.
	//============================================================================================
	public int getMessageCount(HashMap<String,String> param) throws Exception{
		int total = 0;
		if(param.get("message_box").equals("message_received")) {
			param.put("owner_id", "receiver_id");
			param.put("target_id", "sender_id");
		}else {
			param.put("owner_id", "sender_id");
			param.put("target_id", "receiver_id");
		}
		total = sqlSession.selectOne("message.getMessageCount",param);
		return total;
	}
	
	
	
	
	
	
	//============================================================================================
	//메세지들을 삭제하는 요청을 처리하는 메소드.
	//============================================================================================
	public void deleteMessage(HashMap param) throws Exception{
		if(sqlSession.delete("message.deleteMessage", param)==0) {
			throw new Exception();
		}
	}
	
	
	
	
	
	
	//============================================================================================
	//메세지를 읽는 요청을 처리하는 메소드.
	//============================================================================================
	public MessageVO readMessage(HashMap<String,String> param) throws Exception{
		MessageVO message = null;
		if((message=sqlSession.selectOne("message.readMessage", param))==null) {
			throw new Exception();
		}
		return message;
	}
	
	
	
	
	
	
	
	//============================================================================================
	//ID 존재여부 확인
	//============================================================================================
	public void checkID(HashMap param) throws InvalidIDException{
		if(sqlSession.selectOne("message.checkID",param)==null) {
			throw new InvalidIDException();
		}
	}
}

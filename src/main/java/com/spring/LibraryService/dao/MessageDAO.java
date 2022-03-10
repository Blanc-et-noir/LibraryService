package com.spring.LibraryService.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.spring.LibraryService.vo.MessageVO;

@Repository("messageDAO")
public class MessageDAO {
	
	/*
	
	
	
	@Autowired
	private SqlSession sqlSession;
	
	
	
	
	
	
	//============================================================================================
	//송신 메세지함에 메세지를 추가하는 메소드.
	//============================================================================================
	public void sendMessageSent(HashMap<String,String> info) throws Exception{
		if(sqlSession.insert("message.sendMessageSent", info)==0) {throw new Exception();}
	}
	
	
	
	
	
	//============================================================================================
	//수신 메세지함에 메세지를 추가하는 메소드.
	//============================================================================================
	public void sendMessageReceived(HashMap<String,String> info) throws Exception{
		if(sqlSession.insert("message.sendMessageReceived", info)==0) {throw new Exception();}
	}
	
	
	
	
	
	
	//============================================================================================
	//메세지 목록을 읽는 요청을 처리하는 메소드.
	//============================================================================================
	public List receiveMessage(HashMap<String,String> info) {
		try {
			//수신 메세지함의 메세지를 읽을때는 수신자의 ID가 자기자신이어야함.
			//송신자의 ID는 자신이 검색하고자 하는 ID임.
			if(info.get("MESSAGE_BOX").equals("MESSAGE_RECEIVED")) {
				info.put("OWNER_ID", "RECEIVER_ID");
				info.put("TARGET_ID", "SENDER_ID");
			}else {
				//송신 메세지함의 메세지를 읽을때는 송신자의 ID가 자기자신이어야함.
				//수신자의 ID는 자신이 검색하고자 하는 ID임.
				info.put("OWNER_ID", "SENDER_ID");
				info.put("TARGET_ID", "RECEIVER_ID");
			}
			List list = sqlSession.selectList("message.receiveMessage", info);
			return list;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	
	
	
	
	//============================================================================================
	//검색조건에 맞는 메세지들의 개수를 반환하는 메소드.
	//============================================================================================
	public int getMessageCount(HashMap<String,String> info) {
		try {
			if(info.get("MESSAGE_BOX").equals("MESSAGE_RECEIVED")) {
				info.put("OWNER_ID", "RECEIVER_ID");
				info.put("TARGET_ID", "SENDER_ID");
			}else {
				info.put("OWNER_ID", "SENDER_ID");
				info.put("TARGET_ID", "RECEIVER_ID");
			}
			return sqlSession.selectOne("message.getMessageCount",info);
		}catch(Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	
	
	
	
	
	//============================================================================================
	//메세지들을 삭제하는 요청을 처리하는 메소드.
	//============================================================================================
	public void deleteMessage(HashMap info) throws Exception{
		if(sqlSession.delete("message.deleteMessage", info)==0) {throw new Exception();}
	}
	
	
	
	
	
	
	//============================================================================================
	//메세지를 읽는 요청을 처리하는 메소드.
	//============================================================================================
	public MessageVO readMessage(HashMap<String,String> info) {
		try {
			if(info.get("MESSAGE_BOX").equals("MESSAGE_RECEIVED")) {
				info.put("OWNER_ID", "RECEIVER_ID");
				return (MessageVO)sqlSession.selectOne("message.readMessage", info);
			}else {
				info.put("OWNER_ID", "SENDER_ID");
				return (MessageVO)sqlSession.selectOne("message.readMessage", info);
			}
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	
	
	
	
	
	*/
}

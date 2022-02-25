package com.spring.LibraryService.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.spring.LibraryService.vo.MessageVO;

@Repository("messageDAO")
public class MessageDAO {
	@Autowired
	private SqlSession sqlSession;
	
	public void sendMessageSent(HashMap<String,String> info) throws Exception{
		if(sqlSession.insert("message.sendMessageSent", info)==0) {throw new Exception();}
	}
	public void sendMessageReceived(HashMap<String,String> info) throws Exception{
		if(sqlSession.insert("message.sendMessageReceived", info)==0) {throw new Exception();}
	}
	
	public List receiveMessage(HashMap<String,String> info) {
		try {
			if(info.get("MESSAGE_BOX").equals("MESSAGE_RECEIVED")) {
				info.put("OWNER_ID", "RECEIVER_ID");
				info.put("TARGET_ID", "SENDER_ID");
			}else {
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
	
	public void deleteMessage(HashMap info) throws Exception{
		if(sqlSession.delete("message.deleteMessage", info)==0) {throw new Exception();}
	}
	
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
}

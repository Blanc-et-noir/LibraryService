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
	//�۽� �޼����Կ� �޼����� �߰��ϴ� �޼ҵ�.
	//============================================================================================
	public void sendMessageSent(HashMap<String,String> param) throws Exception{
		if(sqlSession.insert("message.sendMessageSent", param)==0) {
			throw new Exception();
		}
	}
	
	
	
	
	
	//============================================================================================
	//���� �޼����Կ� �޼����� �߰��ϴ� �޼ҵ�.
	//============================================================================================
	public void sendMessageReceived(HashMap<String,String> param) throws Exception{
		if(sqlSession.insert("message.sendMessageReceived", param)==0) {
			throw new Exception();
		}
	}
	
	
	
	
	
	
	//============================================================================================
	//�޼��� ����� �д� ��û�� ó���ϴ� �޼ҵ�.
	//============================================================================================
	public List receiveMessage(HashMap<String,String> param) throws Exception{
		List list = null;
		//���� �޼������� �޼����� �������� �������� ID�� �ڱ��ڽ��̾����.
		//�۽����� ID�� �ڽ��� �˻��ϰ��� �ϴ� ID��.
		if(param.get("message_box").equals("message_received")) {
			param.put("owner_id", "receiver_id");
			param.put("target_id", "sender_id");
		}else {
			//�۽� �޼������� �޼����� �������� �۽����� ID�� �ڱ��ڽ��̾����.
			//�������� ID�� �ڽ��� �˻��ϰ��� �ϴ� ID��.
			param.put("owner_id", "sender_id");
			param.put("target_id", "receiver_id");
		}
		
		if((list=sqlSession.selectList("message.receiveMessage", param))==null){
			throw new Exception();
		}
		return list;
	}
	
	
	
	
	
	
	//============================================================================================
	//�˻����ǿ� �´� �޼������� ������ ��ȯ�ϴ� �޼ҵ�.
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
	//�޼������� �����ϴ� ��û�� ó���ϴ� �޼ҵ�.
	//============================================================================================
	public void deleteMessage(HashMap param) throws Exception{
		if(sqlSession.delete("message.deleteMessage", param)==0) {
			throw new Exception();
		}
	}
	
	
	
	
	
	
	//============================================================================================
	//�޼����� �д� ��û�� ó���ϴ� �޼ҵ�.
	//============================================================================================
	public MessageVO readMessage(HashMap<String,String> param) throws Exception{
		MessageVO message = null;
		if((message=sqlSession.selectOne("message.readMessage", param))==null) {
			throw new Exception();
		}
		return message;
	}
	
	
	
	
	
	
	
	//============================================================================================
	//ID ���翩�� Ȯ��
	//============================================================================================
	public void checkID(HashMap param) throws InvalidIDException{
		if(sqlSession.selectOne("message.checkID",param)==null) {
			throw new InvalidIDException();
		}
	}
}

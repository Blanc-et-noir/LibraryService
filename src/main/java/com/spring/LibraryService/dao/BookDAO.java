package com.spring.LibraryService.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("bookDAO")
public class BookDAO {
	@Autowired
	private SqlSession sqlSession;
	
	
	
	
	//============================================================================================
	//���ǿ� �´� ������Ȳ������ ������ ��ȯ�ϴ� �޼ҵ�.
	//============================================================================================
	public List getCheckOutList(HashMap param) {
		return sqlSession.selectList("book.getCheckOutList", param);
	}

	
	
	
	
	//============================================================================================
	//Ư�� ����ڰ� ���� ������ ���� �ð��� ��ȯ�ϴ� �޼ҵ�.
	//============================================================================================
	public HashMap checkCheckOutDate(HashMap param) {
		return sqlSession.selectOne("book.checkCheckOutDate", param);
	}
	
	
	
	
	
	//============================================================================================
	//�ش� ������ ����� 1 ���ҽ�Ű�� �޼ҵ�.
	//============================================================================================
	public void decreaseBookNum(HashMap param) throws Exception{
		int num = sqlSession.update("book.decreaseBookNum",param);
		if(num==0) {
			throw new Exception();
		}
	}
	
	
	
	
	
	//============================================================================================
	//�ش� ������ ����� 1 ������Ű�� �޼ҵ�.
	//============================================================================================
	public void increaseBookNum(HashMap param) throws Exception{
		int num = sqlSession.update("book.increaseBookNum",param);
		if(num==0) {
			throw new Exception();
		}
	}
	
	
	
	
	
	//============================================================================================
	//���� ������Ȳ�� �����ϴ� �޼ҵ�.
	//============================================================================================
	public void insertCheckOut(HashMap param) throws Exception{
		int num = sqlSession.insert("book.insertCheckOut",param);
		if(num==0) {
			throw new Exception();
		}
	}
	
	
	
	
	
	//============================================================================================
	//�˻� ���ǿ� �´� ������Ȳ���� ����Ʈ�� ��ȯ�ϴ� �޼ҵ�.
	//============================================================================================
	public List listCheckOuts(HashMap param) {
		return sqlSession.selectList("book.listCheckOuts", param);
	}
	
	
	
	
	
	//============================================================================================
	//Ư�� ������� �̹ݳ� ������Ȳ ���� ������ ��ȯ��.
	//============================================================================================
	public int getCheckOutsCount(HashMap param) {
		return sqlSession.selectOne("book.getCheckOutsCount", param);
	}
	
	
	
	
	
	//============================================================================================
	//Ư�� ������� �̹ݳ� ������Ȳ�߿��� ��ü�� ������ �����ϴ��� ���θ� ��ȯ�ϴ� �޼ҵ�.
	//============================================================================================
	public HashMap isOverdue(HashMap param){
		return sqlSession.selectOne("book.isOverdue", param);
	}
	
	
	
	
	
	//============================================================================================
	//Ư�� ������� ���� ������ ���� �ð��� �����ϴ� �޼ҵ�.
	//Ư�� ��ü�� ������ �ݳ�������, ���� ������ �ð��� ��ü�� ��ŭ �̷�����.
	//============================================================================================
	public void updateCheckOutDate(HashMap param) throws Exception{
		if(sqlSession.update("book.updateCheckOutDate", param)==0) {
			throw new Exception();
		}
	}
	
	
	
	
	
	//============================================================================================
	//�ڽ��� ������ ������ ���� �ݳ��� ó���ϴ� �޼ҵ�.
	//============================================================================================
	public void returnBook(HashMap param) throws Exception{
		if(sqlSession.delete("book.returnBook", param)==0) {
			throw new Exception();
		}
	}
	
	
	
	
	
	//============================================================================================
	//�ڽ��� ���� ��Ȳ�� ���� ���� �ݳ� ������ 7�� �����ϴ� �޼ҵ�, �� ������Ȳ�� 2������ ������.
	//============================================================================================
	public void renewBook(HashMap param) throws Exception{
		if(sqlSession.update("book.renewBook", param)==0) {
			throw new Exception();
		}
	}
	
	
	
	
	
	//============================================================================================
	//���� ������ �������� ���θ� ��ȸ�ϴ� �޼ҵ�.
	//============================================================================================
	public boolean isExtensible(HashMap param) throws Exception{
		Integer num;
		if((num=sqlSession.selectOne("book.isExtensible", param))==null) {
			throw new Exception();
		}else {
			if(num<2) {
				return true;
			}else {
				return false;
			}
		}
	}
	
	
	
	
	
	//============================================================================================
	//��ü�� ���������� �����ϴ� ����ڵ鿡�� ��ü �˸� �޼��� �ϰ����� ��û�� ó���ϴ� �޼ҵ�.
	//============================================================================================
	public void sendMessage(HashMap param) throws Exception{
		sqlSession.insert("book.sendMessage", param);
	}
}
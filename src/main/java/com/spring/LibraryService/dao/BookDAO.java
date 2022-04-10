package com.spring.LibraryService.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.spring.LibraryService.exception.book.ExhaustedRenewCountException;
import com.spring.LibraryService.exception.book.InvalidBookISBNException;
import com.spring.LibraryService.exception.book.InvalidCheckOutIDException;
import com.spring.LibraryService.exception.book.RunOutOfBookNumberException;

@Repository("bookDAO")
public class BookDAO implements BookDAOInterface{
	
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
	public void decreaseBookNum(HashMap param) throws RunOutOfBookNumberException{
		int num = 0;
		if((num=sqlSession.update("book.decreaseBookNum",param))==0) {
			throw new RunOutOfBookNumberException();
		}
	}
	
	
	
	
	
	//============================================================================================
	//�ش� ������ ����� 1 ������Ű�� �޼ҵ�.
	//============================================================================================
	public void increaseBookNum(HashMap param) throws Exception{
		int num = 0;
		if((num=sqlSession.update("book.increaseBookNum",param))==0) {
			throw new Exception();
		}
	}
	
	
	
	
	
	//============================================================================================
	//���� ������Ȳ�� �����ϴ� �޼ҵ�.
	//============================================================================================
	public void insertCheckOut(HashMap param) throws Exception{
		int num = 0;
		if((num=sqlSession.insert("book.insertCheckOut",param))==0) {
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
	public int getCheckOutsCount(HashMap param) throws Exception{
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
	public void returnBook(HashMap param) throws ExhaustedRenewCountException{
		if(sqlSession.delete("book.returnBook", param)==0) {
			throw new ExhaustedRenewCountException();
		}
	}
	
	
	
	
	
	//============================================================================================
	//�ڽ��� ���� ��Ȳ�� ���� ���� �ݳ� ������ 7�� �����ϴ� �޼ҵ�, �� ������Ȳ�� 2������ ������.
	//============================================================================================
	public void renewBookAsAdmin(HashMap param) throws Exception{
		if(sqlSession.update("book.renewBookAsAdmin", param)==0) {
			throw new Exception();
		}
	}
	
	
	
	
	
	//============================================================================================
	//�ڽ��� ���� ��Ȳ�� ���� ���� �ݳ� ������ 7�� �����ϴ� �޼ҵ�, �� ������Ȳ�� 2������ ������.
	//============================================================================================
	public void renewBookAsCustomer(HashMap param) throws Exception{
		if(sqlSession.update("book.renewBookAsCustomer", param)==0) {
			throw new Exception();
		}
	}	
	
	
	
	
	//============================================================================================
	//���� ������ �������� ���θ� ��ȸ�ϴ� �޼ҵ�.
	//DB�� CHECK ������������ �ذ�
	//============================================================================================
	/*
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
	*/
	
	
	
	
	//============================================================================================
	//��ü�� ���������� �����ϴ� ����ڵ鿡�� ��ü �˸� �޼��� �ϰ����� ��û�� ó���ϴ� �޼ҵ�.
	//============================================================================================
	public void sendMessage(HashMap param) throws Exception{
		if(sqlSession.insert("book.sendMessage", param)==0) {
			throw new Exception();
		}
	}
	
	
	
	
	//============================================================================================
	//��ü�� ���������� �����ϴ� ����ڵ鿡�� ��ü �˸� �޼��� �ϰ����� ��û�� ó���ϴ� �޼ҵ�.
	//============================================================================================
	public void checkBookISBN(HashMap param) throws InvalidBookISBNException{
		if(sqlSession.selectOne("book.checkBookISBN", param)==null) {
			throw new InvalidBookISBNException();
		}
	}
	
	
	
	
	
	
	//============================================================================================
	//��ü�� ���������� �����ϴ� ����ڵ鿡�� ��ü �˸� �޼��� �ϰ����� ��û�� ó���ϴ� �޼ҵ�.
	//============================================================================================
	public void checkCheckOutID(HashMap param) throws InvalidCheckOutIDException{
		if(sqlSession.selectOne("book.checkCheckOutID", param)==null) {
			throw new InvalidCheckOutIDException();
		}
	}	
	
	
	
	
	
	//============================================================================================
	//��ü�� ���������� �����ϴ� ����ڵ鿡�� ��ü �˸� �޼��� �ϰ����� ��û�� ó���ϴ� �޼ҵ�.
	//============================================================================================
	public void checkRenewCount(HashMap param) throws ExhaustedRenewCountException{
		int renewCount=0;
		if((renewCount = sqlSession.selectOne("book.checkRenewCount", param))>=2) {
			throw new ExhaustedRenewCountException();
		}
	}
	
	
	
	
	
	
	//============================================================================================
	//��ü�� ���������� �����ϴ� ����ڵ鿡�� ��ü �˸� �޼��� �ϰ����� ��û�� ó���ϴ� �޼ҵ�.
	//============================================================================================
	public void checkBookNumber(HashMap param) throws RunOutOfBookNumberException{
		if((Integer)sqlSession.selectOne("book.checkBookNumber", param)<1) {
			throw new RunOutOfBookNumberException();
		}
	}	
}
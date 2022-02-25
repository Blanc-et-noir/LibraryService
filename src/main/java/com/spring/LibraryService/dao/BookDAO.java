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
	
	public List getCheckOutList(HashMap param) {
		return sqlSession.selectList("book.getCheckOutList", param);
	}

	public HashMap checkCheckOutDate(HashMap param) {
		return sqlSession.selectOne("book.checkCheckOutDate", param);
	}
	
	public void decreaseBookNum(HashMap param) throws Exception{
		int num = sqlSession.update("book.decreaseBookNum",param);
		if(num==0) {
			throw new Exception();
		}
	}
	
	public void increaseBookNum(HashMap param) throws Exception{
		int num = sqlSession.update("book.increaseBookNum",param);
		if(num==0) {
			throw new Exception();
		}
	}
	
	public void insertCheckOut(HashMap param) throws Exception{
		int num = sqlSession.insert("book.insertCheckOut",param);
		if(num==0) {
			throw new Exception();
		}
	}
	
	public List listCheckOuts(HashMap param) {
		return sqlSession.selectList("book.listCheckOuts", param);
	}
	
	public int getCheckOutsCount(HashMap param) {
		return sqlSession.selectOne("book.getCheckOutsCount", param);
	}
	
	public HashMap isOverdue(HashMap param){
		return sqlSession.selectOne("book.isOverdue", param);
	}
	
	public void updateCheckOutDate(HashMap param) throws Exception{
		if(sqlSession.update("book.updateCheckOutDate", param)==0) {
			throw new Exception();
		}
	}
	
	public void returnBook(HashMap param) throws Exception{
		if(sqlSession.delete("book.returnBook", param)==0) {
			throw new Exception();
		}
	}
	
	public void renewBook(HashMap param) throws Exception{
		if(sqlSession.update("book.renewBook", param)==0) {
			throw new Exception();
		}
	}
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
	public void sendMessage(HashMap param) throws Exception{
		sqlSession.insert("book.sendMessage", param);
	}
}
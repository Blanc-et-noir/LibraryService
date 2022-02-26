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
	//조건에 맞는 대출현황정보의 개수를 반환하는 메소드.
	//============================================================================================
	public List getCheckOutList(HashMap param) {
		return sqlSession.selectList("book.getCheckOutList", param);
	}

	
	
	
	
	//============================================================================================
	//특정 사용자가 대출 가능한 기준 시각을 반환하는 메소드.
	//============================================================================================
	public HashMap checkCheckOutDate(HashMap param) {
		return sqlSession.selectOne("book.checkCheckOutDate", param);
	}
	
	
	
	
	
	//============================================================================================
	//해당 도서의 재고량을 1 감소시키는 메소드.
	//============================================================================================
	public void decreaseBookNum(HashMap param) throws Exception{
		int num = sqlSession.update("book.decreaseBookNum",param);
		if(num==0) {
			throw new Exception();
		}
	}
	
	
	
	
	
	//============================================================================================
	//해당 도서의 재고량을 1 증가시키는 메소드.
	//============================================================================================
	public void increaseBookNum(HashMap param) throws Exception{
		int num = sqlSession.update("book.increaseBookNum",param);
		if(num==0) {
			throw new Exception();
		}
	}
	
	
	
	
	
	//============================================================================================
	//도서 대출현황을 삽입하는 메소드.
	//============================================================================================
	public void insertCheckOut(HashMap param) throws Exception{
		int num = sqlSession.insert("book.insertCheckOut",param);
		if(num==0) {
			throw new Exception();
		}
	}
	
	
	
	
	
	//============================================================================================
	//검색 조건에 맞는 대출현황들을 리스트로 반환하는 메소드.
	//============================================================================================
	public List listCheckOuts(HashMap param) {
		return sqlSession.selectList("book.listCheckOuts", param);
	}
	
	
	
	
	
	//============================================================================================
	//특정 사용자의 미반납 대출현황 정보 개수를 반환함.
	//============================================================================================
	public int getCheckOutsCount(HashMap param) {
		return sqlSession.selectOne("book.getCheckOutsCount", param);
	}
	
	
	
	
	
	//============================================================================================
	//특정 사용자의 미반납 대출현황중에서 연체된 정보가 존재하는지 여부를 반환하는 메소드.
	//============================================================================================
	public HashMap isOverdue(HashMap param){
		return sqlSession.selectOne("book.isOverdue", param);
	}
	
	
	
	
	
	//============================================================================================
	//특정 사용자의 대출 가능한 기준 시각을 갱신하는 메소드.
	//특히 연체된 도서를 반납했을때, 대출 가능한 시각을 연체된 만큼 미루어야함.
	//============================================================================================
	public void updateCheckOutDate(HashMap param) throws Exception{
		if(sqlSession.update("book.updateCheckOutDate", param)==0) {
			throw new Exception();
		}
	}
	
	
	
	
	
	//============================================================================================
	//자신이 대출한 도서에 대해 반납을 처리하는 메소드.
	//============================================================================================
	public void returnBook(HashMap param) throws Exception{
		if(sqlSession.delete("book.returnBook", param)==0) {
			throw new Exception();
		}
	}
	
	
	
	
	
	//============================================================================================
	//자신의 대출 현황에 대해 대출 반납 기한을 7일 연장하는 메소드, 각 대출현황당 2번까지 가능함.
	//============================================================================================
	public void renewBook(HashMap param) throws Exception{
		if(sqlSession.update("book.renewBook", param)==0) {
			throw new Exception();
		}
	}
	
	
	
	
	
	//============================================================================================
	//대출 연장이 가능한지 여부를 조회하는 메소드.
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
	//연체된 대출정보가 존재하는 사용자들에게 연체 알림 메세지 일괄전송 요청을 처리하는 메소드.
	//============================================================================================
	public void sendMessage(HashMap param) throws Exception{
		sqlSession.insert("book.sendMessage", param);
	}
}
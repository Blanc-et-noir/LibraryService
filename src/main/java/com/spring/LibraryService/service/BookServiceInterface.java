package com.spring.LibraryService.service;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import com.spring.LibraryService.exception.book.ExhaustedRenewCountException;
import com.spring.LibraryService.exception.book.InvalidBookISBNException;
import com.spring.LibraryService.exception.book.RunOutOfBookNumberException;

public interface BookServiceInterface {
	//============================================================================================
	//입력받은 정보를 토대로 도서 대출 신청을 처리하는 메소드.
	//============================================================================================
	public HashMap checkOut(HashMap param) throws RunOutOfBookNumberException, InvalidBookISBNException, Exception;
	
	
	
	
	
	//============================================================================================
	//대출한 도서에 대해 반납을 처리하는 메소드.
	//============================================================================================
	public void returnBook(HashMap param) throws Exception;
	
	
	
	
	
	
	
	
	//============================================================================================
	//대출현황 목록을 조회하는 요청을 처리하는 메소드.
	//============================================================================================
	public HashMap listCheckOuts(HashMap param) throws Exception;
	
	
	
	
	//============================================================================================
	//연체된 대출정보가 존재하는 사용자들에게 연체 알림 메세지 일괄전송 요청을 처리하는 메소드.
	//============================================================================================
	public void sendMessage(HashMap param) throws Exception;
	
	
	
	
	
	
	//============================================================================================
	//도서 대출 반납기한 연장 요청을 처리하는 메소드.
	//============================================================================================
	public void renewBook(HashMap param, HttpServletRequest request) throws InvalidBookISBNException, ExhaustedRenewCountException, Exception;
}

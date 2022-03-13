package com.spring.LibraryService.service;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import com.spring.LibraryService.exception.book.ExhaustedRenewCountException;
import com.spring.LibraryService.exception.book.InvalidBookISBNException;
import com.spring.LibraryService.exception.book.RunOutOfBookNumberException;

public interface BookServiceInterface {
	//============================================================================================
	//�Է¹��� ������ ���� ���� ���� ��û�� ó���ϴ� �޼ҵ�.
	//============================================================================================
	public HashMap checkOut(HashMap param) throws RunOutOfBookNumberException, InvalidBookISBNException, Exception;
	
	
	
	
	
	//============================================================================================
	//������ ������ ���� �ݳ��� ó���ϴ� �޼ҵ�.
	//============================================================================================
	public void returnBook(HashMap param) throws Exception;
	
	
	
	
	
	
	
	
	//============================================================================================
	//������Ȳ ����� ��ȸ�ϴ� ��û�� ó���ϴ� �޼ҵ�.
	//============================================================================================
	public HashMap listCheckOuts(HashMap param) throws Exception;
	
	
	
	
	//============================================================================================
	//��ü�� ���������� �����ϴ� ����ڵ鿡�� ��ü �˸� �޼��� �ϰ����� ��û�� ó���ϴ� �޼ҵ�.
	//============================================================================================
	public void sendMessage(HashMap param) throws Exception;
	
	
	
	
	
	
	//============================================================================================
	//���� ���� �ݳ����� ���� ��û�� ó���ϴ� �޼ҵ�.
	//============================================================================================
	public void renewBook(HashMap param, HttpServletRequest request) throws InvalidBookISBNException, ExhaustedRenewCountException, Exception;
}

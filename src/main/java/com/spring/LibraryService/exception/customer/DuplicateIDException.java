package com.spring.LibraryService.exception.customer;

public class DuplicateIDException extends Exception{
	public DuplicateIDException(){
		super("�̹� ������� ���̵��Դϴ�.");
	}
	public DuplicateIDException(String message){
		super(message);
	}
}

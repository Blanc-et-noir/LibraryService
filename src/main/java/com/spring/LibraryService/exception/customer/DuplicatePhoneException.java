package com.spring.LibraryService.exception.customer;

public class DuplicatePhoneException extends Exception{
	public DuplicatePhoneException(){
		super("�̹� ������� ��ȭ��ȣ�Դϴ�.");
	}
	public DuplicatePhoneException(String message){
		super(message);
	}
}
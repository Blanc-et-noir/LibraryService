package com.spring.LibraryService.exception.customer;

public class InvalidPhoneException extends Exception{
	public InvalidPhoneException(){
		super("�ش� ��ȭ��ȣ�� ���Ե� ����� ������ �������� �ʽ��ϴ�.");
	}
	public InvalidPhoneException(String message){
		super(message);
	}
}
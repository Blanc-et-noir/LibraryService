package com.spring.LibraryService.exception.customer;

public class InvalidEmailException extends Exception{
	public InvalidEmailException(){
		super("�ش� �̸��Ϸ� ���Ե� ����� ������ �������� �ʽ��ϴ�.");
	}
	public InvalidEmailException(String message){
		super(message);
	}
}
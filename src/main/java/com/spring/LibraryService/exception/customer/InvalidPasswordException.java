package com.spring.LibraryService.exception.customer;

public class InvalidPasswordException extends Exception{
	public InvalidPasswordException(){
		super("���� ��й�ȣ�� �߸��Ǿ����ϴ�.");
	}
	public InvalidPasswordException(String message){
		super(message);
	}
}

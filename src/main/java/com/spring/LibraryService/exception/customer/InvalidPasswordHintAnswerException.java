package com.spring.LibraryService.exception.customer;

public class InvalidPasswordHintAnswerException extends Exception{
	public InvalidPasswordHintAnswerException(){
		super("��й�ȣ ã�� ������ ���� ���� �߸��Ǿ����ϴ�.");
	}
	public InvalidPasswordHintAnswerException(String message){
		super(message);
	}
}

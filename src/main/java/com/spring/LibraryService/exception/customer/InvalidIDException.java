package com.spring.LibraryService.exception.customer;

public class InvalidIDException extends Exception{
	public InvalidIDException(){
		super("�ش� ���̵�� ���Ե� ����� ������ �������� �ʽ��ϴ�.");
	}
	public InvalidIDException(String message){
		super(message);
	}
}
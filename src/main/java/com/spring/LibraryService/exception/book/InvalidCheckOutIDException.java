package com.spring.LibraryService.exception.book;

public class InvalidCheckOutIDException extends Exception{
	public InvalidCheckOutIDException() {
		super("�ش� ���������� �������� �ʽ��ϴ�.");
	}
	public InvalidCheckOutIDException(String message) {
		super(message);
	}
}

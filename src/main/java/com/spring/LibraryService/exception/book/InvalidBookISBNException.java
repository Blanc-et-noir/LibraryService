package com.spring.LibraryService.exception.book;

public class InvalidBookISBNException extends Exception{
	public InvalidBookISBNException() {
		super("�ش� ISBN �ڵ带 ���� ���� ������ �������� �ʽ��ϴ�.");
	}
	public InvalidBookISBNException(String message) {
		super(message);
	}
}

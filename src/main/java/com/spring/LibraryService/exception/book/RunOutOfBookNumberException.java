package com.spring.LibraryService.exception.book;

public class RunOutOfBookNumberException extends Exception{
	public RunOutOfBookNumberException() {
		super("�ش� ������ ����� �����մϴ�.");
	}
	public RunOutOfBookNumberException(String message) {
		super(message);
	}
}

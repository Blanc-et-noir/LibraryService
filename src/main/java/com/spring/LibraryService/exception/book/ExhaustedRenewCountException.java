package com.spring.LibraryService.exception.book;

public class ExhaustedRenewCountException extends Exception{
	public ExhaustedRenewCountException() {
		super("�ش� ������ ���� ���⿬�� Ƚ���� ��� �����߽��ϴ�.");
	}
	public ExhaustedRenewCountException(String message) {
		super(message);
	}
}

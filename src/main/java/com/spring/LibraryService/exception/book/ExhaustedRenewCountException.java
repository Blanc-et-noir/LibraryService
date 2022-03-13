package com.spring.LibraryService.exception.book;

public class ExhaustedRenewCountException extends Exception{
	public ExhaustedRenewCountException() {
		super("해당 도서에 대한 대출연장 횟수를 모두 소진했습니다.");
	}
	public ExhaustedRenewCountException(String message) {
		super(message);
	}
}

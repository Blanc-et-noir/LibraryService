package com.spring.LibraryService.exception.book;

public class RunOutOfBookNumberException extends Exception{
	public RunOutOfBookNumberException() {
		super("해당 도서의 재고량이 부족합니다.");
	}
	public RunOutOfBookNumberException(String message) {
		super(message);
	}
}

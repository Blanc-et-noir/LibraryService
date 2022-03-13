package com.spring.LibraryService.exception.book;

public class InvalidCheckOutIDException extends Exception{
	public InvalidCheckOutIDException() {
		super("해당 대출정보가 존재하지 않습니다.");
	}
	public InvalidCheckOutIDException(String message) {
		super(message);
	}
}

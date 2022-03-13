package com.spring.LibraryService.exception.book;

public class InvalidBookISBNException extends Exception{
	public InvalidBookISBNException() {
		super("해당 ISBN 코드를 갖는 도서 정보가 존재하지 않습니다.");
	}
	public InvalidBookISBNException(String message) {
		super(message);
	}
}

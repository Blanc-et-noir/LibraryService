package com.spring.LibraryService.exception.customer;

public class DuplicateIDException extends Exception{
	public DuplicateIDException(){
		super("이미 사용중인 아이디입니다.");
	}
	public DuplicateIDException(String message){
		super(message);
	}
}

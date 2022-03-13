package com.spring.LibraryService.exception.customer;

public class InvalidPasswordException extends Exception{
	public InvalidPasswordException(){
		super("기존 비밀번호가 잘못되었습니다.");
	}
	public InvalidPasswordException(String message){
		super(message);
	}
}

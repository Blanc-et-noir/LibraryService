package com.spring.LibraryService.exception.customer;

public class InvalidEmailException extends Exception{
	public InvalidEmailException(){
		super("해당 이메일로 가입된 사용자 정보가 존재하지 않습니다.");
	}
	public InvalidEmailException(String message){
		super(message);
	}
}